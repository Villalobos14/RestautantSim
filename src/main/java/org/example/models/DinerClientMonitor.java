package org.example.models;
import org.example.models.enums.DinerState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
public class DinerClientMonitor {

    private Deque<DinerClient> queue_wait;
    private Restaurant restaurant;
    private DinerClient enter;
    private int total;
    private int id;

    @Override
    public String toString() {
        return "DinerMonitor{" +
                "queue_wait=" + queue_wait +
                ", total=" + total +
                '}';
    }

    public DinerClientMonitor(int total, Restaurant restaurant){
        this.queue_wait= new LinkedList<DinerClient>();
        this.restaurant=restaurant;
        this.enter=null;
        this.total=total;
        this.id=20;
    }

    public synchronized void generateDinersWait(){
        while (total == queue_wait.size()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DinerClient dinerClient = new DinerClient(id);
        this.id++;
        queue_wait.add(dinerClient);
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void extractDinersWait(){
        while (queue_wait.size() == 0 || restaurant.isFull()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        this.enter=this.queue_wait.getFirst();
        this.enter.setState(DinerState.SIT_WITHOUT_ORDER);
        this.restaurant.setData(this.getEnter());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.queue_wait.removeFirst();
        this.notifyAll();
    }

    public DinerClient getEnter() { return this.enter; }

    public Deque<DinerClient> getQueue_wait() {return queue_wait;}
}
