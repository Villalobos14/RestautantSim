package org.example.controller;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import org.example.models.*;
import org.example.threads.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadLocalRandom;



public class MainController implements Observer{
    private DinerMonitor dinerMonitor;

    private ExitMonitor exitMonitor;

    private Restaurant restaurant;

    private ChefMonitor chefMonitor;

    private final Color EmptySpaceColor=Color.web("#98FB98");


    private final Color WaitresColor=Color.web("#FFD700");

    private final Color ChefColor=Color.web("#FF6F61");

    @FXML
    private Button btn_start;

    @FXML
    private Circle enter_waitress;


    @FXML
    private Circle add_command;

    @FXML
    private Circle cooking_command;

    @FXML
    private Circle deliver_command;

    @FXML
    private Circle enter_diner;

    @FXML
    private Circle exit;

    @FXML
    private Circle exit_door;

    @FXML
    private HBox queue_wait;

    @FXML
    private Circle receive_command;

    @FXML
    private GridPane tables;

    @FXML
    private Circle wait_command;
    @FXML
    public void initialize() {
        this.restaurant=new Restaurant();
        this.dinerMonitor=new DinerMonitor(10, this.restaurant);
        this.chefMonitor=new ChefMonitor(this.restaurant);
        this.exitMonitor=new ExitMonitor(this.restaurant);
    }

    @FXML
    void onClickedStart(MouseEvent event) {
        initSimulation();
    }

    private void initSimulation(){
        ConsumeQueueWait consumeQueueWait= new ConsumeQueueWait(this.dinerMonitor);
        ProduceQueueWait produceQueueWait = new ProduceQueueWait(this.dinerMonitor);
        ConsumeCommands consumeCommands= new ConsumeCommands(this.chefMonitor);
        ConsumeExitQueue ConsumeExitQueue = new ConsumeExitQueue(this.exitMonitor);

        consumeQueueWait.addObserver(this);
        produceQueueWait.addObserver(this);
        consumeCommands.addObserver(this);
        ConsumeExitQueue.addObserver(this);

        Thread consumeCommandsThread= new Thread(consumeCommands);
        consumeCommandsThread.setDaemon(true);
        Thread produceQueueWaitThread = new Thread(produceQueueWait);
        produceQueueWaitThread.setDaemon(true);
        Thread consumeQueueWaitThread = new Thread(consumeQueueWait);
        consumeQueueWaitThread.setDaemon(true);
        Thread ConsumeExitQueueThread= new Thread(ConsumeExitQueue);
        ConsumeExitQueueThread.setDaemon(true);

        consumeCommandsThread.start();
        ConsumeExitQueueThread.start();
        produceQueueWaitThread.start();
        consumeQueueWaitThread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        switch (Integer.valueOf(String.valueOf(o))) {
            case 1:
                addDinerToQueueWait();
                break;
            case 2:
                enterDinerToEntrace();
                waitSecond(2);
                sitDinerAtSomeTable();
                makeOrder();
                break;
            case 3:
                eatTimer();
                break;
            case 4:
                leaveDiner();
                break;
        }
    }

    private void leaveDiner(){
        Diner diner=exitMonitor.removeFromExitQueue();
        StackPane stackPane= getTable(diner.getTableId());
        Circle circle=(Circle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            text.setText("-");
            text.setFill(Color.WHITE);
            circle.setFill(EmptySpaceColor);
            exit_door.setFill(diner.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit_door.setFill(EmptySpaceColor);
            exit.setFill(diner.getColor());
        });
        waitSecond(2);
        Platform.runLater(()->{
            exit.setFill(EmptySpaceColor);
        });

    }

