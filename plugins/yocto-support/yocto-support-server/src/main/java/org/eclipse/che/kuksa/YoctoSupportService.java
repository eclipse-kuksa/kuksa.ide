/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import org.eclipse.che.api.core.ApiException;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdkPreferencesDTO;

/**
 * Example server service that greets the
 * usehttps://download.automotivelinux.org/AGL/release/dab/4.0.2/raspberrypi3/deploy/sdk/poky-agl-glibc-x86_64-agl-demo-platform-crosssdk-armv7vehf-neon-vfpv4-toolchain-4.0.2.shr.
 *
 * @author Edgar Mueller
 */
@Path("yocto")
public class YoctoSupportService {

  //  private EventService eventService;
  //  private WorkspaceManager workspaceManager;
  //  private ExecAgentClientFactory execAgentClientFactory;
  //  private AppContext appContext;

  @Inject
  public YoctoSupportService() {
    //    this.eventService = eventService;
    //    this.workspaceManager = workspaceManager;
    //    this.execAgentClientFactory = execAgentClientFactory;
    //    this.appContext = appContext;

    //    java.lang.Runtime rt = java.lang.Runtime.getRuntime();
    //    // Start a new process: UNIX command ls
    //    java.lang.Process p = rt.exec("mkdir test_auto");
  }

  // Use this to source the selected environment
  //  @PostConstruct
  //  public void start() {
  //    //    eventService.subscribe(
  //    //        new EventSubscriber<WorkspaceStatusEvent>() {
  //    //          @Override
  //    //          public void onEvent(WorkspaceStatusEvent event) {
  //    //            if (event.getStatus() != WorkspaceStatus.RUNNING) {
  //    //              return;
  //    //            }
  //    //            CompletableFuture.runAsync(
  //    //                ThreadLocalPropagateContext.wrap(
  //    //                    () -> injectPublicKeys(event.getWorkspaceId())));
  //    //          }
  //    //        });
  //  }

  /**
   * Route for getting getting schemas from client side and injecting them into yaml language server
   *
   * @param yamlDto A yamlDTO containing the list of schemas you would like to add
   */
  @POST
  @Path("installsdk")
  @Consumes(MediaType.APPLICATION_JSON)
  public String installSdk(YoctoSdkPreferencesDTO yoctoPrefDto) throws ApiException {

    if (yoctoPrefDto != null) {

      String version = yoctoPrefDto.getVersion();
      String name = yoctoPrefDto.getName();

      return name + "_" + version;
    }

    return "";
  }
}
