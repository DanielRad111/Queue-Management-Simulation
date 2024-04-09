package model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    int maxClientsPerServer;

    public Server(int maxClientsPerServer) {
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger();
        this.maxClientsPerServer = maxClientsPerServer;
    }

    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                Client client = clients.take();
                Thread.sleep(client.getServiceTime());
                waitingPeriod.addAndGet(client.getServiceTime());
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }

    public void addClient(Client client) {
            if (clients.size() < maxClientsPerServer) {
                try {
                    clients.put(client);
                    calculateTotalWaitingTime();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                System.out.println("Server is full. Client not added.");
            }
    }

    public Client[] getClients() {
        return clients.toArray(new Client[0]);
    }

    public void calculateTotalWaitingTime() {
        int totalWaitingTime = 0;
        for (Client client : clients) {
            totalWaitingTime += client.getServiceTime();
        }
        waitingPeriod.set(totalWaitingTime);
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
