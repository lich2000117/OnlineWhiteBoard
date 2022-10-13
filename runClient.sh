mvn clean;
mvn package;
java -cp target/client-jar-with-dependencies.jar client.Client 127.0.0.1 2005
