package logic.simulator;

import gui.InputForm;
import gui.ProgressBars;
import logic.generator.Generator;
import logic.logger.Logger;
import logic.scheduler.Scheduler;
import logic.scheduler.strategy.SelectionPolicy;
import model.Client;
import model.Server;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

public class Simulator implements Runnable {
    private static int currentTime;
    private static int totalServiceTime;
    private static int totalWaitingTime;
    private JProgressBar[] progressBars;

    public static int getCurrentTime() {
        return currentTime;
    }

    private final Generator generator;
    private final Scheduler scheduler;
    private final Logger logger;
    private final InputForm inputForm;


    public Simulator(Generator generator, Scheduler scheduler, String logFilePath, InputForm form) {
        this.generator = generator;
        this.scheduler = scheduler;
        this.logger = new Logger(logFilePath);
        this.inputForm = form;
        progressBars = new JProgressBar[generator.getNumberOfQueues()];
        for (int i = 0; i < generator.getNumberOfQueues(); i++) {
            progressBars[i] = new JProgressBar();
            progressBars[i].setStringPainted(true);
        }
        ProgressBars progressBar = new ProgressBars(progressBars);
    }

    @Override
    public void run() {
        currentTime = 1;
        totalWaitingTime = 0;
        int maxClientsInQueue = 0;
        totalServiceTime = 0;
        int peakHour = 1;
        List<Client> clients = generator.generateRandomClients(generator.getNumberOfClients());
        clients = clients.stream()
                .sorted(Comparator.comparingInt(Client::getArrivalTime))
                .collect(Collectors.toList());
        for (Client client : clients) {
            totalServiceTime += client.getServiceTime();
        }
        while (currentTime <= generator.getMaxSimulationTime()) {
            Iterator<Client> iterator = clients.iterator();
            while (iterator.hasNext()) {
                Client client = iterator.next();
                if (client.getArrivalTime() == currentTime) {
                    scheduler.dispatchClient(client);
                    iterator.remove();
                }
            }
            try {
                Thread.sleep(1000L);
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

                for (int j = 0; j < generator.getNumberOfQueues(); j++) {
                    if (scheduler.getServers().get(j).getClients().length == 0) {
                        progressBars[j].setValue(0);
                    } else {
                        progressBars[j].setValue((int) ((double) scheduler.getServers().get(j).getClients().length / scheduler.getMaxClientsPerServer() * 100));
                    }
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

    public static void main(String[] args) {
        InputForm form = new InputForm();
    }
}