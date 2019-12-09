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


import * as path from 'path';
import * as vscode from 'vscode';
import { Uri } from 'vscode';



export async function yamlGeneratorForm(context: vscode.ExtensionContext) {

	const panel = vscode.window.createWebviewPanel(
		'kuksConfig',
		'Kuksa Configuration',
		vscode.ViewColumn.One,
		{
			enableScripts: true
		}
	);
	panel.webview.html = getWebviewContent();
	panel.webview.onDidReceiveMessage(
		message => {
			var configArray = [];
			switch (message.command) {
				case 'saveConfig':
					
				let kuksaConfig = {
					general:{
						name: message.appName,
						img: message.imgName,
						version: message.appVersion,
						owner: message.appOwner,
						description: message.appDescription
					},
					appstore:{						
						url: message.appStoreUrl,
						category:  message.appStoreCategory,
						auth:  message.appStoreAuth,
						username: message.appStoreUser,
						password: message.appStorePassword
					},
					keycloak:{
						url: message.keycloakUrl,
						client: message.keycloakClient,
						"client-secret": message.keycloakClientSecret
					},
					docker:{
						config:{
							privileged: 'true'
						}
					}
				};

				var fs = require('fs');
				var os = require('os');
				

				let data = JSON.stringify(kuksaConfig,null,2);
				fs.writeFileSync('kuksa.json', data);
				vscode.window.showInformationMessage("Select Project Folder");

				const projectSelection: vscode.OpenDialogOptions = {
					canSelectMany: false,
					canSelectFiles: false,
					canSelectFolders: true,
					openLabel: 'Select',
					filters: {'All files': ['*']
				}
	};
				var projectFilePath:string;
				vscode.window.showOpenDialog(projectSelection).then(fileUri => {
					if (fileUri && fileUri[0]) {
						const projectFilePath = fileUri[0].fsPath;
						const execLocation = context.asAbsolutePath("./kuksa/yamlgen.py");
						const {PythonShell} = require('python-shell');
						let options = {
							args:[projectFilePath]
						};
						PythonShell.run(execLocation, options, function (err: any, res: any[]) {
							if (err) { throw err; }
							else{
								let yamlUri = Uri.file(projectFilePath+'/kuksa.yaml');
                                vscode.commands.executeCommand('vscode.openFolder',yamlUri);  
							}
  								console.log(res[0]); 
							}); 
					}
				});


					return;
			}
		},
		undefined,
	);
}




