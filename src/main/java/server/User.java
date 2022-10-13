package server;

import remote.iClient;

/**
 * Store user information on server, it only needs to know user name and corresponding interface.
 *
 * User joined the room is always at status WAITING, only approved by MANAGER, it changes to USER.
 */
public class User {
    //public STATUS status = STATUS.WAITING;
    public UserSTATUS status = UserSTATUS.USER; // FOR DEBUG PURPOSE ^^
    public String name;
    public iClient client;

    public User (String name, iClient client) {
        this.name = name;
        this.client = client;
    }

    // only use this constructor for manager.
    public User (String name, iClient client, UserSTATUS s) {
        this.name = name;
        this.client = client;
        this.status = s;
    }

}
