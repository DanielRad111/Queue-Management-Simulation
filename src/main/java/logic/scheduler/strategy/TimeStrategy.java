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
        int minWaitingTime = servers.getFirst().getWaitingTime();
        Server selectedServer = servers.getFirst();
        for (Server server : servers) {
            if (server.getWaitingTime() < minWaitingTime) {
                minWaitingTime = server.getWaitingTime();
                selectedServer = server;
            }
        }
        selectedServer.addClient(client);
    }
}
