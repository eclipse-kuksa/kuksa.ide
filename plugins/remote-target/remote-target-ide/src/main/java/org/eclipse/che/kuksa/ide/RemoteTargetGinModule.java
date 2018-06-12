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
package org.eclipse.che.kuksa.ide;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.inject.client.multibindings.GinMultibinder;
import com.google.inject.Singleton;

import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.ide.api.preferences.PreferencePagePresenter;
import org.eclipse.che.kuksa.ide.macro.RemoteTargetHostnameMacro;
import org.eclipse.che.kuksa.ide.macro.RemoteTargetMacroRegistrar;

/**
 * GIN module for Che Yocto extension.
 *
 * @author Pedro Cuadra
 */
@ExtensionGinModule
public class RemoteTargetGinModule extends AbstractGinModule {

  @Override
  protected void configure() {


    // All macros
    bind(RemoteTargetHostnameMacro.class).asEagerSingleton();
        
    
    bind(RemoteTargetMacroRegistrar.class).asEagerSingleton();
    

//    bind(YoctoExtensionManagerView.class)
//        .to(YoctoExtensionManagerViewImpl.class)
//        .in(Singleton.class);
//
//    bind(YoctoSdkInputDialogView.class).to(YoctoSdkInputDialogViewImpl.class).in(Singleton.class);
//
//    bind(YoctoSdkManager.class);
//    bind(YoctoSdkInputDialogFooter.class);
//    bind(YoctoSdkInputDialogPresenter.class);
//
//    GinMultibinder.newSetBinder(binder(), PreferencePagePresenter.class)
//        .addBinding()
//        .to(YoctoExtensionManagerPresenter.class);
  }
}
