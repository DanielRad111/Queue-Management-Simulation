package logic.simulator;

import logic.generator.Generator;
import logic.logger.Logger;
import logic.scheduler.Scheduler;
import model.Client;
import model.Server;

import java.util.*;

public class Simulator implements Runnable {

    private static int currentTime;

    public static int getCurrentTime() {
        return currentTime;
    }

    private Generator generator;
    private Scheduler scheduler;
    private Logger logger;


    public Simulator(Generator generator, Scheduler scheduler, String logFilePath) {
        this.generator = generator;
        this.scheduler = scheduler;
        this.logger = new Logger(logFilePath);
    }

    @Override
    public void run() {
        currentTime = 0;
        List<Client> clients = generator.generateRandomClients(generator.getNumberOfClients());
        while (currentTime < generator.getMaxSimulationTime()) {
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

            logger.logEvent("Time " + currentTime + "\n");
            logger.logEvent("Waiting clients: " + clients + "\n");
            for (int i = 0; i < scheduler.getServers().size(); i++) {
                logger.logEvent("Queue " + (i + 1) + ": ");
                Server server = scheduler.getServers().get(i);
                if (server.getClients().length > 0) {
                    logger.logEvent(Arrays.toString(server.getClients()) + " waiting time: " + server.getWaitingTime() + "\n");
                } else {
                    logger.logEvent("closed\n");
                }
            }
            logger.logEvent("\n");


            currentTime++;
        }
        logger.close();
    }
}
