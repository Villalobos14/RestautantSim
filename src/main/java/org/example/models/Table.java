package org.example.models;
import org.example.models.enums.TableState;
public class Table {
    private DinerClient dinerClient;
    private TableState state;

    public Table(DinerClient dinerClient) {
        this.dinerClient = dinerClient;
        this.state=TableState.EMPTY;
    }

    public DinerClient getDiner() {
        return dinerClient;
    }

    public void setDiner(DinerClient dinerClient) {
        this.dinerClient = dinerClient;
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
                "diner=" + dinerClient +
                ", state=" + state +
                '}';
    }
}
