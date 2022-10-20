mvn clean;
mvn package;
java -cp target/server-jar-with-dependencies.jar server.Server 127.0.0.1 2005
