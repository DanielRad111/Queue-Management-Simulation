package logic.simulator;

import logic.generator.Generator;
import logic.scheduler.Scheduler;
import logic.scheduler.strategy.TimeStrategy;
import model.Client;
import model.Server;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Simulator implements Runnable{

    private Generator generator;
    private Scheduler scheduler;

    public Simulator(Generator generator, Scheduler scheduler) {
        this.generator = generator;
        this.scheduler = scheduler;
    }


    private boolean hasClients() {
        for (Server server : scheduler.getServers()) {
            if (server.getClients().length != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        int currentTime = 0;
        List<Client> clients = generator.generateRandomClients(generator.getNumberOfClients());
        generator.initializeServers();
        while(currentTime < generator.getMaxSimulationTime()){
            Iterator<Client> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                if (client.getArrivalTime() == currentTime) {
                    scheduler.dispatchClient(client);
                    iterator.remove();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Time " + currentTime);
            System.out.println("Waiting clients: " + clients);
            System.out.println("Servers: " + generator.getServers());

            currentTime++;
        }
    }
}
