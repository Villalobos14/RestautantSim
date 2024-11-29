package org.example.threads;

import org.example.models.DinerClient;
import org.example.models.ExitMonitor;
import org.example.models.enums.DinerState;

public class CounterToEat implements Runnable {
    private ExitMonitor exitMonitor;
    private DinerClient dinerClient;

    public CounterToEat(ExitMonitor exitMonitor, DinerClient dinerClient) {
        this.exitMonitor = exitMonitor;
        this.dinerClient = dinerClient;
    }

    @Override
    public void run() {
        while (dinerClient.getTime()>0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            dinerClient.decrementTime();
        }
        dinerClient.setState(DinerState.EAT_FINISH);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        exitMonitor.passToExitQueue();
    }
}

