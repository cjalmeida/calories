Calories App
============

Note: this is a test project I used to learn Angular2. At the time it was still in beta so don't consider this a model for current Angular2 dev practices.

Overview
--------
This system has the goal of providing a simple Single-Page-Application where users may log meals and calories.

The system consists of two modules:

 - `api`: A backend REST server.
 - `web`: The web interface for users and administrators.

Stack
-----
The API server is built using Java 8, Spring Framework 4 and Gradle 2.14 as the build tool. Additionally, I used Spring Data REST on top of Spring MVC to provide a HATEOAS capable REST layer. To simplify development, the system uses the H2 embedded database, however we're using JPA to make the server database agnostic.

The front-end was developed using Typescript and Angular 2.0.0-RC2. Because the Angular 2 router component is not yet stable, I opted to use the external '@ngrx/router'. For a better look-and-feel, I used the SB-Admin template, which is based on Bootstrap and uses SASS for styling. Webpack is used as the build tool for the front-end.


Dependencies
------------

The software was developed and fully tested on Ubuntu 14.04 LTS system. However, it has no OS specific dependencies and should work on Mac and Windows.

### Java 8

The `api` modules depends on a recent Oracle Java 8 installation. Use your system installer or download from the [Oracle website](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### NodeJS 6

The `web` module depends on NodeJS v6. We recommend using the [Node Version Manager](https://github.com/creationix/nvm). If you're on Windows, [use this version](https://github.com/coreybutler/nvm-windows)

### Libraries

All library dependencies are fully managed via Gradle and NPM. We provide a Gradle wrapper to automatically download a suitable Gradle version on first run. The NPM tool is part of the NodeJS distribution.

Running Automated Tests
-----------------------

We provide tests for the REST back-end. Check the files under `api/src/test/groovy`.

To execute the tests, just run the command below in the root of the `api` folder:

```
> cd api
> ./gradlew test
```

After run, test reports are available under `build/reports/tests/index.html`. Coverage report can be generated with the command below and will be available at `build/reports/jacoco/test/html/index.html`

```
> cd api
> ./gradlew jacocoTestReport
```

Then reports will be available under `api/build/reports/jacoco/test/html/index.html`


Running the application (development)
-------------------------------------

Run the server by issuing the commands below. The first time will download all dependencies and may take a long time. The default configuration will also load the demo data automatically.

```
> cd api
> ./gradlew bootRun
```

On another terminal window, use NPM to download the dependencies and start the application.

```
> cd web
> npm install
> npm start
```

After initialization, the web interface should be available at `http://localhost:8080/`. You may login with the credentials below for each role:

* User `admin@example.com`, password `admin`: administrator role,
* User `manager@example.com`, password `dummy`: user manager role,
* User `dummy1@example.com`, password `dummy`: regular user (with some data).

After login, a JWT token is stored and will persist the session for 10 days. You may logout by clicking on the user name on the top-right corner of the dashboard. You may click the "Signup" button to sign-up as new regular user.

To "reset" the database, just remove the `api/db` directory. It will be recreated with demo data the next time the server is started.

You can change back-end configuration in the `api/config/application.properties` file. The front-end configuration is located in `web/src/config.ts`.

