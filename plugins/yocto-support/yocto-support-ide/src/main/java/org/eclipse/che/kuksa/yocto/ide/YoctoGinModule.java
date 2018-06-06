/*
 * Copyright (c) 2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Pedro Cuadra
 */
package org.eclipse.che.kuksa.yocto.ide;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.ide.api.macro.Macro;
import org.eclipse.che.ide.api.preferences.PreferencePagePresenter;
import org.eclipse.che.kuksa.yocto.ide.command.YoctoCommandType;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkEnvPathMacro;
import org.eclipse.che.kuksa.yocto.ide.macro.YoctoSdkPathMacro;
import org.eclipse.che.kuksa.yocto.ide.preferences.YoctoExtensionManagerPresenter;
import org.eclipse.che.kuksa.yocto.ide.preferences.YoctoExtensionManagerView;
import org.eclipse.che.kuksa.yocto.ide.preferences.YoctoExtensionManagerViewImpl;

/**
 * GIN module for Che Yocto extension.
 *
 * @author Pedro Cuadra
 */
@ExtensionGinModule
public class YoctoGinModule extends AbstractGinModule {

  @Override
  protected void configure() {

    GinMultibinder<Macro> macrosBinder = GinMultibinder.newSetBinder(binder(), Macro.class);
    macrosBinder.addBinding().to(YoctoSdkPathMacro.class);
    macrosBinder.addBinding().to(YoctoSdkEnvPathMacro.class);

    GinMultibinder.newSetBinder(binder(), CommandType.class)
        .addBinding()
        .to(YoctoCommandType.class);

    bind(YoctoExtensionManagerView.class)
        .to(YoctoExtensionManagerViewImpl.class)
        .in(Singleton.class);

    GinMultibinder.newSetBinder(binder(), PreferencePagePresenter.class)
        .addBinding()
        .to(YoctoExtensionManagerPresenter.class);
  }
}
