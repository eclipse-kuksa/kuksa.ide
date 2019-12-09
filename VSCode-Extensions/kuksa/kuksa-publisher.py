#!/usr/bin/env python

#  Copyright (c) 2019 Eclipse KUKSA project
#
#  This program and the accompanying materials are made available under the
#  terms of the Eclipse Public License 2.0 which is available at
#  http://www.eclipse.org/legal/epl-2.0
#
#  SPDX-License-Identifier: EPL-2.0
#
#  Contributors: Robert Bosch GmbH

import json, yaml
import os
import subprocess
from datetime import datetime
import requests
from requests import Session, HTTPError

def __handle_error(response):
    try:
        response.raise_for_status()
    except HTTPError as error:
        print("HTTP Error", response.status_code)
        return -1
    return 0

def upload_artifacts(config_file, app_id, keycloak_token):
    with open(config_file, mode='r') as __config_file:
        y = yaml.safe_load(__config_file)
        config = json.dumps(y)
        config = json.loads(config)

    app_image_file = '{}-{}.tar.bz2'.format("docker", "image")

    app_image = config['general']['img']
    app_image_new = '{}__{}-{}'.format(app_image, config['general']['name'], config['general']['version'])

    subprocess.run('docker tag {} {}'.format(app_image, app_image_new), shell=True)

    headers = {
        'Accept': 'application/json',
        'Authorization': keycloak_token
    }

    # check if artifacts existing and delete them
    response_get = requests.get('{}/api/1.0/app/{}/artifacts'.format(config['appstore']['url'], app_id),
                                headers=headers)

    # Status code 200 indicates that artifacts for app exists
    if (response_get.status_code == 200):
        content_get = response_get.content
        content_get = json.loads(content_get.decode("utf-8"))
        for artifact in content_get:
            artifact_id = artifact.get('id')
            if artifact_id:
                response_delete = requests.delete(
                    '{}/api/1.0/app/{}/artifact/{}'.format(config['appstore']['url'], app_id, artifact_id),
                    headers=headers)

    # Generate docker image
    app_image_file = 'docker-image.tar.bz2'

    # ToDO: check
    success = subprocess.run('docker save {} | bzip2 -9 > {}'.format(app_image, app_image_file), shell=True).returncode
    success = True

    # upload the docker image and config file via the Appstore to the according software module in hawkBit
    if success:
        print("Docker image created")
        img_data = {
            'filename': 'docker-image.tar.bz2'
        }
        img_file = {
            'file': open(app_image_file, mode='rb')
        }

        response_post_img = requests.post('{}/api/1.0/app/{}/artifact'.format(config['appstore']['url'], app_id), headers=headers, data=img_data, files=img_file)


        docker_container = config['docker']
        docker_container['image'] = app_image_new
        with open('docker-container.json', 'w') as outfile:
            json.dump(docker_container, outfile)
        config_data = {
            'filename': 'docker-container.json'
        }
        config_file = {
            'file': open('docker-container.json', mode='rb')
        }

        response_post_config = requests.post('{}/api/1.0/app/{}/artifact'.format(config['appstore']['url'], app_id),
                                             headers=headers, data=config_data, files=config_file)

        if __handle_error(response_post_img) != 0 | __handle_error(response_post_config) != 0:
        #if __handle_error(response_post_config) != 0:
            print("Artifact couldn't be uploaded to hawkBit")
        else:
            print("Artifact successfully uploaded to hawkBit")

    else:
        print("Docker image could not be created")
    # os.remove(app_image_file)

