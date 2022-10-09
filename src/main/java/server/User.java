package server;

import remote.iClient;

/**
 * Store user information on server, it only needs to know user name and corresponding interface.
 */
public class User {
    public String name;
    public iClient client;
    public User (String name, iClient client) {
        this.name = name;
        this.client = client;
    }
}
