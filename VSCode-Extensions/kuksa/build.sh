#!/bin/sh
#--------------------------------------------------------------------------------------------
# Copyright (c) 2019 Eclipse KUKSA project
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Public License 2.0 which is available at
# http://www.eclipse.org/legal/epl-2.0
#
# SPDX-License-Identifier: EPL-2.0
#
# Contributors: Dortmund University of Applied Sciences and Arts
#--------------------------------------------------------------------------------------------

EXPERIMENTAL=`docker version | grep Experimental | tail -n 1 | xargs`
if [ "$EXPERIMENTAL" != "Experimental: true" ]; then
    echo "Please enable the docker engine's experimental features" 
    exit 1
fi

ARCH=$1
PLATFORM="linux/$1"
PROJECTNAME="kuksa"

TAG=$2
cp /usr/bin/qemu-arm-static ./qemu-arm-static

if [ "$?" != "0" ]; then
    echo "Please install the qemu-user-static package" 
    exit 1
fi



docker build --platform $PLATFORM -f $3 -t ${ARCH}/${PROJECTNAME}:${TAG} .
