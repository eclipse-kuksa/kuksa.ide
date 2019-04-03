Quick Start
-----------

Kuksa IDE is built as a full custom Eclipse Che Assembly.
Therefore, it includes all assembly components specified and described
in `Eclipse Che Assembly
<https://www.eclipse.org/che/docs/assemblies.html>`_ are included
into Kuksa IDE's build system.

Build the Assembly
++++++++++++++++++

Build the assembly by running;

.. code-block:: bash

    cd <kuksa-ide-root-path>
    docker run -ti -v ~/.m2:/home/user/.m2 -v `pwd`:/home/user/che-build -v `pwd`:/projects eclipse/che-dev:6.10.0 sh -c "mvn clean install"


Deploying the Assembly
++++++++++++++++++++++



.. code-block:: bash

    cd <kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>

Running as Single User:

.. code-block:: bash

    docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/assembly -v /tmp:/data -e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true eclipse/che:6.10.0 start

Running as Multi User:

.. code-block:: bash

    docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/assembly -v /tmp:/data -e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true -e CHE_MULTIUSER=true eclipse/che:6.10.0 start

Custom Stacks
#############

In case a custom stack, such as the AGL stack, is not included within the Che instance for some reasons, one can use swagger to add it.
Therefore, open *YOUR_IP*:8080/swagger/#!/stack/createStack , click on stack -> stack post, add the according json (e.g. https://github.com/eclipse/kuksa.ide/blob/master/stacks/src/main/resources/stacks/agl.json), and post it via the "Try it out!" button.
