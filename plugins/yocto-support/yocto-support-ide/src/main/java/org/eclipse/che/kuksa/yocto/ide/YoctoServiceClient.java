/*
 * Copyright (c) 2012-2018
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.che.kuksa.yocto.ide;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.api.promises.client.Promise;
import org.eclipse.che.ide.api.app.AppContext;
import org.eclipse.che.ide.dto.DtoFactory;
import org.eclipse.che.ide.rest.AsyncRequestFactory;
import org.eclipse.che.ide.rest.AsyncRequestLoader;
import org.eclipse.che.ide.rest.StringUnmarshaller;
import org.eclipse.che.ide.ui.loaders.request.LoaderFactory;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdk;
import org.eclipse.che.kuksa.yocto.shared.YoctoSdkPreferencesDTO;

/**
 * Client for consuming the sample server service.
 *
 * @author Pedro Cuadra
 */
@Singleton
public class YoctoServiceClient {

  private final LoaderFactory loaderFactory;
  private final AsyncRequestFactory asyncRequestFactory;
  private final AsyncRequestLoader loader;
  private final AppContext appContext;
  private final DtoFactory dtoFactory;

  /**
   * Constructor.
   *
   * @param appContext the {@link AppContext}
   * @param asyncRequestFactory the {@link AsyncRequestFactory} that is used to create requests
   * @param loaderFactory the {@link LoaderFactory} for displaying a message while waiting for a
   *     response
   */
  @Inject
  public YoctoServiceClient(
      LoaderFactory loaderFactory,
      AsyncRequestFactory asyncRequestFactory,
      AppContext appContext,
      DtoFactory dtoFactory) {
    this.loaderFactory = loaderFactory;
    this.asyncRequestFactory = asyncRequestFactory;
    this.loader = loaderFactory.newLoader();
    this.appContext = appContext;
    this.dtoFactory = dtoFactory;
  }

  public Promise<String> installSdk(YoctoSdk pref) {
    YoctoSdkPreferencesDTO prefDto =
        dtoFactory.createDto(YoctoSdkPreferencesDTO.class).withUrl(pref.getUrl());
    prefDto.setName(pref.getName());
    prefDto.setVersion(pref.getVersion());
    String url = getWsAgentBaseUrl() + "/yocto/installsdk";
    return asyncRequestFactory
        .createPostRequest(url, prefDto)
        .loader(loader)
        .send(new StringUnmarshaller());
  }

  private String getWsAgentBaseUrl() {
    return appContext.getWsAgentServerApiEndpoint();
  }
}
