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
    main_path = os.path.join(src_path, 'main')
    res_path = os.path.join(main_path, 'resources')
    stacks_path = os.path.join(res_path, 'stacks')
    
    org_stacks_file = os.path.join(res_path, 'stacks.json')
    out_stacks_file = os.path.join(res_path, 'stacks.json')
    
    with open(org_stacks_file) as f:
        stacks_data = json.load(f)
        
        for filename in os.listdir(stacks_path):
            full_path = os.path.join(stacks_path, filename)
            
            with open(full_path) as f2:            
                stacks_data.append(json.load(f2))
        
        
        
        with open(out_stacks_file, 'w') as outfile:
            json.dump(stacks_data, outfile, indent=4)