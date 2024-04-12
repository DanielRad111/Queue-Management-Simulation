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

    @Override
    public void run() {
        while (true) {
            try {
                Client client = clients.peek();
                if (client != null) {
                    Thread.sleep(1000);
                    client.decrementServiceTime();
                    synchronized (waitingPeriod) {
                        waitingPeriod.decrementAndGet();
                    }
                    if (client.getServiceTime() == 0) {
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

    public void addClient(Client client) {
        synchronized (waitingPeriod) {
            if (clients.size() < maxClientsPerServer) {
                try {
                    clients.put(client);
                    waitingPeriod.addAndGet(client.getServiceTime());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("Server is full. Client not added.");
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
    public String toString() {
        return "Server{" +
                "clients=" + clients +
                ", waitingPeriod=" + waitingPeriod +
                '}';
    }
}
