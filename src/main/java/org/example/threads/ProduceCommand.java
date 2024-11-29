package org.example.threads;

import org.example.models.ChefResMonitor;
import java.util.concurrent.ThreadLocalRandom;

public class ProduceCommand implements Runnable{
    private ChefResMonitor chefResMonitor;

    public ProduceCommand(ChefResMonitor chefResMonitor) {
        this.chefResMonitor = chefResMonitor;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            this.chefResMonitor.generatedCommand();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
