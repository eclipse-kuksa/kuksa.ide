/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa.yocto.ide.action;

import com.google.inject.Inject;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.action.BaseAction;
import org.eclipse.che.ide.api.command.CommandExecutor;
import org.eclipse.che.ide.api.command.CommandImpl;
import org.eclipse.che.ide.api.command.CommandManager;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.workspace.WsAgentServerUtil;
import org.eclipse.che.kuksa.yocto.ide.YoctoServiceClient;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;

/**
 * Actions that triggers the sample server service call.
 *
 * @author Edgar Mueller
 */
public class MyAction extends BaseAction {

  private final NotificationManager notificationManager;
  private final YoctoServiceClient serviceClient;
  private final CommandManager commandManager;
  private final CommandExecutor commandExecutor;
  private final WsAgentServerUtil wsAgentServerUtil;

  /**
   * Constructor.
   *
   * @param notificationManager the notification manager
   * @param serviceClient the client that is used to create requests
   */
  @Inject
  public MyAction(
      final NotificationManager notificationManager,
      final YoctoServiceClient serviceClient,
      CommandManager commandManager,
      CommandExecutor commandExecutor,
      WsAgentServerUtil wsAgentServerUtil) {
    super("My Action 2", "My Action 2 Description");
    this.notificationManager = notificationManager;
    this.serviceClient = serviceClient;
    this.commandManager = commandManager;
    this.commandExecutor = commandExecutor;
    this.wsAgentServerUtil = wsAgentServerUtil;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    YoctoSdk pref = new YoctoSdk();
    //    CommandImpl cmd = new CommandImpl("test", "mkdir test_auto_action", "yocto");
    //    this.cmdExec.executeCommand(cmd);
    //    this.serviceClient
    //        .installSdk(pref)
    //        .then(
    //            new Operation<String>() {
    //              @Override
    //              public void apply(String response) throws OperationException {
    //                // This passes the response String to the notification manager.
    //                notificationManager.notify(
    //                    response,
    //                    StatusNotification.Status.SUCCESS,
    //                    StatusNotification.DisplayMode.FLOAT_MODE);
    //              }
    //            })
    //        .catchError(
    //            new Operation<PromiseError>() {
    //              @Override
    //              public void apply(PromiseError error) throws OperationException {
    //                notificationManager.notify(
    //                    "Fail",
    //                    StatusNotification.Status.FAIL,
    //                    StatusNotification.DisplayMode.FLOAT_MODE);
    //              }
    //            });

    final String name = "test";
    CommandImpl cmd = new CommandImpl("test", "mkdir test_auto_action", "yocto");

    wsAgentServerUtil
        .getWsAgentServerMachine()
        .ifPresent(m -> commandExecutor.executeCommand(cmd, m.getName()));
  }
}
