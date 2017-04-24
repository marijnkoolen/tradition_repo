# Stemmarest
### a graph-based data storage solution for Stemmaweb
---
Stemmarest is a java application designed to improve the performance of [stemmaweb](http://stemmaweb.net/stemmaweb/) by using the graph-database [Neo4j](http://neo4j.com/).

### Downloading
---


git clone https://github.com/tohotforice/PSE2_DH.git

This application requires graphviz and its dot parser. To install on e.g. Ubuntu, type:

```
sudo apt install graphviz
```

or apt-get for pre-16.04 versions of Ubuntu:



### Building
---
- Stemmarest needs to be built using [Maven](http://maven.apache.org/run-maven/index.html#Quick_Start). This can be done using a java IDE (e.g Eclipse) and a Maven plugin

Make sure that the library `com.alexmerz.graphviz.jar` is either part of the war file or added to the e.g. tomcat `lib/` folder.

Install the graphviz jar to your local repository:
```
mvn install:install-file -Dfile=stemmarest/lib/com.alexmerz.graphviz.jar -DgroupId=com.alexmerz -DartifactId=graphviz -Dversion=0.8.6 -Dpackaging=jar -DlocalRepository=<path-to-local-repo>
```

### Running 
---
Make sure to set the location for the neo4j database under `DATABASE_HOME` in your environment variables, or update the default location in ApplicationConfig.java.

As this application represents a server side only, there is no full GUI included


It is possible though to test it by using the test interface testGui.html which is located at StemmaClient

>#### Using the test interface
>1. Create a user and give it an id (this is necessary as every graph needs to be owned by a user)
>2. Import an GraphML file using the id of the user you have created. The generated id of the tradition will be returned
>3. Use the custom request by typing in the API call you want (all calls are listed in the documentation)

**A word about node id's:** when a graph is being imported each node gets from Neo4j a unique id-number. In order to use an id in an API call (e.g. reading-id) it is necessary to explicitly get it from the data base. This can be done by using the _getAllReadings_ method (*getallreadings/fromtradition/{traditionId}*) or by actually going into the data base (see _Neo4j visualization_ in section Database)


### Deploying with Maven on Apache Tomcat

The WAR file is bigger than the default war size limit of 50Mb of Tomcat. You can increase the size limit in `conf/tomcat-users.xml` in the Tomcat directory.

Use `mvn tomcat7:deploy` to deploy to your tomcat server. In case you already have a version deployed, use redeploy: `mvn tomcat7:redeploy`

### Database
---
- The application's database is located in the database folder in stemmarest. It can be reset anytime by simply deleting this folder

- To view the data base it is necessary to install [Neo4j](http://neo4j.com/download/) and start it on the database location

- [Neo4j visualization](http://neo4j.com/developer/guide-data-visualization/) information



