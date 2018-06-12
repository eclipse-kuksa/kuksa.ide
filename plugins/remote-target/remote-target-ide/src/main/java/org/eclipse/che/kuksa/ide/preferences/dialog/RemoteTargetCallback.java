package org.eclipse.che.kuksa.ide.preferences.dialog;

import org.eclipse.che.kuksa.ide.RemoteTarget;

public interface RemoteTargetCallback {
  /**
   * Action called when the user clicks on OK.
   *
   * @param value the string typed into input dialog
   */
  void accepted(RemoteTarget value);
}
