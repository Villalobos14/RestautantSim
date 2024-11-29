package org.example.models;

import org.example.models.enums.DinerState;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


public class ExitMonitor {
    private Deque<DinerClient> exitQueue;
    private Restaurant restaurant;
    private DinerClient exit;

    public ExitMonitor( Restaurant restaurant) {
        this.exitQueue = new LinkedList<DinerClient>();
        this.restaurant=restaurant;
    }
    public synchronized void extractDinersExit(){
        while (exitQueue.size() == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.exit=this.exitQueue.getFirst();
        this.exit.setState(DinerState.EXIT);
        restaurant.removeDinnerByTableId(exit.getTableId());
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public synchronized void passToExitQueue(){
        while (exitQueue.size()==20) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        DinerClient dinerClientOrder =restaurant.getDinnerByState(DinerState.EAT_FINISH);
        if(dinerClientOrder !=null){
            dinerClientOrder.setState(DinerState.WAIT_ORDER);
            this.exitQueue.add(dinerClientOrder);
        }
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(2000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.notifyAll();
    }

    public DinerClient removeFromExitQueue(){
        return this.exitQueue.remove();
    }
}
