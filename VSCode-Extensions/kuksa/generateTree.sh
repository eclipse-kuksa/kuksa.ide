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


mkdir $1
cd $1
mkdir docker include src
touch kuksa.yaml
cp $2 ./docker
sed -i "s/kuksa/$3/g" ./docker/build.sh


