package logic.scheduler.strategy;

import logic.simulator.Simulator;
import model.Client;
import model.Server;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TimeStrategy implements Strategy{

    @Override
    public void addClient(List<Server> servers, Client client) {
        Server server  = servers.getFirst();
        int index = servers.indexOf(server);
        for (Server s : servers) {
            if(s.getWaitingTime() < server.getWaitingTime()) {
                server = s;
                index = servers.indexOf(s);
            }
        }
        servers.get(index).addClient(client);
    }
}