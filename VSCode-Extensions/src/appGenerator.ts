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

import { QuickPickItem, window, Disposable, CancellationToken, QuickInputButton, QuickInput, ExtensionContext, QuickInputButtons, Uri, OpenDialogOptions, TerminalOptions } from 'vscode';
import { MultiStepInput } from "./mutiStepInput";

export async function appGenerator(context: ExtensionContext) {



	const architectureList: QuickPickItem[] = ['amd64', 'arm64', 'arm32v6']
		.map(label => ({ label }));


	interface State {
		title: string;
		step: number;
		totalSteps: number;
		architecture: string;
		tag: string;
		dockerpath: string;
	}

	async function collectInputs() {
		const state = {} as Partial<State>;
		await MultiStepInput.run(input => pickArchitecture(input, state));
		return state as State;
	}

	const title = 'Generate App';

	async function pickArchitecture(input: MultiStepInput, state: Partial<State>) {
		const pickArch = await input.showQuickPick({
			title,
			step: 1,
			totalSteps: 3,
			placeholder: 'Select Architecture',
			items: architectureList,
			shouldResume: shouldResume
		});
		state.architecture = pickArch.label;
		return (input: MultiStepInput) => inputTag(input, state);
	}



	async function inputTag(input: MultiStepInput, state: Partial<State>) {
		state.tag = await input.showInputBox({
			title,
			step: 2,
			totalSteps: 3,
			value: state.tag || '',
			prompt: 'Provide a tag',
			validate: validateNameIsUnique,
			shouldResume: shouldResume
		});
		return (input: MultiStepInput) => selectDockerImage(state);
	}

	async function selectDockerImage(state: Partial<State>) {
		window.showInformationMessage("Select Dockerfile");
		const options: OpenDialogOptions = {
			canSelectMany: false,
			canSelectFiles: true,
			canSelectFolders: false,
			openLabel: 'Select',
			filters: {
				'All files': ['*']
			}
		};

		await window.showOpenDialog(options).then(fileUri => {
			if (fileUri && fileUri[0]) {
				state.dockerpath = fileUri[0].fsPath;
			}
		});

		return (input: MultiStepInput) => generateImage(state);

	}

	async function generateImage(state: Partial<State>) {
		window.showInformationMessage("Check kuksaTerminal for build progress");
		const arch = state.architecture;
		const tag = state.tag;
		const dockerpath = state.dockerpath;
		const options: TerminalOptions = {
			hideFromUser: false,
			name: 'kuksaTerminal'
		};
		let kuksaTerminal = window.createTerminal(options);
		kuksaTerminal.show(false);
		kuksaTerminal.sendText('sh docker/build.sh '+arch+' '+tag+' '+dockerpath);
	}


	function shouldResume() {
		return new Promise<boolean>((resolve, reject) => {

		});
	}

	async function validateNameIsUnique(name: string) {
		// ...validate...
		await new Promise(resolve => setTimeout(resolve, 1000));
		return name === 'vscode' ? 'Name not unique' : undefined;
	}


	const state = await collectInputs();
}
