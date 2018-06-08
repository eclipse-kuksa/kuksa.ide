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
import org.eclipse.che.ide.CoreLocalizationConstant;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.api.macro.Macro;
import org.eclipse.che.kuksa.yocto.ide.preferences.YoctoSdkManager;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;

/**
 * Provides path to the environment file of the selected SDK
 *
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoSdkEnvPathMacro implements Macro {

  private static final String KEY = "${yocto.sdk.env.path}";

  private final AppContext appContext;
  private final PromiseProvider promises;
  private final CoreLocalizationConstant localizationConstants;
  private String expandVal;

  @Inject
  public YoctoSdkEnvPathMacro(
      AppContext appContext,
      PromiseProvider promises,
      CoreLocalizationConstant localizationConstants) {
    this.appContext = appContext;
    this.promises = promises;
    this.localizationConstants = localizationConstants;
  }

  @NotNull
  @Override
  public String getName() {
    return KEY;
  }

  @Override
  public String getDescription() {
    return "Environment file of the selected Yocto-based SDK";
  }

  public void setSelectedSdk(YoctoSdk pref) {
    expandVal = YoctoSdkManager.SDK_ROOT_PATH;
  }

  @NotNull
  @Override
  public Promise<String> expand() {
    return promises.resolve(expandVal);
  }
}
