# Kuksa-IDE

The following documentation describes the Kuksa IDE, which is basically an extension of Eclipse Che to establish an end-to-end workflow in the Eclipse Kuksa ecosystem from the IDE via the Appstore and Hawkbit to the device.

## Scope

As the In-Vehicle gateway can be run on different operating systems (Raspbian, AGL, Debian etc.) or hardware architectures (arm64 etc.), In-Vehicle applications will be provided in a generic way via docker images. 
Therefore, a build script allows to generate target-specific docker images.
Based on a generated .yaml config file and a Python-based script, the resulting docker images can then be published to the app store and hawkbit respectively.
This process is depicted in the following image:

![](https://github.com/eclipse/kuksa.ide/blob/master/VSCode-Extensions/images/kuksa_ide.png)

## Architecture

The default IDE in Eclipse Che is Che-Theia [1]. 
Theia extensions is the original way to extend Theia. 
They are build-time extensions but they give you access to everything.

## User Guide

This section describes how to use the Kuksa IDE extension for publishing In-vehicle applications to the Kuksa appstore.

### Prerequisites

#### Docker

1. Follow instructions in https://docs.docker.com/v17.09/engine/installation/.
2. Make sure docker can be run as a non-root user. https://docs.docker.com/v17.09/engine/installation/linux/linux-postinstall/#manage-docker-as-a-non-root-user
3. Enable docker experimental feature 

Create a daemon.json file in /etc/docker/ and add 

```sh
{
  "experimental" : true
}
```

Restart Docker daemon 
```sh
$ sudo systemctl restart docker
```

#### QEMU user-static package
```sh
$ sudo apt-get install qemu-user-static
```

#### NodeJS and NPM
1. Install NodeJS
```sh
$ sudo apt install nodejs
```
2. Install NPM
```sh
sudo apt install npm
```
##### Python (atleast v3.6+)
1. Install from https://www.python.org/downloads/release/python-360/
2. Install PyYaml `python3 -m pip install pyyaml`
## Developer Guide

This section depict how to generate the vsix file based on source code.
In this way, changes to the IDE can be be done.

### Generate .vsix file

1. Clone the Kuksa IDE repository into your VSCode workspace
2. Open a new terminal (Menu bar / Ctrl+Shift+Ö / cmd+shift+P on MAC) in Visual Studio Code
3. Install prerequisite: 
   * "npm install"
   * "npm install vsce" 
   * Install .vsix extensions from https://marketplace.visualstudio.com/items?itemName=fabiospampinato.vscode-install-vsix
4. Run "vsce package". A "xxx.vsix" file should be generated ( in-case of any errors delete the node_modules directory and install vsce again)
5. Run "Extensions:Install from VSIX" and select the accoring .vsix file
6. Reload VSCode

### Development Flow

1. Press `F5` from the Editor.
2. An Extension Development Window will open which enables to test Kuksa Extensions in real time.

### Using Kuksa IDE

Open the command palette (View -> Command Palette / Ctrl+Shift+P) and search for "kuksa"

#### Kuksa: Initialize Workspace

1. Provide folder/workspace name
2. Project tree will be generated as follows:

![](https://github.com/eclipse/kuksa.ide/blob/master/VSCode-Extensions/images/tree.png)

*The project folder will be created inside the folder VSCode was open (by default the $HOME directory)*

#### Kuksa: Generate Docker Image

1. Open a Kuksa project in VSCode. 
2. Check if the directory *src* has the source code and *docker* has Dockerfile.
3. Run command and provide the archictecture and the tags.
4. Check (`<sudo> docker images`) if the docker image is created or not.

#### Kuksa: Configure Kuksa Environment(YAML)

1. Open a Kuksa project in VSCode. 
2. Run command and provide the details:

![](https://github.com/eclipse/kuksa.ide/blob/master/VSCode-Extensions/images/yaml_form.png)


3. Check if kuksa.yaml file is populated.


#### Kuksa: Publish to AppStore

1. Open a Kuksa project in VSCode.
2. Run command
3. At the end of the command, two new files should be created: `docker-image.tar.bz2` and `docker-container.json`.
#### Note

In OSX, if you are running into `bash: code: command not found` please open the Command Palette via (⇧⌘P) and type `shell command` and `Install 'code' command in PATH** command.`

## References

[1] https://www.eclipse.org/che/extend/

## Current Status

1. Generate file system
2. Generate YAML config file through form
3. Generate Docker image
4. Push docker image (.tar) through app-publisher
