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


import { window, commands, ExtensionContext } from 'vscode';
import { appGenerator } from './appGenerator';
import { appPublisher } from './appPublisher';
import { kuksaTreeGenerator } from './kuksaTreeGenerator';
import { yamlGeneratorForm } from './yamlGeneratorForm';



export function activate(context: ExtensionContext) {

	context.subscriptions.push(commands.registerCommand('kuksa.init', _ => {
		kuksaTreeGenerator(context)
			.catch(console.error);
	}));
	
	context.subscriptions.push(commands.registerCommand('kuksa.generateDockerFile', _ => {
		appGenerator(context)
			.catch(console.error);
	}));

	context.subscriptions.push(commands.registerCommand('kuksa.config', _ => {

		yamlGeneratorForm(context)
			.catch(console.error);
	}));

	context.subscriptions.push(commands.registerCommand('kuksa.publishApp', _ => {
		appPublisher(context)
			.catch(console.error);
	}));
}
