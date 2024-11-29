package org.example.threads;


import java.util.Observable;
import org.example.models.DinerClientMonitor;

public class ConsumeQueueWait  extends Observable implements Runnable{
    private DinerClientMonitor dinerClientMonitor;

    public ConsumeQueueWait(DinerClientMonitor dinerClientMonitor){
        this.dinerClientMonitor = dinerClientMonitor;
    }
    @Override
    public void run() {
        while (true){
            System.out.println("ESTOY AQUI");
            this.dinerClientMonitor.extractDinersWait();
            setChanged();
            notifyObservers("2");
        }
    }
}
