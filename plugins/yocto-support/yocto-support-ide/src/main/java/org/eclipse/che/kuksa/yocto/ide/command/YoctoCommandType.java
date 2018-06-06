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
package org.eclipse.che.kuksa.yocto.ide.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.che.ide.api.command.CommandPage;
import org.eclipse.che.ide.api.command.CommandType;
import org.eclipse.che.ide.api.icon.Icon;
import org.eclipse.che.ide.api.icon.IconRegistry;
import org.eclipse.che.ide.machine.MachineResources;

/**
 * Yocto command type.
 *
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoCommandType implements CommandType {

  public static final String COMMAND_TEMPLATE = "source ${yocto.sdk.env.path} && make";

  private static final String ID = "yocto";

  private final List<CommandPage> pages;

  @Inject
  public YoctoCommandType(MachineResources resources, IconRegistry iconRegistry) {
    pages = new LinkedList<>();

    iconRegistry.registerIcon(new Icon("command.type." + ID, resources.customCommandType()));
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public String getDisplayName() {
    return "Yocto";
  }

  @Override
  public String getDescription() {
    return "Command for launching command line tools using Yocto-generated toolchain";
  }

  @Override
  public List<CommandPage> getPages() {
    return pages;
  }

  @Override
  public String getCommandLineTemplate() {
    return COMMAND_TEMPLATE;
  }

  @Override
  public String getPreviewUrlTemplate() {
    return "";
  }
}
