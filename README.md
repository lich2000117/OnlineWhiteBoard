# OnlineWhiteBoard

## How to Run:
**On local Computer:**
```
bash ./runServer.sh   # whiteboard
bash ./runClient.sh   # users
```

_**On Remote Distributed Computers:**_
- WhiteBoard Side:
  - Specify WhiteBoard Server public IP and Port that is **visible to clients** to set up stub.
  - for example, whiteboard's public IP is 192.168.0.1 and public port 2005
```
java -cp target/server-jar-with-dependencies.jar server.Server 192.168.0.1 2005   
```

- Client Side:
  - Specify WhiteBoard Server public IP and Port that we need to connect.
  - Specify Client public IP and Port that is **visible to whiteboard**.
  - for example, whiteboard's public IP is 192.168.0.1 and public port 2005 for us to connect.
  - for example, client's public IP is 192.168.0.4 and public port 1999.
```
java -cp target/client-jar-with-dependencies.jar client.Client 192.168.0.1 2005 192.168.0.4 1999
```
## RMI usage:

Each Client specify a client Port, used for export client RMI methods.

Whiteboard Server specify a server Port, used for export server RMI methods.

Client needs server IP and port to connect to server.

Server needs client port to connect to client (use getClientHost() to get client IP as well.)



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

