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


import { ExtensionContext, window, OpenDialogOptions, QuickPickItem, QuickInputButton, QuickInput, Disposable, QuickInputButtons, TerminalOptions } from "vscode";
import { MultiStepInput } from "./mutiStepInput";

export async function appPublisher(context: ExtensionContext) {


    interface State {
        title: string;
        step: number;
        totalSteps: number;
        configFilePath: string;
    }

    async function collectInputs() {
        const state = {} as Partial<State>;
        await MultiStepInput.run(input => selectConfigFile(state));
        return state as State;
    }

    const title = 'App Publisher';


    async function selectConfigFile(state: Partial<State>) {
        window.showInformationMessage("Select Configuration(.yaml) File ");
        const options: OpenDialogOptions = {
            canSelectMany: false,
            canSelectFiles: true,
            canSelectFolders: false,
            openLabel: 'Open',
            filters: {
                'YAML files': ['yaml'],
                'All files': ['*']
            }
        };

        await window.showOpenDialog(options).then(fileUri => {
            if (fileUri && fileUri[0]) {
                state.configFilePath = fileUri[0].fsPath;
                console.log(state.configFilePath);
            }
        });

        return (input: MultiStepInput) => publishApp(state);

    }
    async function publishApp(state: Partial<State>) { 
        window.showInformationMessage("Check progress in kuksaPublishTerminal");      
        const execLocation = context.asAbsolutePath("./kuksa/kuksa-publisher.py");
        const options: TerminalOptions = {
			hideFromUser: false,
			name: 'kuksaPublishTerminal'
		};
		let kuksaPublishTerminal = window.createTerminal(options);
		kuksaPublishTerminal.show(false);
        kuksaPublishTerminal.sendText('python3 ' + execLocation + ' ' + state.configFilePath );
    }


    function shouldResume() {
        return new Promise<boolean>((resolve, reject) => {

        });
    }

    async function validateNameIsUnique(name: string) {
        await new Promise(resolve => setTimeout(resolve, 1000));
        return name === 'vscode' ? 'Name not unique' : undefined;
    }


    const state = await collectInputs();
}


