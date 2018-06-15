Stacks
------

Eclipse Che doesn't provide a standard mechanism to add custom
stacks during build time. Therefore, Kuksa IDE provides an easy
and straight forward mechanism to append custom stacks to 
the ones provided by Eclipse Che during build time.

Adding New Stacks
+++++++++++++++++

To add a new stack simply write its JSON. And add it to the 
repository at :code:`stacks/src/main/resources/stacks`.
During build time Maven will unpack :code:`che-core-ide-stacks-<version>`
and append any JSON content from :code:`stacks/src/main/resources/stacks/*`
to it's main :code:`stacks.json`.

For instance, by the time of writing two stacks were already added. These are
shown below.

.. code-block:: bash

    $ tree stacks/src/main/resources/stacks
    stacks/src/main/resources/stacks
       ├── agl.json
       └── yocto.json


Debugging
+++++++++

For verifying that your stack is being added to the main :code:`stacks.json` 
you can first check :code:`stacks/target/classes/stacks.json`. 

.. note::

    :code:`stacks/target/classes/stacks.json` is created during the
    building of :code:`kuksa-stacks` Maven module.
    
You can also just build :code:`kuksa-stacks` Maven module instead of the 
entire assembly for quicker debuggin by running;

.. code-block:: bash

    mvn clean install -rf :kuksa-stacks
    
You can also check if your stack was in fact added to the assembly by 
checking if :code:`kuksa-stacks-<version>.jar` was included into the
:code:`api.war` as explained in :ref:`verify_assembly`.

Additionally, you can extract :code:`kuksa-stacks-<version>.jar`'s
content to verify it your stack is in :code:`stacks.json` by running;

.. code-block:: bash

    pushd ${TEMP_DIR}
    jar xf WEB-INF/lib/kuksa-stacks-<version>.jar
    cat stacks.json

.. note::

    ${TEMP_DIR} is obtaine by following the steps from 
    :ref:`verify_assembly`
    