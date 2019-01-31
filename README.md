<!--
******************************************************************************
Copyright (c) 2018 Dortmund University of Applied Sciences and Arts

All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v2.0
which accompanies this distribution, and is available at
https://www.eclipse.org/org/documents/epl-2.0/index.php

Contributors:
    Robert Hoettger - initial readme files added
*****************************************************************************
-->

# Kuksa-IDE Repo

This repository contains documentation and implementation to setup an Eclipse Che Kuksa instance, that incorporates an Automotive Grade Linux (AGL) workspace with Yocto support.
AGL represents an automotive specific Linux distribution specifically designed as open software stack for connected car scenarios.
The following video features an example on how to use the Kuksa-IDE for developing AGL applications and services running on a [Rover](https://wiki.eclipse.org/APP4MC/Rover)

[![IMAGE ALT TEXT HERE](https://img.youtube.com/vi/lK8z8SSrpuY/0.jpg)](https://www.youtube.com/watch?v=lK8z8SSrpuY)

## Documentation Build

The Kuksa-IDE documentation is available [here](https://kuksa-che-ide.readthedocs.io/en/latest/).

[![Documentation Status](https://readthedocs.org/projects/kuksa-che-ide/badge/?version=latest)](https://kuksa-che-ide.readthedocs.io/en/latest/?badge=latest)


# Model-driven Development of AGL Applications and Services

The Eclipse Che Kuksa instance simplifies the development of AGL applications and services.
AGL features the usage of automotive applications based on HTML5, JavaScript, and C/C++, which run on top of AGL.
While applications realize a distinct use case, services offers functionality to all applications.
For more information please refer to the [AGL documentation](http://docs.automotivelinux.org/docs/apis_services/en/dev/)

The following sections demonstrate the development of AGL applications/services in a model-driven way based on the tool RAML2AGL (cf. Section [Raml2AGL](#raml2agl) ) as well as the building and deployment of AGL applications/services to a remote device running AGL (cf. Section [Building & Deploying](building-and-deploying)).

## Prerequiste

* Eclipse Che Kuksa instance including the AGL stack

## Setting up Eclipse Che KUKSA

The following steps are necessary if you want to build and deploy AGL applications & services within Che:

* Create a new workspace with Automotive Grade Linux (AGL) as selected stack
* Go to "Profile" &rightarrow; "Preferences" &rightarrow; "Remote Targets" and add a new Remote Target with the device IP and the according User, e.g. "root". Then select the Target.
* Go to Profile &rightarrow; Preferences &rightarrow; Yocto Settings and add a new SDK with a Name, e.g. "agl-rover", a Version, e.g. "1.0.0" and "https://owncloud.idial.institute/s/Mm7tPJJ337QJpbR/download" as Donwload Link. Then select the added Yocto SDK.
* To avoid connection trouble, open the Terminal and ssh into the appropriate Device:
`
ssh <User>@<IP>
`

## RAML2AGL

As AGL do not foster any development methodology explicitly, the tool
[Raml2AGL](https://github.com/pjcuadra/raml2agl) has been developed to simplify and abstract the development of AGL applications/service via a model-driven approach.
Based on a RESTfull API (RAML) model, all communication components for both the AGL service and the AGL application are automatically generated.
In this way, developers can focus on implementing functionality rather than bothering with creating the communication interfaces.
This section demonstrate the usage of RAML2AGL within Eclipse Che to generate all communication components for C/C++ applications and services running on top of AGL.
However, RAML2AGL can be also used locally as described [here](https://github.com/pjcuadra/raml2agl)

### Installing RAML2AGL in Che

RAML2AGL requires [Jinja2](http://jinja.pocoo.org/docs/2.10/) and [PyYAML](https://pyyaml.org/).
To install this dependencies, the python package management system [pip](https://pypi.org/project/pip/) has to be first installed.
Therefore, go to the Terminal and follow the instructions:

```
sudo apt-get install software-properties-common
sudo apt-add-repository universe
sudo apt-get update

sudo apt-get install aptitude
sudo aptitude install python3-setuptools
sudo easy_install3 pip
```
Based on pip, the dependencies can then be installed:

```
sudo pip3 install jinja2
sudo pip3 install pyyaml
```

After the necessary dependencies have been installed, RAML2AGL can be installed from the github repository:  

```
sudo pip install git+https://github.com/pjcuadra/raml2agl
```

### Using RAML2AGL in Che

[RAML](https://github.com/raml-org/raml-spec) is basically a human-readable language based on [YAML](http://yaml.org/) to describe practically RESTful APIs.
RAML models contains all information necessary for generating the required client/server source code.
More precisely, a RAML model consists of basic information such as a title, description, version, Uris, supported protocols, declared data types, or security scheme as well as a detailed description of the required interfaces, i.e. HTTP GET, POST, PUT, DELETE, PATCH, HEAD, and OPTIONS including optional properties.
The following RAML model for a temperature sensor gives you an impression regarding the syntax:

```
#%RAML 1.0
title: Rover DHT22
mediaType: application/json
version: v1
baseUri: localhost:8000/api?token=x
types:
  sensor_id:
    enum:
      - front
      - rear
/read_temperature:
  description: "Read Temperature"
  get:
    responses:
      200:
        body:
          properties:
            temperature:
              type: number
/read_humidity:
  description: "Read Humidity"
  get:
    responses:
      200:
        body:
          properties:
            humidity:
              type: number
```

For more information on the RAML syntax please refer to the [specification](https://github.com/raml-org/raml-spec/blob/master/versions/raml-10/raml-10.md/).

As described [here](https://github.com/pjcuadra/raml2agl), the general syntax for using RAML2AGL is as follows:

```
raml2agl -i <model>.raml [-o <output_dir>] [-s <output_sources_dir>] [-h <output_headers_dir>] [-v] --app|--service
```

### Example Application and Service
This section features an example application as well as service to demonstrate the usage of RAML2AGL within Eclipse Che Kuksa.  


## Building and Deploying

tbd
