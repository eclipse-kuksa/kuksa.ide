/*
 * Copyright (c) 2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Pedro Cuadra - pjcuadra@gmail.com
 */
package org.eclipse.che.kuksa.ide;

import com.google.gwt.i18n.client.Messages;

/**
 * Localization for Remote Target plugin.
 *
 * @author Pedro Cuadra
 */
public interface RemoteTargetLocalizationConstant extends Messages {

  @Key("addRemoteTarget")
  String addRemoteTarget();

  @Key("deleteRemoteTarget")
  String deleteRemoteTarget();

  @Key("headerUiMessage")
  String headerUiMessage();

  @Key("addRemoteTargetHostnameLabel")
  String addRemoteTargetHostnameLabel();

  @Key("addRemoteTargetUserLabel")
  String addRemoteTargetUserLabel();

  @Key("deleteRemoteTargetLabel")
  String deleteRemoteTargetLabel();

  @Key("selectColumnHeader")
  String selectColumnHeader();

  @Key("hostnameColumnHeader")
  String hostnameColumnHeader();

  @Key("userColumnHeader")
  String userColumnHeader();

  @Key("deleteColumnHeader")
  String deleteColumnHeader();

  @Key("addRemoteTargetButtonText")
  String addRemoteTargetButtonText();
}
