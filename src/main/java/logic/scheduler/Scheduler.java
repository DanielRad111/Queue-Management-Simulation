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

    public Scheduler(int maxNoServers, int maxClientsPerServer){
        this.servers = new ArrayList<>();
        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server(maxClientsPerServer);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
        //default
        this.strategy = new TimeStrategy();
    }

    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }

    public void dispatchClient(Client client){
        strategy.addClient(servers, client);
    }

    public List<Server> getServers(){
        return servers;
    }
}
