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
package org.eclipse.che.kuksa.yocto.ide.macro;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.validation.constraints.NotNull;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.ide.api.macro.BaseMacro;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.kuksa.yocto.ide.YoctoConstants;
import org.eclipse.che.kuksa.yocto.ide.YoctoSdk;

/**
 * Provides path to the installation directory of the selected SDK
 *
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoSdkPathMacro extends BaseMacro {

  private static final String KEY = "${yocto.sdk.path}";
  private static final String DEFAULT_VALUE = "/opt/";
  private static final String DESCRIPTION = "Installation path to the selected Yocto-based SDK";
  private static final String FAIL_MESSAGE = "Could not expand " + KEY + " Macro. No SDK selected.";

  private final NotificationManager notificationManager;
  private final PromiseProvider promises;
  private YoctoSdk sel;
  private boolean thrown;

  @Inject
  public YoctoSdkPathMacro(PromiseProvider promises, NotificationManager notificationManager) {
    super(KEY, DEFAULT_VALUE, DESCRIPTION);

    this.promises = promises;
    this.notificationManager = notificationManager;
    this.sel = null;
    this.thrown = false;
  }

  /** {@inheritDoc} */
  @NotNull
  @Override
  public Promise<String> expand() {
    if (this.sel == null) {

      if (!thrown) {
        notificationManager.notify(
            new StatusNotification(
                FAIL_MESSAGE,
                StatusNotification.Status.WARNING,
                StatusNotification.DisplayMode.FLOAT_MODE));
        thrown = true;
      }

      return promises.resolve("");
    }

    String path = YoctoConstants.SDK_ROOT_PATH;

    path += "/" + this.sel.getName();
    path += "/" + this.sel.getVersion();

    return promises.resolve(path);
  }

  public void setSelected(YoctoSdk pref) {
    this.sel = pref;
    this.thrown = false;
  }

  public void deselect() {
    this.sel = null;
    this.thrown = false;
  }
}
