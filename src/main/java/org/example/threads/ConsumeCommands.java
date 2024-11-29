package org.example.threads;
import org.example.models.ChefResMonitor;
import java.util.Observable;

public class ConsumeCommands extends Observable implements  Runnable{
    private ChefResMonitor chefResMonitor;

    public ConsumeCommands(ChefResMonitor chefResMonitor) {
        this.chefResMonitor = chefResMonitor;
    }

    @Override
    public void run() {
        while(true){
            this.chefResMonitor.makeTheOrder();
            setChanged();
            notifyObservers("3");
        }
    }

}
