package logic.scheduler;

import logic.scheduler.strategy.SelectionPolicy;
import logic.scheduler.strategy.ShortestQueueStrategy;
import logic.scheduler.strategy.Strategy;
import logic.scheduler.strategy.TimeStrategy;
import model.Client;
import model.Server;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private int maxClientsPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxClientsPerServer) {
        if (maxNoServers < 1 || maxClientsPerServer < 1) {
            System.err.println("The scheduler can't have a negative number of servers or clients!");
            return;
        }
        this.maxNoServers = maxNoServers;
        this.maxClientsPerServer = maxClientsPerServer;
        this.strategy = new TimeStrategy();//default strategy
    }

    public void serversInitializer() {
        this.servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(maxNoServers);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new TimeStrategy();
        }
    }

    synchronized public void dispatchClient(Client client) {
        List<Server> availableServers = new ArrayList<>();
        try {
            servers.forEach((currentServer) -> {
                if (currentServer.getClients().length < this.maxClientsPerServer) {
                    availableServers.add(currentServer);
                }
            });
            strategy.addClient(availableServers, client);
        } catch (ClassCastException exception) {
            System.err.println(exception.getMessage());
        }
    }

    public List<Server> getServers() {
        return servers;
    }
}
