package org.example.models;
import org.example.models.enums.TableState;
public class Table {
    private Diner diner;
    private TableState state;

    public Table(Diner diner) {
        this.diner = diner;
        this.state=TableState.EMPTY;
    }

    public Diner getDiner() {
        return diner;
    }

    public void setDiner(Diner diner) {
        this.diner = diner;
    }

    public TableState getState() {
        return state;
    }

    public void setState(TableState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Table{" +
                "diner=" + diner +
                ", state=" + state +
                '}';
    }
}
