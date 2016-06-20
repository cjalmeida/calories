DBAK - The Distributed LAN Backup Tool
======================================

Note: Design document available at `doc/design/ApplicationDesign.docx`

Overview
--------
This system has the goal of providing a distributed LAN backup tool with central administration. The clients
are Windows machines that run a backup operation based on a server-stored configuration. The administration
is done through a Web interface.

The system consists of three main modules:

 - `api`: A REST server hosting machine configuration.
 - `client`: A desktop Java application to be installed on client machines.
 - `web`: The web admin interface.

The API server and client application are built using Java 8, Spring Framework 4 and Gradle 2.13 as 
the build tool.


Dependencies
------------

### Java 8

Both the `client` and `api` modules depends on a recent Oracle Java 8 installation. Use your 
system installer or download from the 
[Oracle website](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### NodeJS 5

The `web` module depends on NodeJS v5. We recommend using the 
[Node Version Manager](https://github.com/creationix/nvm). 
If you're on Windows, [use this version](https://github.com/coreybutler/nvm-windows)  

### Libraries

All library dependencies are fully managed via Gradle, NPM. We provide a Gradle wrapper to automatically
download a suitable Gradle version on first run. The NPM tools is part of the NodeJS distribution.

Running Automated Tests
-----------------------

We provide tests for the `api` and `client` modules (check the design doc on why `web` has no tests).
Just run the command below in the root of the project:

```
> ./gradlew test
```

After run, test reports are available under `<module>/build/reports/tests/index.html` where `<module>` is
either `api` or `client`. Coverage report can be generated with the command:

```
> ./gradlew jacocoTestReport
```

Then reports will be available under `<module>/build/reports/jacoco/test/html/index.html`


Running (test/development)
--------------------------

If running for the first time, install the NodeJS dependencies for the `web` module.
```
> cd web
> npm install
> cd ..
```

Load the test data if running for the first time or to restore the DB to the initial test state
```
> ./gradlew api:loadTestData
```

Deploy web assets to the server and start it.
```
> ./gradlew webDeploy api:bootRun
```

After initialization, the web admin interface should be available at `http://localhost:9000/`.
You may login with `admin/admin` as administrator, or `machine1/testpass` as a machine user.

To demo the client application, first create a machine configuration in the web admin interface
with values suited for your environment. Then create a distribution ZIP file and copy it over 
to the test machine.
```
> ./gradle client:distZip
```

The ZIP file will be named `client/build/distributions/dbak-client.zip`. Unzip it anywhere and edit the 
`config/application.properties` file to match the configuration you just created. Open a `cmd.exe` window
and run:
```
> cd path\to\dbak-client
> bin\client.bat
```

The application log should connect and show the next fire time for the scheduled triggers.
