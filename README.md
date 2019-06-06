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

This repository contains documentation and implementation to setup an Eclipse Che Kuksa instance, which ease the development of Kuksa In-Vehicle applications as well as Kuksa Cloud services.
Therefore, the default Eclipse Che version has been extended by different Kuksa-specific components:

*  Automotive Grade Linux (AGL) stack with Yocto support:
AGL represents an automotive specific Linux distribution specifically designed as open software stack for connected car scenarios.
The following video features an example on how to use the Kuksa-IDE for developing AGL applications and services running on a [Rover](https://wiki.eclipse.org/APP4MC/Rover)

* App Store integration:
A Che Kuksa Plugin allows to establish an end-to-end workflow from the IDE via the Appstore and Hawkbit to the device. _tbd_

## Version

The current Eclipse Che Kuksa instance relies on Che 6.10 (cf. [che-6](https://github.com/eclipse/kuksa.ide/tree/master/che-6)).
However, there a plans to port the Kuksa-specific extensions, i.e. the developed [plugins](https://github.com/eclipse/kuksa.ide/tree/master/plugins), to Che 7.0
