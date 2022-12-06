Cake Manager Micro Service (fictitious)
=======================================

A summer intern started on this project but never managed to get it finished.
The developer assured us that some of the above is complete, but at the moment accessing the /cakes endpoint
returns a 404, so getting this working should be the first priority.

Requirements:
* By accessing /cakes, it should be possible to list the cakes currently in the system. JSON would be an acceptable response format.

* It must be possible to add a new cake.

* It must be possible to update an existing cake.

* It must be possible to delete an existing cake.

Comments:
* We feel like the software stack used by the original developer is quite outdated, it would be good to migrate the entire application to something more modern. If you wish to update the repo in this manner, feel free! An explanation of the benefits of doing so (and any downsides) can be discussed at interview.

* Any other changes to improve the repo are appreciated (implementation of design patterns, seperation of concerns, ensuring the API follows REST principles etc)

Bonus points:
* Add some suitable tests (unit/integration...)
* Add some Authentication / Authorisation to the API
* Continuous Integration via any cloud CI system
* Containerisation

Scope
* Only the API and related code is in scope. No GUI of any kind is required


Original Project Info
=====================

To run a server locally execute the following command:

`mvn jetty:run`

and access the following URL:

`http://localhost:8282/`

Feel free to change how the project is run, but clear instructions must be given in README
You can use any IDE you like, so long as the project can build and run with Maven or Gradle.

The project loads some pre-defined data in to an in-memory database, which is acceptable for this exercise.  There is
no need to create persistent storage.


Submission
==========

Please provide your version of this project as a git repository (e.g. Github, BitBucket, etc).

A fork of this repo, or a Pull Request would be suitable.

Good luck!

Changes 
==========
* Backed part was moved from servlets to Spring Boot Rest project
* Initial data load was moved from dynamic URL pulling to loading from resources file
* Autogenerated REST documentation is available in Swagger and OpenAPI 3.0 format
* Project is dockerised in order to run without extra steps 


### How to build
#### How to build locally
Open project in IDE of choice as Java Maven project, execute maven `package` goal. It will run unit tests and export jar file.

Alternatively with maven installed,go to project folder and execute `mvn package` command

#### How to build Docker image
in project root execute follow command, it will create docker image named "cake-manager" in local repository 
```
docker build -t cake-manager .  
```

### How to run locally
#### prerequisites:
* JRE installed, version 17+. Can check with running `java --version`
* Maven installed, version 3.8.6+. Can check with running `mvn --version`

#### running from command line
Follow command can be triggered to start web app explicitly
```
mvn spring-boot:run
```

#### running from IDE
Execute method main in `com.waracle.cakemgr.CakeManagerApplication` class, which would start the local server, available 

#### running from Docker
execute follow command to start container exposing port to 8282 
```
docker run -p 8282:8282 cake-manager
```

### How to test if app is working

Post startup health endpoint should return valid response using this URL:
http://localhost:8282/actuator/health

Also, documentation in Swagger format is available via  http://localhost:8282/swagger-ui/index.html link, it also could be used to trigger API endpoints

