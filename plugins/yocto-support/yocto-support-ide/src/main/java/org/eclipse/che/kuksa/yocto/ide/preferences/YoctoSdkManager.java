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
package org.eclipse.che.kuksa.yocto.ide.preferences;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.kuksa.yocto.ide.YoctoLocalizationConstant;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkEnvPathMacro;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkPathMacro;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Initially taken from plugin-yaml

/**
 * The presenter for managing the YoctoSdkCellTable in YoctoExtensionManagerView.
 *
 * @author Joshua Pinkney
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoSdkManager {

  private static final Logger LOG = LoggerFactory.getLogger(YoctoSdkManager.class);
  private List<YoctoSdk> yoctoSdkList;
  private YoctoLocalizationConstant local;
  private NotificationManager notificationManager;
  //  private final CommandExecutor commandExecutor;
  //  private final MacroProcessor macroProcessor;
  //  private final CommandConsoleFactory commandConsoleFactory;
  //  private final ProcessesPanelPresenter processesPanelPresenter;
  //  private final ExecAgentCommandManager execAgentClient;
  //  private final MachineChooser machineChooser;
  //  private final SelectionAgent selectionAgent;
  private final YoctoSdkEnvPathMacro yoctoSdkEnvPathMacro;
  private final YoctoSdkPathMacro yoctoSdkPathMacro;
  public static final String SDK_ROOT_PATH = "/projects/.sdk";
  private static final String SDK_TMP_PATH = "/projects/.sdk/tmp";
  //  private final WsAgentServerUtil wsAgentServerUtil;

  @Inject
  public YoctoSdkManager(
      YoctoLocalizationConstant local,
      NotificationManager notificationManager,
      YoctoSdkEnvPathMacro yoctoSdkEnvPathMacro,
      YoctoSdkPathMacro yoctoSdkPathMacro) {
    this.local = local;
    this.notificationManager = notificationManager;
    //    this.commandExecutor = commandExecutor;
    this.yoctoSdkList = new ArrayList<YoctoSdk>();
    //    this.macroProcessor = macroProcessor;
    //    this.commandConsoleFactory = commandConsoleFactory;
    //    this.processesPanelPresenter = processesPanelPresenter;
    //    this.execAgentClient = execAgentClient;
    //    this.machineChooser = machineChooser;
    //    this.selectionAgent = selectionAgent;
    this.yoctoSdkEnvPathMacro = yoctoSdkEnvPathMacro;
    this.yoctoSdkPathMacro = yoctoSdkPathMacro;
    //    this.wsAgentServerUtil = wsAgentServerUtil;
  }

  private void executeCommand(final CommandImpl cmd) {
    //    wsAgentServerUtil
    //        .getWsAgentServerMachine()
    //        .ifPresent(m -> commandExecutor.executeCommand(cmd, m.getName()));
  }

  //  public void executeCommand(Command command, String machineName) {
  //    final String name = command.getName();
  //    final String commandLine = command.getCommandLine();
  //    final String type = command.getType();
  //    final Map<String, String> attributes = command.getAttributes();
  //
  //    macroProcessor
  //        .expandMacros(commandLine)
  //        .then(
  //            new Operation<String>() {
  //              @Override
  //              public void apply(String expandedCommandLine) throws OperationException {
  //                final CommandImpl expandedCommand =
  //                    new CommandImpl(name, expandedCommandLine, type, attributes);
  //                final CommandOutputConsole console =
  //                    commandConsoleFactory.create(expandedCommand, machineName);
  //
  //                processesPanelPresenter.addCommandOutput(machineName, console, true);
  //
  //                execAgentClient
  //                    .startProcess(machineName, expandedCommand)
  //                    .thenIfProcessStartedEvent(console.getProcessStartedConsumer())
  //                    .thenIfProcessDiedEvent(console.getProcessDiedConsumer())
  //                    .thenIfProcessStdOutEvent(console.getStdOutConsumer())
  //                    .thenIfProcessStdErrEvent(console.getStdErrConsumer());
  //              }
  //            });
  //  }
  //
  //  public void executeCommand(CommandImpl command) {
  //    final MachineImpl selectedMachine = getSelectedMachine();
  //
  //    if (selectedMachine != null) {
  //      executeCommand(command, selectedMachine.getName());
  //    } else {
  //      machineChooser
  //          .show()
  //          .then(
  //              new Operation<MachineImpl>() {
  //                @Override
  //                public void apply(MachineImpl machine) throws OperationException {
  //                  executeCommand(command, machine.getName());
  //                }
  //              });
  //    }
  //  }

  //  /** Returns the currently selected machine or {@code null} if none. */
  //  @Nullable
  //  private MachineImpl getSelectedMachine() {
  //    final Selection<?> selection = selectionAgent.getSelection();
  //
  //    if (selection != null && !selection.isEmpty() && selection.isSingleSelection()) {
  //      final Object possibleNode = selection.getHeadElement();
  //
  //      if (possibleNode instanceof MachineImpl) {
  //        return (MachineImpl) possibleNode;
  //      }
  //    }
  //
  //    return null;
  //  }

  private String getInstallDirectory(final YoctoSdk pref) {
    return YoctoSdkManager.SDK_ROOT_PATH + "/" + pref.getName() + "/" + pref.getVersion();
  }

  private String getDownloadPath(final YoctoSdk pref) {
    return YoctoSdkManager.SDK_TMP_PATH + "/" + pref.getName() + "_" + pref.getVersion() + ".sh";
  }

  private void installSdk(final YoctoSdk pref) {
    String cmdLine = "";

    cmdLine = "mkdir -p " + YoctoSdkManager.SDK_TMP_PATH + " && ";
    cmdLine += "cd " + YoctoSdkManager.SDK_TMP_PATH + " && ";
    cmdLine += "curl -L " + pref.getUrl() + " -o " + getDownloadPath(pref) + " && ";
    cmdLine += "chmod +x " + getDownloadPath(pref) + " && ";
    cmdLine += getDownloadPath(pref) + " -y -d " + getInstallDirectory(pref);

    //    cmdLine = "pwd";

    CommandImpl installSdk = new CommandImpl("Install SDK", cmdLine, "yocto");
    this.executeCommand(installSdk);
  }

  public void addSdk(final YoctoSdk pref) {
    installSdk(pref);
    this.yoctoSdkList.add(pref);
    //    notificationManager.notify(
    //        pref.getName() + " (" + pref.getVersion() + ")",
    //        StatusNotification.Status.SUCCESS,
    //        StatusNotification.DisplayMode.FLOAT_MODE);
  }

  private void uninstallSdk(final YoctoSdk pref) {
    String cmdLine = "rm -rf " + getInstallDirectory(pref);

    CommandImpl removeSdk = new CommandImpl("Remove SDK", cmdLine, "yocto");
    this.executeCommand(removeSdk);
  }

  /**
   * Delete a preference from Yaml Preferences
   *
   * @param pref The preference you would like to delete
   */
  public void removeSdk(final YoctoSdk pref) {
    this.uninstallSdk(pref);
    this.yoctoSdkList.remove(pref);
  }

  public void selectSdk(final YoctoSdk pref) {

    for (YoctoSdk curr_pref : this.yoctoSdkList) {
      curr_pref.setSelected(false);
    }

    pref.setSelected(true);
    this.yoctoSdkEnvPathMacro.setSelectedSdk(pref);
  }

  public List<YoctoSdk> getAll() {
    return this.yoctoSdkList;
  }
}
