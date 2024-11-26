package org.example.threads;


import java.util.Observable;
import org.example.models.DinerMonitor;
import org.example.models.Restaurant;

import java.util.ArrayList;
public class ConsumeQueueWait  extends Observable implements Runnable{
    private DinerMonitor dinerMonitor;

    public ConsumeQueueWait(DinerMonitor dinerMonitor){
        this.dinerMonitor=dinerMonitor;
    }
    @Override
    public void run() {
        while (true){
            System.out.println("ESTOY AQUI");
            this.dinerMonitor.extractDinersWait();
            setChanged();
            notifyObservers("2");
        }
    }
}
