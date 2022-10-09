package server;

import remote.iFunction;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Calculator extends UnicastRemoteObject implements iFunction {

    protected Calculator() throws RemoteException {
        super();
    }

    @Override
    public String calculate(String message) throws RemoteException {
        System.out.println("Received!");
        System.out.println(message);
        return "Comeback to Client";
    }
}
