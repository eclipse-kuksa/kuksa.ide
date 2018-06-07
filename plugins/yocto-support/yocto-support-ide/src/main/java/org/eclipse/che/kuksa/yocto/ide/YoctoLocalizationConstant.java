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
package org.eclipse.che.kuksa.yocto.ide;

import com.google.gwt.i18n.client.Messages;

/**
 * Localization for Yaml Language Server.
 *
 * @author Joshua Pinkney
 */
public interface YoctoLocalizationConstant extends Messages {

  @Key("addSdk")
  String addSdk();

  @Key("deleteSdk")
  String deleteSdk();

  @Key("headerUiMessage")
  String headerUiMessage();

  @Key("addSdkUrlLabel")
  String addSdkUrlLabel();

  @Key("addSdkNameLabel")
  String addSdkNameLabel();

  @Key("addSdkVersionLabel")
  String addSdkVersionLabel();

  @Key("deleteSdkLabel")
  String deleteSdkLabel();

  @Key("selectColumnHeader")
  String selectColumnHeader();

  @Key("urlColumnHeader")
  String urlColumnHeader();

  @Key("sdkColumnHeader")
  String sdkColumnHeader();

  @Key("versionColumnHeader")
  String versionColumnHeader();

  @Key("deleteColumnHeader")
  String deleteColumnHeader();

  @Key("addSdkButtonText")
  String addSdkButtonText();
}
