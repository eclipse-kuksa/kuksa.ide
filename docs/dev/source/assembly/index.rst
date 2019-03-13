Kuksa IDE Custom Assembly
-------------------------

Kuksa IDE is built as a full custom Eclipse Che Assembly.
Therefore, it includes all assembly components specified and described
in `Eclipse Che Assembly
<https://www.eclipse.org/che/docs/assemblies.html>`_ are included
into Kuksa IDE's build system.

Build the Assembly
++++++++++++++++++

Eclipse Che provides different ways to build a custom assembly and
can be seen at `Building Che
<https://www.eclipse.org/che/docs/build-reqs.html>`_. In this section,
two of this procedures are going to be explained.

Build Using Eclipse Che's Docker Image
**************************************

According to Eclipse Che's documentation one can use :code:`eclipse/che-dev`
docker image to build a custom Eclipse Che Assembly. This can be achieved by
running;

.. code-block:: bash

    cd <kuksa-ide-root-path>
    docker run -ti -v ~/.m2:/home/user/.m2 -v `pwd`:/home/user/che-build -v `pwd`:/projects eclipse/che-dev:6.10.0 sh -c "mvn clean install"


Eclipse Che's developers recommend mounting Maven repo (-v ~/.m2:/home/user/.m2)
to persist dependencies and make subsequent builds faster.


.. note::

    Building using Eclipse Che's Docker image is the prefereable way if
    you want to test all the components of the Assembly. For instance,
    stacks can't be verified when building it inside Eclipse Che itself.
    Nevertheless, to make use of the built artifacts you first have to
    deploy it following :ref:`deploy_full_assembly`.


.. _verify_assembly:

Verify your build's correctness
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

You can check if your assembly was correctly build by checking the content of
:code:`<kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>`.
For instance, you can check the that AGL sample projects have been added by running;

.. code-block:: bash

    cd <kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>
    cat templates/samples.json


And the output should contain an entry similar to the following;

.. code::

    {
        "category": "Samples",
        "commands": [],
        "displayName": "agl-helloworld-service",
        "name": "agl-helloworld-service",
        "links": [],
        "tags": [
            "agl",
            "gcc",
            "cpp"
        ],
        "mixins": [],
        "modules": [],
        "source": {
            "type": "git",
            "location": "https://github.com/iotbzh/helloworld-service.git",
            "parameters": {}
        },
        "path": "/helloworld-service",
        "attributes": {},
        "problems": [],
        "projectType": "c",
        "description": "A binding example for AGL"
    }

Similarly, to verify that the stacks have been added run;

.. code-block:: bash

    export TEMP_DIR=`mktemp -d`

    cd <kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>

    # Copy to temp dir
    cp tomcat/webapps/api.war ${TEMP_DIR}

    # Change to temp dir
    pushd ${TEMP_DIR}
    jar xf api.war

    tree | grep stacks

And the output shouldn't contain :code:`che-core-ide-stacks-<version>.jar`. Instead, it should look like;

.. code-block:: bash

   │   ├── kuksa-stacks-<version>.jar


For verifying other included components please review Eclipse Che's documentation
to see how your component is packaged into the assembly.


Troubleshooting
~~~~~~~~~~~~~~~

The docker image building process can fail because the user ID (uid) of the
user issuing the :code:`docker run` command doesn't correspond the uid of
the user ":code:`user`" inside the docker. To avoid this add the following flag to the
:code:`docker run` command.

.. code-block:: bash

    --user `id -u ${USER}`


Build Using Running Eclipse Che
*******************************

To build our Eclipse Che Assembly you can follow the steps in the article
`Build Che in Che
<https://www.eclipse.org/che/docs/che-in-che-quickstart.html>`_.

.. note::

    Building Eclipse Che Assembly using the procedure don't allow the user
    to have a look at anything that is outside the workspace. However, it's
    easier to start testing and even debugging your IDE extensions.


.. _deploy_full_assembly:

Deploying the Assembly within Eclipse Che's Docker
++++++++++++++++++++++++++++++++++++++++++++++++++

.. code-block:: bash
    cd <kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>

Running as Single User:

.. code-block:: bash
    docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/assembly -v /tmp:/data -e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true eclipse/che start

Running as Multi User:

.. code-block:: bash
    docker run -it -e CHE_MULTIUSER=true --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/assembly -v /tmp:/data -e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true eclipse/che start

.. warning::

    The previous command includes optional arguments denoted by :code:`[arg]`. If you want
    to keep any (or all) remove the brackets :code:`[]`.

If you want to run the Kuksa IDE in multi user mode add :code:`-e CHE_MULTIUSER=true`. Similarly,
if from the previous run you added news custom stacks to the assembly you'll need to add
:code:`-e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true`.
