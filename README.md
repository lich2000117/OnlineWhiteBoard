# OnlineWhiteBoard

## RMI usage:

Remote: object/method repository 


### Server:

Server: naming server, link object/method in Remote, give it a unique name, say "calculator" for BOTH client and server

User: whiteboard's way to store user information for future operations for every user.

WhiteBoardRMI: method repository, it is registred in naming server, includes all methods need to be called from client, for example, "add me to user list", "broadcast messages" 

### Client:
Client: a client trying to use Remote method to execute something. and register ClientRMI to naming server.

ClientRMI: Client side method repository to store all methods needed to be called from server. It is regiesterd to naming server.