function getWebviewContent() {
	return `
	<!DOCTYPE html>
  <html lang="en">

    <head>
		  <meta charset="UTF-8">
		  <meta name="viewport" content="width=device-width, initial-scale=1.0">
		  <title>Kuksa Configuration</title>
		  <style>
			  .tabbed figure {
				  display: block;
				  margin-left: 0;
				  border-bottom: 1px solid silver;
				  clear: both;
			  }
			  
			  .tabbed > input,
			  .tabbed figure > div {
				  display: none;
			  }
			  
			  .tabbed figure>div {
				  padding: 20px;
				  width: 600px;
				  border: 1px solid silver;
				  background: #fff;
				  line-height: 1.5em;
				  letter-spacing: 0.3px;
				  color: #444;
			  }
			  
			  #tab1:checked ~ figure .tab1,
			  #tab2:checked ~ figure .tab2,
	          #tab3:checked ~ figure .tab3{
				  display: block;
			  }
			  
			  nav label {
				  float: left;
				  padding: 15px 15px;
				  border-top: 1px solid silver;
				  border-right: 1px solid silver;
				  background: #1E907C;
				  color: #3D3C3C;
				  font-weight: bold;
			  }
			  
			  nav label:nth-child(1) {
				  border-left: 1px solid silver;
			  }
			  
			  nav label:hover {
				  background: hsl(210, 50%, 40%);
			  }
			  
			  nav label:active {
				  background: #ffffff;
			  }
			  
			  #tab1:checked ~ nav label[for="tab1"],
			  #tab2:checked ~ nav label[for="tab2"],
			  #tab3:checked ~ nav label[for="tab3"]{
				  background: white;
				  color: #111;
				  position: relative;
				  border-bottom: none;
			  }
			  
			  #tab1:checked ~ nav label[for="tab1"]:after,
			  #tab2:checked ~ nav label[for="tab2"]:after,
			  #tab3:checked ~ nav label[for="tab3"]:after{
				  content: "";
				  display: block;
				  position: absolute;
				  height: 2px;
				  width: 100%;
				  background: white;
				  left: 0;
				  bottom: -1px;
			  }
			  
			  form {
				  padding: 1em;
				  border: 1px solid #CCC;
				  border-radius: 1em;
			  }
			  
			  form div + div {
				  margin-top: 1em;
			  }
			  
			  label {
				  display: inline-block;
				  width: 90px;
				  text-align: right;
			  }
			  
			  input,
			  textarea {
				  font: 1em sans-serif;
				  width: 300px;
				  box-sizing: border-box;
				  border: 1px solid #999;
			  }
			  
			  input:focus,
			  textarea:focus {
				  border-color: #000;
			  }
			  
			  textarea {
				  vertical-align: top;
				  height: 5em;
			  }
			  
			  .button {
				  background-color: #1E907C; /* Green */
				  border: none;
				  color: white;
				  padding: 10px 10px;
				  margin: 5px 5px;
				  text-align: center;
				  text-decoration: none;
				  display: inline-block;
				  font-size: 16px;
				  width: 100px;
			  }

		  </style>
	  </head>

	  <body>
		  <div class="tabbed">
			  <input checked="checked" id="tab1" type="radio" name="tabs" />
			  <input id="tab2" type="radio" name="tabs" />
			  <input id="tab3" type="radio" name="tabs" />
			  <nav>
				  <label for="tab1">General</label>
				  <label for="tab2">App Store</label>
		          <label for="tab3">Keycloak</label>
			  </nav>
			  <figure>

				  <div class="tab1">
					  <form action="" method="post">
						  <div>
							  <label for="name">Name:</label>
							  <input type="text" id="name" name="app_name" placeholder="Application Name">
						  </div>
              <div>
							  <label for="image">Image:</label>
							  <input type="text" id="image" name="image" placeholder="Image Name(arch/project:version)">
						  </div>
						  <div>
							  <label for="version">Version:</label>
							  <input type="text" id="version" name="version" placeholder="Application Version">
						  </div>
						  <div>
							  <label for="owner">Owner:</label>
							  <input type="text" id="owner" name="owner" placeholder="Company/Individual">
						  </div>
						  <div>
							  <label for="description">Description:</label>
							  <input type="text" id="description" name="version" placeholder="A Short Description">
						  </div>
					  </form>
				  </div>
		
				  <div class="tab2">
					  <form action="" method="post">
						  <div>
							  <label for="appstore-url">URL:</label>
							  <input type="url" id="appstore-url" name="appstore-url">
						  </div>
						  <div>
							  <label for="category">Category:</label>
							  <input type="text" id="category" name="category" placeholder="App Category">
						  </div>
			<div>
							  <label for="user">User Name:</label>
							  <input type="text" id="user" name="user" value="Admin">
						  </div>
			<div>
							<label for="password">Password:</label>
							  <input type="text" id="password" name="password" value="Admin">
						  </div>
					  </form>
				  </div>
		
		
		<div class="tab3">
					  <form action="" method="post">
						  <div>
							  <label for="keycloak-url">URL:</label>
							  <input type="url" id="keycloak-url" name="keycloak-url">
						  </div>
			<div>
							  <label for="client">Client:</label>
							  <input type="text" id="client" name="client" value="demo">
						  </div>
			<div>
							<label for="client-secret">Client Secret:</label>
							  <input type="text" id="client-secret" name="client-secret">
						  </div>
					  </form>
			  </div>
			  
				 
			  <input type="button" class="button" id="bt" value="Save" onclick="saveFile(true)" />



			  <script>
					  function saveFile(buttonPressed) {

						  const vscode = acquireVsCodeApi();
						  const appName = document.getElementById('name');
                          const imgName = document.getElementById('image');
						  const appVersion = document.getElementById('version');
						  const appOwner = document.getElementById('owner');
						  const appDescription = document.getElementById('description');
						  const appStoreUrl = document.getElementById('appstore-url');
						  const appStoreCategory = document.getElementById('category');
						  const appStoreUser = document.getElementById('user');
						  const appStorePassword = document.getElementById('password');
						  const keycloakUrl = document.getElementById('keycloak-url');
						  const keycloakClient = document.getElementById('client');
						  const keycloakClientSecret = document.getElementById('client-secret');
						 


						  if (buttonPressed && appVersion.value != "") {
							  vscode.postMessage({
								  command: 'saveConfig',
								  appName: appName.value,
                                  imgName: imgName.value,
								  appVersion: appVersion.value,
								  appOwner: appOwner.value,
								  appDescription: appDescription.value,
								  appStoreUrl: appStoreUrl.value,
								  appStoreCategory: appStoreCategory.value,
								  appStoreUser: appStoreUser.value,
								  appStorePassword: appStorePassword.value,
								  keycloakUrl: keycloakUrl.value,
								  keycloakClient:keycloakClient.value,
								  keycloakClientSecret:keycloakClientSecret.value
								  
							  })
						  }
					  }
			  </script>
		  </body>
	  </html>
  `;
}