    private void eatTimer(){
        deliverCommand();
        waitSecond(1);
        Diner diner=chefMonitor.getOrders().remove();
        StackPane stackPane=getTable(diner.getTableId());
        Text text = (Text) stackPane.getChildren().get(1);
        Timeline timeline = new Timeline();
        EventHandler<ActionEvent> eventHandler = event -> {
            Platform.runLater(()->{
                text.setText(String.valueOf(diner.getTime()));
            });
            if (diner.getTime() == 0) {
                Platform.runLater(()->{
                    text.setText("-F");
                });
                timeline.stop();
            }
        };
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), eventHandler);
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        countdown(diner);
        waitSecond(1);
        CheckCommands();
    }

    private void CheckCommands(){
        Platform.runLater(()->{
            wait_command.setFill(ChefColor);
            cooking_command.setFill(ChefColor);
            deliver_command.setFill(EmptySpaceColor);
        });
    }

    private void deliverCommand(){
        Platform.runLater(()->{
            deliver_command.setFill(ChefColor);
            cooking_command.setFill(EmptySpaceColor);
            wait_command.setFill(EmptySpaceColor);
        });
    }

    private void countdown(Diner diner){
        CounterToEat counterEatDiner=new CounterToEat(this.exitMonitor, diner);
        Thread CounterToEatThread=new Thread(counterEatDiner);
        CounterToEatThread.setDaemon(true);
        CounterToEatThread.start();
    }
    private void makeOrder(){
        ProduceCommand produceCommand=new ProduceCommand(this.chefMonitor);
        Thread produceCommandThread=new Thread(produceCommand);
        produceCommandThread.setDaemon(true);
        produceCommandThread.start();
        workChef();
    }

    private void workChef(){
        Platform.runLater(()->{
            wait_command.setFill(EmptySpaceColor);
            deliver_command.setFill(EmptySpaceColor);
            cooking_command.setFill(ChefColor);
        });
    }

    private void enterDinerToEntrace(){
        Circle popDiner= (Circle) queue_wait.getChildren().get(0);
        Platform.runLater(()->{
            enter_diner.setFill(popDiner.getFill());
            queue_wait.getChildren().remove(popDiner);
        });
    }
    private void sitDinerAtSomeTable(){
        for(Node hboxNode:tables.getChildren()){
            HBox hbox=(HBox) hboxNode;
            Circle waitress = (Circle) hbox.getChildren().get(0);
            StackPane stackPane= (StackPane) hbox.getChildren().get(1);
            Circle diner = (Circle) stackPane.getChildren().get(0);
            if(EmptySpaceColor.equals(diner.getFill())){
                sitDiner(waitress, stackPane);
                waitSecond(1);
                waitresReturnEntrace(waitress);
                break;
            }
        }
    }

    private void sitDiner( Circle waitress,StackPane stackPane){
        Circle diner = (Circle) stackPane.getChildren().get(0);
        Text text = (Text) stackPane.getChildren().get(1);
        Platform.runLater(()->{
            enter_waitress.setFill(EmptySpaceColor);
            waitress.setFill(WaitresColor);
            diner.setFill(enter_diner.getFill());
            text.setFill(Color.BLACK);
            text.setText("-W");
            enter_diner.setFill(EmptySpaceColor);
        });
    }

    private void waitresReturnEntrace(Circle waitress){
        Platform.runLater(()->{
            enter_waitress.setFill(WaitresColor);
            waitress.setFill(EmptySpaceColor);
        });
    }
    private void addDinerToQueueWait() {
        Diner newDiner = this.dinerMonitor.getQueue_wait().getLast();
        Circle circle = new Circle(25, newDiner.getColor()); // Círculo con radio 25 y color del comensal
        Platform.runLater(() -> {
            queue_wait.getChildren().add(circle); // Agrega el círculo a la cola visual
        });
    }


    private void waitSecond(int second){
        int milliseconds= second*1000;
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private StackPane getTable(int id){
        Node hboxNode=tables.getChildren().get(id);
        HBox hbox=(HBox) hboxNode;
        return  (StackPane) hbox.getChildren().get(1);
    }
}
