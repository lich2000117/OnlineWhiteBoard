# OnlineWhiteBoard

## How to Run:



## RMI usage:
```
Run Server: java -cp target/server-jar-with-dependencies.jar server.Server
Run Client: java -cp target/client-jar-with-dependencies.jar client.Client
```
OR
```
bash ./runServer.sh
bash ./runClient.sh
```
OR
```
Specify Port 2000 and Server Address 127.0.0.1 :
Run Server: java -cp target/server-jar-with-dependencies.jar server.Server 2000
Run Client: java -cp target/client-jar-with-dependencies.jar client.Client 127.0.0.1 2000
```



```Want to send a chat:
    Client: call {local method} request_sendChat(){Whiteboard as w, w.brodcast_Chat() (RMI)}
    ----(which call a iServer RMI method)>>>> 
    Whiteboard: call {local method} brodcast_Chat(){ for c:Client, c.DrawChat() (RMI)}
    
    By doing so, we use Client to call broadcast messages method defined in whiteboard, and whiteboard can 
    call clients method to draw chat on their client side.
    
    -- Key: 
    Need to store Whiteboard.RMI address in ever Client.
    A single WhiteBoard instance need an ArrayList of Client.RMI addresses.
```

### Remote: 
object/method repository 


### Server:

Server: naming server, link object/method in Remote, give it a unique name, say "calculator" for BOTH client and server

User: whiteboard's way to store user information for future operations for every user.

WhiteBoardRMI: method repository, it is registred in naming server, includes all methods need to be called from client, for example, "add me to user list", "broadcast messages" 

### Client:
Client: a client trying to use Remote method to execute something. and register ClientRMI to naming server.

ClientRMI: Client side method repository to store all methods needed to be called from server. It is regiesterd to naming server.


## How to Run in terminal:

#### Server:
`
mvn clean;
mvn package;
java -cp target/server-jar-with-dependencies.jar server.Server
`


#### Client
`
mvn clean;
mvn package;
java -cp target/client-jar-with-dependencies.jar client.Client
`

## Assumptions:
- Every user/client has unique names, and their RMI addresses are of "rmi://rmiServerIP:ServerPort/username"
- so when removing user, only has their username is sufficient.

