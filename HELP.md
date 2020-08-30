# Getting Started with Healthcare Micro Service

### Building Project
* Github: https://github.com/chiangmaithai/healthcare_add_enrollee.git
* Project build using maven: Run: mvn clean install
* Starting app: Run: java -jar add_new_enrollee_demo-0.0.1-SNAPSHOT.jar

### Database Server
* Database: hsqldb database server:
* version: hsqldb-2.5.1 (http://hsqldb.org/)
* Starting: Run: java -classpath lib/hsqldb.jar org.hsqldb.server.Server
* hsqldb server properties file details below:
* filename: server.properties 
* location: hsqldb-2.5.1\hsqldb
* server.database.0=file:db0/db0
* server.dbname.0=healthcare
* server.port=8701

### Eureka Server
* Github: https://github.com/chiangmaithai/healthcare.git
* https://github.com/chiangmaithai/healthcare.git
* Project build using maven
* Build: mvn clean install
* Start application: Run:  java -jar service_registry_demo-0.0.1-SNAPSHOT.jar

### Testing
* Setup json post request to: http://localhost:8000/process-enrollee