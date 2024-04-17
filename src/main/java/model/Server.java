package model;

import logic.simulator.Simulator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    int maxClientsPerServer;

    public Server(int maxClientsPerServer) {
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger();
        this.maxClientsPerServer = maxClientsPerServer;
    }

    public void addClient(Client client) {
        synchronized (waitingPeriod) {
            try {
                clients.put(client);
                waitingPeriod.addAndGet(client.getServiceTime());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Client[] getClients() {
        return clients.toArray(new Client[0]);
    }

    public int getWaitingTime() {
        return waitingPeriod.get();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Client client = clients.peek();
                if (client != null) {
                    Thread.sleep(1000);
                    client.decrementRemainingServiceTime();
                    synchronized (waitingPeriod) {
                        waitingPeriod.decrementAndGet();
                    }
                    if (client.getRemainingServiceTime() == 0) {
                        clients.poll();
                    }
                } else {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Server{" +
                "clients=" + clients +
                ", waitingPeriod=" + waitingPeriod +
                '}';
    }
}
