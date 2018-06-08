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

  /**
   * Constructor.
   *
   * @param notificationManager the notification manager
   * @param serviceClient the client that is used to create requests
   */
  @Inject
  public MyAction(
      final NotificationManager notificationManager,
      final YoctoServiceClient serviceClient) {
    super("My Action67", "My Action 2 Description");
    this.notificationManager = notificationManager;
    this.serviceClient = serviceClient;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    YoctoSdk pref = new YoctoSdk();

  }


}
