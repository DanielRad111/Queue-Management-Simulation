package logic.simulator;

import gui.InputForm;
import gui.ProgressBars;
import logic.generator.Generator;
import logic.logger.Logger;
import logic.scheduler.Scheduler;
import model.Client;
import model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Simulator implements Runnable {
    public int numberOfClients;
    public int numberOfServers;
    public int simulationTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int minServiceTime;
    public int maxServiceTime;
    private int maxClientsPerServer;
    private String logFilePath = "D:\\CODING\\Proiecte InteliJ\\TP\\pt2024_30223_daniel_rad_assignment_2\\src\\main\\java\\logic\\logger\\log";

    public static int currentTime;
    private static int totalServiceTime;
    private static int totalWaitingTime;

    private JProgressBar[] progressBars;
    private List<Client> generatedClients;

    private final Generator generator;
    private final Scheduler scheduler;
    private final Logger logger;
    private final InputForm inputForm;


    public Simulator(InputForm form) {
        this.inputForm = form;
        this.numberOfClients = form.getNumberOfClients();
        this.numberOfServers = form.getNumberOfServers();
        this.simulationTime = form.getTimeLimit();
        this.minArrivalTime = form.getMinArrivalTime();
        this.maxArrivalTime = form.getMaxArrivalTime();
        this.minServiceTime = form.getMinServiceTime();
        this.maxServiceTime = form.getMaxServiceTime();
        this.maxClientsPerServer = form.getMaxClientsPerServer();
        this.generator = new Generator(new Random(), numberOfClients, numberOfServers, simulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        this.scheduler = new Scheduler(numberOfServers, maxClientsPerServer);
        scheduler.changeStrategy(form.getSelectionPolicy());
        scheduler.serversInitializer();
        this.logger = new Logger(this.logFilePath);
        progressBars = new JProgressBar[numberOfServers];
        for (int i = 0; i < numberOfServers; i++) {
            progressBars[i] = new JProgressBar();
            progressBars[i].setStringPainted(true);
            progressBars[i].setPreferredSize(new Dimension(200, 20));
        }
        new ProgressBars(progressBars);
    }

    @Override
    public void run() {
        currentTime = 1;
        totalWaitingTime = 0;
        totalServiceTime = 0;

        int maxClientsInQueue = 0;
        int peakHour = 1;


        generatedClients = generator.generateRandomClients(numberOfClients);
        generatedClients = generatedClients.stream()
                .sorted(Comparator.comparingInt(Client::getArrivalTime))
                .collect(Collectors.toList());
        for (Client client : generatedClients) {
            totalServiceTime += client.getServiceTime();
        }


        while (currentTime <= simulationTime) {
            inputForm.appendLog("Time " + currentTime);
            inputForm.appendLog("Waiting clients: " + generatedClients + "\n");
            logger.logEvent("Time " + currentTime + "\n");
            logger.logEvent("Waiting clients: " + generatedClients + "\n");

            Iterator<Client> iterator = generatedClients.iterator();
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

                for (int j = 0; j < numberOfServers; j++) {
                    if (scheduler.getServers().get(j).getClients().length == 0) {
                        progressBars[j].setValue(0);
                    } else {
                        progressBars[j].setValue((int) ((double) scheduler.getServers().get(j).getClients().length / maxClientsPerServer * 100));
                    }
                }

            }
            logger.logEvent("\n");
            if (serversEmpty && generatedClients.isEmpty()) {
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
        //
        InputForm form = new InputForm();
        form.setStartButtonActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Simulator simManager = new Simulator(form);
                Thread t = new Thread(simManager);
                t.start();
            }
        });
    }
}