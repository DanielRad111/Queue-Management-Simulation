package logic.simulator;

import gui.InputForm;
import logic.generator.Generator;
import logic.logger.Logger;
import logic.scheduler.Scheduler;
import model.Client;
import model.Server;

import java.util.*;
import java.util.stream.Collectors;

public class Simulator implements Runnable {

    private static int currentTime;
    private static int totalServiceTime;
    private static int totalWaitingTime;

    public static int getCurrentTime() {
        return currentTime;
    }

    private Generator generator;
    private Scheduler scheduler;
    private Logger logger;
    private InputForm inputForm;


    public Simulator(Generator generator, Scheduler scheduler, String logFilePath) {
        this.generator = generator;
        this.scheduler = scheduler;
        this.logger = new Logger(logFilePath);
        inputForm = new InputForm();
    }

    @Override
    public void run() {
        currentTime = 0;
        totalWaitingTime = 0;
        int maxClientsInQueue = 0;
        totalServiceTime = 0;
        int peakHour = 0;
        List<Client> clients = generator.generateRandomClients(generator.getNumberOfClients());
        clients = clients.stream()
                .sorted(Comparator.comparingInt(Client::getArrivalTime))
                .collect(Collectors.toList());
        for (Client client : clients) {
            totalServiceTime += client.getServiceTime();
        }
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
            inputForm.appendLog("Time " + currentTime);
            inputForm.appendLog("Waiting clients: " + clients + "\n");
            logger.logEvent("Time " + currentTime + "\n");
            logger.logEvent("Waiting clients: " + clients + "\n");
            boolean serversEmpty = true;
            for (int i = 0; i < scheduler.getServers().size(); i++) {
                logger.logEvent("Queue " + (i + 1) + ": ");
                Server server = scheduler.getServers().get(i);
                int size = 0;
                size += scheduler.getServers().get(i).getClients().length;
                totalWaitingTime += size;
                int clientsNr = server.getClients().length;
                if (clientsNr > 0) {
                    logger.logEvent(Arrays.toString(server.getClients()) + " waiting time: " + server.getWaitingTime() + "\n");
                    serversEmpty = false;
                } else {
                    logger.logEvent("closed\n");
                }
                if (clientsNr > maxClientsInQueue) {
                    maxClientsInQueue = clientsNr;
                    peakHour = currentTime;
                }
                inputForm.appendLog("Queue " + (i + 1) + ": ");
                if (clientsNr > 0) {
                    inputForm.appendLog(Arrays.toString(server.getClients()) + " waiting time: " + server.getWaitingTime() + "\n");
                } else {
                    inputForm.appendLog("closed\n");
                }
            }
            logger.logEvent("\n");
            if (serversEmpty && clients.isEmpty()) {
                break;
            }
            currentTime++;
        }

        double averageWaitingTime = (double) totalWaitingTime / generator.getNumberOfClients();
        double averageServiceTime = (double) totalServiceTime / generator.getNumberOfClients();

        inputForm.setAvgServiceTime(averageServiceTime);
        inputForm.setAvgWaitingTime(averageWaitingTime);
        inputForm.setPeakHour(peakHour);

        logger.logEvent("Total waiting time: " + totalWaitingTime + "\n");
        logger.logEvent("Total service time: " + totalServiceTime + "\n");
        logger.logEvent("Average waiting time: " + averageWaitingTime + "\n");
        logger.logEvent("Average service time: " + averageServiceTime + "\n");
        logger.logEvent("Peak hour: " + peakHour + "\n");
        logger.logEvent("Maximum clients in queues: " + maxClientsInQueue + "\n");

        logger.close();
    }
}