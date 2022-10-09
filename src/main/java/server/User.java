package server;

import remote.iClient;

/**
 * Store user information on server, it only needs to know user name and corresponding interface.
 *
 * User joined the room is always at status WAITING, only approved by MANAGER, it changes to USER.
 */
public class User {
    enum STATUS {
        WAITING,
        MANAGER,
        USER,
    }
    //public STATUS status = STATUS.WAITING;
    public STATUS status = STATUS.USER; // FOR DEBUG PURPOSE ^^
    public String name;
    public iClient client;

    public User (String name, iClient client) {
        this.name = name;
        this.client = client;
    }

    // only use this constructor for manager.
    public User (String name, iClient client, STATUS s) {
        this.name = name;
        this.client = client;
        this.status = s;
    }

    public void changeStatue(STATUS s){
        this.status = s;
    }
}
