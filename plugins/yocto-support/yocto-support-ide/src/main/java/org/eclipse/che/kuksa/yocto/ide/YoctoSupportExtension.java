/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa.yocto.ide;

import com.google.inject.Singleton;
import org.eclipse.che.ide.api.extension.Extension;

/**
 * Server service extension that registers action which calls a service.
 */
@Singleton
@Extension(title = "Yocto Support", version = "0.0.1")
public class YoctoSupportExtension {

  /** Constructor. */
  public YoctoSupportExtension() {}
}
