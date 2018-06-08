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

/**
 * Implementation of {@link YoctoSdkPreferencesDTO}
 *
 * @author Joshua Pinkney
 */
public class YoctoSdkPreferencesDTOImpl implements YoctoSdkPreferencesDTO {

  private String version;
  private String name;
  private String url;
  private String glob;
  private boolean selected;

  @Override
  public YoctoSdkPreferencesDTO withUrl(String url) {
    this.url = url;
    return this;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public String getVersion() {
    return this.version;
  }

  @Override
  public void setUrl(String url) {
    this.url = url;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setVersion(String version) {
    this.version = version;
  }
}
