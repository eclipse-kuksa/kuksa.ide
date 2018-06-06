/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa.inject;

import com.google.inject.AbstractModule;
import org.eclipse.che.inject.DynaModule;
import org.eclipse.che.kuksa.YoctoSupportService;

/** Server service example Guice module for setting up a simple service. */
@DynaModule
public class YoctoSupportGuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(YoctoSupportService.class);
  }
}
