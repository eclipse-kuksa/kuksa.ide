/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.kuksa.yocto.ide.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;
import org.eclipse.che.agent.exec.shared.dto.ProcessStartResponseDto;
import org.eclipse.che.agent.exec.shared.dto.event.ProcessDiedEventDto;
import org.eclipse.che.agent.exec.shared.dto.event.ProcessStdErrEventDto;
import org.eclipse.che.api.core.model.workspace.config.Command;
import org.eclipse.che.api.promises.client.Function;
import org.eclipse.che.api.promises.client.FunctionException;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.commons.annotation.Nullable;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.command.exec.ExecAgentCommandManager;
import org.eclipse.che.ide.api.command.exec.ExecAgentConsumer;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.ide.api.selection.Selection;
import org.eclipse.che.ide.api.selection.SelectionAgent;
import org.eclipse.che.ide.api.workspace.model.MachineImpl;
import org.eclipse.che.ide.console.CommandConsoleFactory;
import org.eclipse.che.ide.console.CommandOutputConsole;
import org.eclipse.che.ide.machine.chooser.MachineChooser;
import org.eclipse.che.ide.processes.panel.ProcessesPanelPresenter;

// Initially taken from plugin-yaml

/**
 * This is a custom command executer to allow finned grained command handling
 *
 * @author Pedro Cuadra
 */
@Singleton
public class CustomSilentCommandExecutor {

  //  private static final Logger LOG = LoggerFactory.getLogger(YoctoSdkManager.class);

  private NotificationManager notificationManager;
  private final ExecAgentCommandManager execAgentClient;
  private final MachineChooser machineChooser;
  private final SelectionAgent selectionAgent;
  //  private final MacroProcessor macroProcessor;
  private final PromiseProvider promises;
  private final CommandConsoleFactory commandConsoleFactory;
  private final ProcessesPanelPresenter processesPanelPresenter;
  private final AppContext appContext;
  private boolean commandRunning;
  private ExecAgentConsumer<ProcessStartResponseDto> agent;

  @Inject
  public CustomSilentCommandExecutor(
      NotificationManager notificationManager,
      ExecAgentCommandManager execAgentClient,
      MachineChooser machineChooser,
      SelectionAgent selectionAgent,
      PromiseProvider promises,
      ProcessesPanelPresenter processesPanelPresenter,
      CommandConsoleFactory commandConsoleFactory,
      AppContext appContext) {
    this.notificationManager = notificationManager;
    this.execAgentClient = execAgentClient;
    this.machineChooser = machineChooser;
    this.selectionAgent = selectionAgent;
    //    this.macroProcessor = macroProcessor;
    this.promises = promises;
    this.commandConsoleFactory = commandConsoleFactory;
    this.processesPanelPresenter = processesPanelPresenter;
    this.appContext = appContext;
    this.commandRunning = false;
  }

  public void executeCommand(
      Command command,
      String machineName,
      StatusNotification notification,
      ArrayList<String> ignoreErrors) {
    final String name = command.getName();
    final String type = command.getType();
    final String commandLine = command.getCommandLine();
    final Map<String, String> attributes = command.getAttributes();
    final String orginalTitle = notification.getTitle();

    this.notificationManager.notify(notification);

    final CommandImpl expandedCommand = new CommandImpl(name, commandLine, type, attributes);
    final CommandOutputConsole console = commandConsoleFactory.create(expandedCommand, machineName);

    processesPanelPresenter.addCommandOutput(machineName, console, true);

    execAgentClient
        .startProcess(machineName, expandedCommand)
        .thenIfProcessStartedEvent(console.getProcessStartedConsumer())
        .thenIfProcessStdOutEvent(console.getStdOutConsumer())
        .thenIfProcessStdErrEvent(
            new Consumer<ProcessStdErrEventDto>() {

              @Override
              public void accept(ProcessStdErrEventDto arg0) {

                console.getStdErrConsumer().accept(arg0);

                if (ignoreErrors != null) {
                  for (String ignoreError : ignoreErrors) {
                    if (arg0.getText().contains(ignoreError)) {
                      return;
                    }
                  }
                }
                notification.setTitle(orginalTitle + " failed with " + arg0.getText());
                notification.setStatus(StatusNotification.Status.FAIL);
              }
            })
        //        .thenIfProcessStdOutEvent(console.getStdOutConsumer())
        .thenIfProcessDiedEvent(
            new Consumer<ProcessDiedEventDto>() { // Run command

              @Override
              public void accept(ProcessDiedEventDto arg0) {
                if (notification.getStatus() != StatusNotification.Status.FAIL) {
                  notification.setStatus(StatusNotification.Status.SUCCESS);
                }
                console.close();
              }
            });
  }

  public void executeCommand(CommandImpl command, StatusNotification notification) {
    this.executeCommand(command, notification, null);
  }

  public void executeCommand(
      CommandImpl command, StatusNotification notification, ArrayList<String> ignoreErrors) {
    final MachineImpl selectedMachine = getSelectedMachine();

    if (selectedMachine != null) {
      executeCommand(command, selectedMachine.getName(), notification, ignoreErrors);
    } else {
      machineChooser
          .show()
          .then(
              new Function<MachineImpl, Object>() {

                @Override
                public Object apply(MachineImpl machine) throws FunctionException {
                  executeCommand(command, machine.getName(), notification, ignoreErrors);
                  return null;
                }
              });
    }
  }

  /** Returns the currently selected machine or {@code null} if none. */
  @Nullable
  private MachineImpl getSelectedMachine() {
    final Selection<?> selection = selectionAgent.getSelection();

    if (selection != null && !selection.isEmpty() && selection.isSingleSelection()) {
      final Object possibleNode = selection.getHeadElement();

      if (possibleNode instanceof MachineImpl) {
        return (MachineImpl) possibleNode;
      }
    }

    return null;
  }
}
