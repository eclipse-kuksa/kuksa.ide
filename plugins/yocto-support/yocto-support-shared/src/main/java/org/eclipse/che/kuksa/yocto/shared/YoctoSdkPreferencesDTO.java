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
package org.eclipse.che.kuksa.yocto.shared;

import org.eclipse.che.dto.shared.DTO;

/**
 * Interface of DTO for sending schemas
 *
 * @author Joshua Pinkney
 */
@DTO
public interface YoctoSdkPreferencesDTO {

  //  YoctoSdkPreferencesDTO withPreferences(YoctoSdkPreferences pref);
  YoctoSdkPreferencesDTO withUrl(String url);

  String getUrl();

  String getName();

  String getVersion();

  void setUrl(String url);

  void setName(String name);

  void setVersion(String version);
}
