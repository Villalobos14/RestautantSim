package org.example.models;

import org.example.models.enums.DinerState;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;


public class ChefResMonitor {
    private Queue<DinerClient> commands;

    private Deque<DinerClient> orders;

    private Restaurant restaurant;

    private int TOTAL;

    public ChefResMonitor(Restaurant restaurant){
        this.commands=new LinkedList<DinerClient>();
        this.orders=new LinkedList<DinerClient>();
        this.restaurant=restaurant;
        this.TOTAL=20;

    }
    public synchronized void makeTheOrder() {
        while (commands.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DinerClient dinerClient =commands.remove();
        dinerClient.setState(DinerState.EAT);
        dinerClient.setTime(getRandomEatTime());
        orders.add(dinerClient);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(5000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();

    }

    public int getRandomEatTime() {
        return  (int)(Math.random() * (10 - 5)) + 5;
    }

    public synchronized void generatedCommand(){
        while (commands.size()==TOTAL) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DinerClient dinerClientOrder =restaurant.getDinnerByState(DinerState.SIT_WITHOUT_ORDER);
        if(dinerClientOrder !=null){
            dinerClientOrder.setState(DinerState.WAIT_ORDER);
            commands.add(dinerClientOrder);
        }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public Deque<DinerClient> getOrders() {
        return orders;
    }
}
