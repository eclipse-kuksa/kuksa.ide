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
    docker run -ti -v /tmp:/home/user/.m2 -v `pwd`:/home/user/che-build -v `pwd`:/projects eclipse/che-dev:6.10.0 sh -c "mvn clean install"


Deploying the Assembly
++++++++++++++++++++++

.. code-block:: bash

    cd <kuksa-ide-root-path>/assembly/assembly-main/target/eclipse-che-<version>/eclipse-che-<version>
    docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v `pwd`:/assembly -v /tmp:/data -e CHE_PREDEFINED_STACKS_RELOAD__ON__START=true eclipse/che start


