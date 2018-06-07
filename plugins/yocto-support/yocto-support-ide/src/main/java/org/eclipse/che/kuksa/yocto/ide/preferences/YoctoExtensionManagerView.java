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
package org.eclipse.che.kuksa.yocto.ide.preferences;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.eclipse.che.ide.api.mvp.View;

/**
 * The view of {@link YoctoExtensionManagerPresenter}.
 *
 * @author Joshua Pinkney
 */
public interface YoctoExtensionManagerView extends View<YoctoExtensionManagerView.ActionDelegate> {
  /** Needs for delegate some function into YoctoExtensionManagerPresenter. */
  interface ActionDelegate {
    /**
     * Performs any actions appropriate in response to the user having pressed the Delete button.
     *
     * @param prefKey prefKey what needs to be deleted
     */
    void onDeleteClicked(@NotNull YoctoSdkPreferences prefKey);

    /**
     * Performs any actions appropriate in response to the user having pressed the Delete button.
     *
     * @param prefKey prefKey what needs to be deleted
     */
    void onSelectClicked(@NotNull YoctoSdkPreferences pairKey);

    /**
     * Performs any actions appropriate in response to the user having pressed the Add url button.
     */
    void onAddSdkClicked();

    /** Sets the current state to dirty. */
    void nowDirty();
  }

  /**
   * Set pairs into view.
   *
   * @param pairs available pairs
   */
  void setPairs(@NotNull List<YoctoSdkPreferences> pairs);
}
