package logic.scheduler.strategy;

import model.Client;
import model.Server;

import java.util.List;

public class ShortestQueueStrategy implements Strategy {

    @Override
    synchronized public void addClient(List<Server> servers, Client client) {
        Server server = servers.getFirst();
        int index = servers.indexOf(server);
        for (Server s : servers) {
            if (s.getClients().length < server.getClients().length) {
                server = s;
                index = servers.indexOf(s);
            }
        }
        servers.get(index).addClient(client);
    }
}
