package main;

import logic.generator.Generator;
import logic.scheduler.Scheduler;
import logic.scheduler.strategy.TimeStrategy;
import logic.simulator.Simulator;
import model.Client;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        Integer numberOfClients = 4;
        Integer numberOfQueues = 2;
        Integer maxSimulationTime = 100;
        Integer minArrivalTime = 2;
        Integer maxArrivalTime = 30;
        Integer minServiceTime = 2;
        Integer maxServiceTime = 4;
        int maxClientsPerServer = 3;
        int maxNoServers = 2;
        Generator generator = new Generator(random, numberOfClients, numberOfQueues, maxSimulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, maxClientsPerServer);
        Scheduler scheduler = new Scheduler(maxNoServers, maxClientsPerServer);
        Simulator simulator = new Simulator(generator, scheduler);
        simulator.run();
    }
}