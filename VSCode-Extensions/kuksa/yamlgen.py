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


import yaml
import os
import sys
import json


f = open('kuksa.json', 'r')
jsonData = json.load(f)
f.close()

with open(str(sys.argv[1])+'/kuksa.yaml', 'w') as f:
    data = yaml.safe_dump(jsonData,f,default_flow_style=False)