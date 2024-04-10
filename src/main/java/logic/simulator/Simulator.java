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

    private static int currentTime;

    public static int getCurrentTime() {
        return currentTime;
    }

    private Generator generator;
    private Scheduler scheduler;

    public Simulator(Generator generator, Scheduler scheduler) {
        currentTime = 0;
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
        currentTime = 0;
        List<Client> clients = generator.generateRandomClients(generator.getNumberOfClients());
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
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Time " + currentTime);
            System.out.println("Waiting clients: " + clients);
            System.out.println("Servers: " + scheduler.getServers());

            currentTime++;
        }
    }
}
