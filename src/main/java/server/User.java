package server;

import remote.iClient;

/**
 * Store user information on server, it only needs to know user name and corresponding interface.
 */
public class User {
    private String name;
    private iClient client;
    public User (String name, iClient client) {
        this.name = name;
        this.client = client;
    }
}
