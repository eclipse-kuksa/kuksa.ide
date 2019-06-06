# Copyright (c) 2018
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#
# Contributors:
#   Pedro Cuadra - pjcuadra@gmail.com
#

import json
import os


if __name__ == '__main__':

    src_path = '../'
    target_path = '../../target/classes'
    main_path = os.path.join(src_path, 'main')
    res_path = os.path.join(main_path, 'resources')
    samples_path = os.path.join(res_path, 'samples')
    
    org_samples_file = os.path.join(res_path, 'samples.json')
    out_samples_file = os.path.join(target_path, 'samples.json')
    
    with open(org_samples_file) as f:
        samples_data = json.load(f)
        
        for filename in os.listdir(samples_path):
            full_path = os.path.join(samples_path, filename)
            
            with open(full_path) as f2:            
                samples_data.append(json.load(f2))
        
        
        
        with open(out_samples_file, 'w') as outfile:
            json.dump(samples_data, outfile, indent=4)
