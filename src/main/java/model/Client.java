package model;

public class Client {
    private int id;

    private int arrivalTime;

    private int serviceTime;

    private int remainingServiceTime;


    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.remainingServiceTime = serviceTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getRemainingServiceTime() {
        return remainingServiceTime;
    }

    public int getId() {
        return id;
    }

    public void decrementRemainingServiceTime() {
        if (remainingServiceTime > 0) {
            remainingServiceTime--;
        }
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                ", remainingServiceTime=" + remainingServiceTime +
                '}';
    }
}
