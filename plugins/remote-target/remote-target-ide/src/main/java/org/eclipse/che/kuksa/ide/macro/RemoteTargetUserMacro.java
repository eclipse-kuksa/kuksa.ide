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
package org.eclipse.che.kuksa.ide.macro;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.validation.constraints.NotNull;

import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.api.promises.client.PromiseProvider;
import org.eclipse.che.ide.api.macro.BaseMacro;
import org.eclipse.che.kuksa.ide.RemoteTarget;

/**
 * Provides path to the environment file of the selected SDK
 *
 * @author Pedro Cuadra
 */
@Singleton
public class RemoteTargetUserMacro extends BaseMacro {

  private static final String KEY = "${remote.target.user}";
  private static final String DEFAULT_VALUE = "root";
  private static final String DESCRIPTION = "Remote Target User";

  private final PromiseProvider promises;
  private String expandVal;

  @Inject
  public RemoteTargetUserMacro(PromiseProvider promises) {
    super(KEY, DEFAULT_VALUE, DESCRIPTION);

    expandVal = DEFAULT_VALUE;
    this.promises = promises;
  }

  /** {@inheritDoc} */
  @NotNull
  @Override
  public Promise<String> expand() {
    return promises.resolve(expandVal);
  }

    public void setSelected(RemoteTarget pref) {
      this.expandVal = pref.getUser();
    }
}
