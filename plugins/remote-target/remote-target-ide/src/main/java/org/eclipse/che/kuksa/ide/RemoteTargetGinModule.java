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

import org.eclipse.che.ide.api.extension.ExtensionGinModule;
import org.eclipse.che.ide.api.preferences.PreferencePagePresenter;
import org.eclipse.che.kuksa.ide.macro.RemoteTargetHostnameMacro;
import org.eclipse.che.kuksa.ide.macro.RemoteTargetMacroRegistrar;
import org.eclipse.che.kuksa.ide.macro.RemoteTargetUserMacro;
import org.eclipse.che.kuksa.ide.preferences.RemoteTargetManager;
import org.eclipse.che.kuksa.ide.preferences.RemoteTargetManagerPresenter;
import org.eclipse.che.kuksa.ide.preferences.RemoteTargetManagerView;
import org.eclipse.che.kuksa.ide.preferences.RemoteTargetManagerViewImpl;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetInputDialogFooter;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetInputDialogPresenter;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetInputDialogView;
import org.eclipse.che.kuksa.ide.preferences.dialog.RemoteTargetInputDialogViewImpl;

/**
 * GIN module for Che Remote Target extension.
 *
 * @author Pedro Cuadra
 */
@ExtensionGinModule
public class RemoteTargetGinModule extends AbstractGinModule {

  @Override
  protected void configure() {

    // All macros
    bind(RemoteTargetHostnameMacro.class).asEagerSingleton();
    bind(RemoteTargetUserMacro.class).asEagerSingleton();

    // Marcro registrar
    bind(RemoteTargetMacroRegistrar.class).asEagerSingleton();

    bind(RemoteTargetManagerView.class).to(RemoteTargetManagerViewImpl.class).in(Singleton.class);

    bind(RemoteTargetInputDialogView.class)
        .to(RemoteTargetInputDialogViewImpl.class)
        .in(Singleton.class);

    bind(RemoteTargetManager.class);
    bind(RemoteTargetInputDialogFooter.class);
    bind(RemoteTargetInputDialogPresenter.class);

    GinMultibinder.newSetBinder(binder(), PreferencePagePresenter.class)
        .addBinding()
        .to(RemoteTargetManagerPresenter.class);
  }
}
