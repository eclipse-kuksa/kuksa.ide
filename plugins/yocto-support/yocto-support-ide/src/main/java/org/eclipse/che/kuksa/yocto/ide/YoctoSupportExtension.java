/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa.yocto.ide;

import com.google.inject.Inject;
import org.eclipse.che.ide.api.action.ActionManager;
import org.eclipse.che.ide.api.action.DefaultActionGroup;
import org.eclipse.che.ide.api.action.IdeActions;
import org.eclipse.che.ide.api.extension.Extension;
import org.eclipse.che.kuksa.yocto.ide.action.MyAction;

/**
 * Server service extension that registers action which calls a service.
 *
 * @author Edgar Mueller
 */
@Extension(title = "Yocto Support Extension", version = "0.0.1")
public class YoctoSupportExtension {

  /**
   * Constructor.
   *
   * @param actionManager the {@link ActionManager} that is used to register our actions
   * @param myAction the action that calls the example server service
   */
  @Inject
  public YoctoSupportExtension(ActionManager actionManager, MyAction myAction) {

    actionManager.registerAction("myAction2", myAction);

    DefaultActionGroup mainContextMenuGroup =
        (DefaultActionGroup) actionManager.getAction(IdeActions.GROUP_MAIN_CONTEXT_MENU);
    mainContextMenuGroup.add(myAction);
  }
}
