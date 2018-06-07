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
package org.eclipse.che.kuksa.yocto.ide.preferences;

/**
 * Yocto SDK Preferences object
 *
 * @author Pedro Cuadra
 */
public class YoctoSdkPreferences {

  private String version;
  private String name;
  private String url;
  private String glob;
  private boolean selected;

  public YoctoSdkPreferences(String name, String version, String url, String glob) {
    this.url = url;
    this.glob = glob;
    this.version = version;
    this.name = name;
    selected = false;
  }

  public YoctoSdkPreferences() {
    this.url = "http://unknown.sh";
    this.glob = "/*";
    this.version = "0.0.0";
    this.name = "unknown";
  }

  public String getName() {
    return this.name;
  }

  public String getVersion() {
    return this.version;
  }

  public String getUrl() {
    return this.url;
  }

  public String getGlob() {
    return this.glob;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setGlob(String glob) {
    this.glob = glob;
  }

  public boolean isSelected() {
    return this.selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}
