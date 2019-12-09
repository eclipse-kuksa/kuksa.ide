<!--
******************************************************************************
Copyright (c) 2019 Dortmund University of Applied Sciences and Arts

All rights reserved. This program and the accompanying materials
are made available under the terms of the Eclipse Public License v2.0
which accompanies this distribution, and is available at
https://www.eclipse.org/org/documents/epl-2.0/index.php

Contributors:
    Robert Hoettger - initial readme files added
    Philipp Heisig  - documentation added
*****************************************************************************
-->

# Kuksa-IDE Repo

This repository contains documentation and implementation to setup an Eclipse Che (6.10) Kuksa instance under `/che-6/`, which eases the development of Kuksa In-Vehicle applications as well as Kuksa Cloud services.
Therefore, the default Eclipse Che version is extended by different Kuksa-specific components:

*  Automotive Grade Linux (AGL) stack with Yocto support:
AGL represents an automotive specific Linux distribution specifically designed as open software stack for connected car scenarios.
The following video features an example on how to use the Kuksa-IDE for developing AGL applications and services running on a [Rover](https://wiki.eclipse.org/APP4MC/Rover)

Additionally, under `/VSCode-Extensions/`, 4 VSCode extensions are provided which ease Eclipse Kuksa In-Vehicle App and Cloud service development activities. 
Such extensions can be used (a) locally as part of a VSCode instance, or (b) along with Eclipse Che 7 and Theia or (c) Gitpod together with kubernetes and docker (with root privileges) in order to employ a cloud-based IDE.
Hence, the set of Visual Studio Code extensions establish an end-to-end workflow from the IDE via the Appstore and Hawkbit to the device. 
The extensions can be installed from .vsix files and provide:

* *Initialize Workspace*
* *Generate Docker Image*
* *Configure Kuksa Environment(YAML)*
* *Publish to Appstore*

For more information, please see [here](https://github.com/eclipse/kuksa.ide/blob/master/VSCode-Extensions/README.md).
