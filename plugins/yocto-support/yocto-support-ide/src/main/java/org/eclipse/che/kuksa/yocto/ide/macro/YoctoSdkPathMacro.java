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
import org.eclipse.che.ide.api.macro.BaseMacro;
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

  private final AppContext appContext;
  private final PromiseProvider promises;
  private final CoreLocalizationConstant localizationConstants;
  private String expandVal;

  @Inject
  public YoctoSdkPathMacro(
      AppContext appContext,
      PromiseProvider promises,
      CoreLocalizationConstant localizationConstants) {
    super(KEY, DEFAULT_VALUE, DESCRIPTION);
    expandVal = "def";
    this.appContext = appContext;
    this.promises = promises;
    this.localizationConstants = localizationConstants;
  }

  /** {@inheritDoc} */
  @NotNull
  @Override
  public Promise<String> expand() {
    return promises.resolve(expandVal);
  }

  public void setSelectedSdk(YoctoSdk pref) {
    String path = YoctoConstants.SDK_ROOT_PATH;

    path += "/" + pref.getName();
    path += "/" + pref.getVersion();

    this.expandVal = path;
  }
}
