package logic.generator;

import model.Client;
import model.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Generator {
    private static int idCopy;
    private Random random;
    private Integer numberOfClients;
    private Integer numberOfQueues;
    private Integer maxSimulationTime;
    private Integer minArrivalTime;
    private Integer minServiceTime;
    private Integer maxArrivalTime;
    private Integer maxServiceTime;

    public Generator(Random random, Integer numberOfClients, Integer numberOfQueues, Integer maxSimulationTime, Integer minArrivalTime, Integer maxArrivalTime, Integer minServiceTime, Integer maxServiceTime) {
        this.random = random;
        this.numberOfClients = numberOfClients;
        this.numberOfQueues = numberOfQueues;
        this.maxSimulationTime = maxSimulationTime;
        this.minArrivalTime = minArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxArrivalTime = maxArrivalTime;
        this.maxServiceTime = maxServiceTime;
        this.idCopy = 1;
    }

    public Client generateRandomClient() {
        int id = idCopy++;
        int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;
        int serviceTime = random.nextInt(maxServiceTime - minServiceTime) + minServiceTime;
        return new Client(id, arrivalTime, serviceTime);
    }

    public List<Client> generateRandomClients(int numClients) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < numClients; i++) {
            clients.add(generateRandomClient());
        }
        return clients;
    }
}