def get_or_create_app(config_file, cat_id, keycloak_token):
    with open(config_file, mode='r') as __config_file:
        y = yaml.safe_load(__config_file)
        config = json.dumps(y)
        config = json.loads(config)
    app_name = config['general']['name']
    headers = {
        'Authorization': keycloak_token,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    }
    # always returns status code 200, even when there is no app. TODO check with appstore developer
    response_get = requests.get('{}/api/1.0/app/category/{}/{}'.format(config['appstore']['url'], cat_id, app_name),
                                headers=headers)
    if __handle_error(response_get) == 0:
        content_get = response_get.content
        content_get = json.loads(content_get.decode("utf-8"))
        app_content = content_get.get('content')

        payload = {
            'downloadcount' : '0',
            'appcategory': {
                'id': cat_id
            },
            'name' : app_name,
            'version' : config['general']['version'],
            'description' : config['general']['description'],
            'owner' : config['general']['owner'],
            'publishdate' : datetime.utcnow().isoformat()
        }

        # app already exists, update content and return app ID
        if app_content:
            print("App already exists in the Appstore. Information will be updated")
            appId = app_content[0].get('id')
            response_put = requests.put('{}/api/1.0/app/{}'.format(config['appstore']['url'], appId), headers=headers,
                                        data=json.dumps(payload))
            if __handle_error(response_put) == 0:
                print("App has been updated in Appstore")
                return appId
        # app don't exists yet, create app and return app ID
        else:
            response_post = requests.post('{}/api/1.0/app'.format(config['appstore']['url']), headers=headers,
                                          data=json.dumps(payload))
            if __handle_error(response_post) == 0:
                print("App has been created in Appstore")
                content_post = response_post.content
                content_post = json.loads(content_post.decode("utf-8"))
                return content_post.get('id')
            # The category of the app has changed. Update the app category as post leads to an error
            # TODO extends appstore with rest api for getting app based on name
            # elif(response_post.status_code == 409):
    return -1


def get_or_create_app_category(config_file, keycloak_token):
    with open(config_file, mode='r') as __config_file:
        y = yaml.safe_load(__config_file)
        config = json.dumps(y)
        config = json.loads(config)
    category = config['appstore']['category']
    headers = {
        'Authorization': keycloak_token,
        'Content-Type': 'application/json'
    }
    params = {
        'name': category
    }
    
    response_get = requests.get('{}/api/1.0/appcategory'.format(config['appstore']['url']), headers=headers,
                                params=params)
    # App category already exists, return the ID
    if __handle_error(response_get) == 0:
        print("App-category already exists in Appstore")
        content_get = response_get.content
        content_get = json.loads(content_get.decode("utf-8"))
        return content_get.get('id')
    # App category don't exists yet, create new one with given name and return ID
    else:
        data = '{\"name\" : "" }'
        data = json.loads(data)
        data['name'] = category
        response_create = requests.post('{}/api/1.0/appcategory'.format(config['appstore']['url']), headers=headers,
                                        data=json.dumps(data))
        if __handle_error(response_create) == 0:
            print("Created new App-category in Appstore")
            content_create = response_create.content
            content_create = json.loads(content_create.decode("utf-8"))
            return content_create.get('id')
        else:
            print("Error when creating new App-category in Appstore")
            return -1


# Get the access token from keycloak for authenticating against the appstore
def get_token_from_keycloak(config_file):
    with open(config_file, mode='r') as __config_file:
        y = yaml.safe_load(__config_file)
        config = json.dumps(y)
        config = json.loads(config)

    headers = {
        'Content-Type': 'application/x-www-form-urlencoded'
    }

    payload = {
        'client_id': config['keycloak']['client'],
        'username': config['appstore']['username'],
        'password': config['appstore']['password'],
        'grant_type': 'password',
        'client_secret': config['keycloak']['client-secret']
    }
    
    response_post = requests.post(config['keycloak']['url'], headers=headers, data=payload)
    if __handle_error(response_post) == 0:
        
        content = response_post.content
        content = json.loads(content.decode("utf-8"))
        return content.get('access_token')
    else:
        print("Access token cannot be obtained")

# main
if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='Publishes your docker app to kuksa Appstore')
    parser.add_argument('config_file', help="YAML config file")
    args = parser.parse_args()
    keycloak_token = get_token_from_keycloak(args.config_file)
    keycloak_token = "bearer " + keycloak_token
    if keycloak_token:
        app_category_id = get_or_create_app_category(args.config_file, keycloak_token)
        print("App Category with ID", app_category_id)
        if (app_category_id != -1):
            app_id = get_or_create_app(args.config_file, app_category_id, keycloak_token)
            print("App with ID", app_id)
            if (app_id != -1):
                upload_artifacts(args.config_file, app_id, keycloak_token)