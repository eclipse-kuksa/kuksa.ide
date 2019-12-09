/*---------------------------------------------------------------------------------------------
* Copyright (c) 2019 Eclipse KUKSA project
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License 2.0 which is available at
* http://www.eclipse.org/legal/epl-2.0
*
* SPDX-License-Identifier: EPL-2.0
*
* Contributors: Dortmund University of Applied Sciences and Arts
*--------------------------------------------------------------------------------------------*/

import { ExtensionContext, Uri, window, workspace } from 'vscode';
import * as path from 'path';
import * as vscode from 'vscode';

export async function kuksaTreeGenerator(context: ExtensionContext) {
    const projectName = await window.showInputBox({
        placeHolder: 'Enter Project Name'
    });
    if (typeof projectName !== "undefined") {
        var os = require('os');
       
        if(os.type()=="Linux" || os.type()=="Darwin"){
            let folderPath:string = vscode.workspace.rootPath || ''; // get the open folder path
            const projectLocation = path.join(folderPath, projectName);
            const buildLocation = context.asAbsolutePath("./kuksa/build.sh");
            const execLocation = context.asAbsolutePath("./kuksa/generateTree.sh");
             var exec = require('child_process').exec;
             var cmdToLaunch = "sh "+execLocation+" "+projectLocation+" "+buildLocation+" "+projectName;
             exec(cmdToLaunch,function(error: { code: any; }, stdout: any, stderr: any){
                if (!error) {
                    window.showInformationMessage("Generating Project Tree");
                    //let newProjectUri = Uri.file(projectLocation);
                    //vscode.commands.executeCommand('vscode.openFolder',newProjectUri);      
                  }
                else{
                    console.log(error);
                }
                });
                
        let newProjectUri = Uri.file(projectLocation);
        vscode.commands.executeCommand('vscode.openFolder',newProjectUri); 
        }

        else {
            window.showInformationMessage("Not Linux or Mac");
        }
       
    }
    else {
        window.showErrorMessage("Invalid project name");
    }
}
