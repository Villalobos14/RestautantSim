package org.example.threads;

import org.example.models.DinerClientMonitor;
import java.util.Observable;

public class ProduceQueueWait  extends Observable implements Runnable{

    private final DinerClientMonitor dinerClientMonitor;
    public ProduceQueueWait(DinerClientMonitor dinerClientMonitor){
        this.dinerClientMonitor = dinerClientMonitor;
    }
    @Override
    public void run() {
        while (true){
            this.dinerClientMonitor.generateDinersWait();
            setChanged();
            notifyObservers("1");
        }
    }
}
