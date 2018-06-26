Sample Projects
---------------

Eclipse Che doesn't provide a standard mechanism to add custom
sample projects during build time. Therefore, Kuksa IDE provides 
an easy and straight forward mechanism to append them to ones 
provided by Eclipse Che during build time.

Adding New Sample Projects
++++++++++++++++++++++++++

To add a new sample projects simply write its JSON. And add it to the 
repository at :code:`samples/src/main/resources/samples`.
During build time Maven will unpack :code:`che-core-ide-templates-<version>`
and append any JSON content from :code:`samples/src/main/resources/samples/*`
to it's main :code:`samples.json`.

For instance, by the time of writing a sample project was already added. This is
shown below.

.. code-block:: bash

    $ tree samples/src/main/resources/samples
    samples/src/main/resources/samples
       └── helloworld_service.json


Debugging
+++++++++

For verifying that your sample project is being added to the main 
:code:`samples.json` you can first check 
:code:`samples/target/classes/samples.json`. 

.. note::

    :code:`samples/target/classes/samples.json` is created during the
    building of :code:`kuksa-samples` Maven module.
    
You can also just build :code:`kuksa-samples` Maven module instead of the 
entire assembly for quicker debuggin by running;

.. code-block:: bash

    mvn clean install -rf :kuksa-samples
    
You can also check if your sample project was in fact added to the assembly 
by following the steps explained in :ref:`verify_assembly`.
    