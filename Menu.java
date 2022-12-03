import java.util.Comparator;
import java.util.PriorityQueue;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends Application { 
    int numberOfProcesses;
    
    @Override     
    public void start(Stage primaryStage) throws Exception {                  
        PriorityQueue<Process> inputProcesses = new PriorityQueue<Process>(Comparator.comparing(Process :: getArrivalTime).thenComparing(Process :: getPriority));
        Scheduler scheduler = new Scheduler();
        numberOfProcesses = 0;
        
        // Scene 1
        Label arrivalTime = new Label("Arrival Time");
        Label burstTime = new Label("Burst Time");
        Label priority = new Label("Priority");
        TextField arrivalTimeField = new TextField();
        TextField burstTimeField = new TextField();
        TextField priorityField = new TextField();
        Button submitProcess = new Button("Submit");
        Button beginScheduler = new Button("Begin Scheduler");

        GridPane textFields = new GridPane();
        textFields.setAlignment(Pos.CENTER);
        textFields.setHgap(10);
        textFields.setVgap(5);
        textFields.addRow(0, arrivalTime, arrivalTimeField);
        textFields.addRow(1, burstTime, burstTimeField);
        textFields.addRow(2, priority, priorityField);

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().add(submitProcess);
        buttons.getChildren().add(beginScheduler);

        VBox form = new VBox(10);
        form.setAlignment(Pos.CENTER);
        form.getChildren().add(textFields);
        form.getChildren().add(buttons);

        Scene scene1 = new Scene(form ,600, 300); 
        
        // Scene 2
        
        Group group = new Group(); 
        Scene scene2 = new Scene(group ,600, 300); 

        // Button handlers
        submitProcess.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                inputProcesses.add(new Process("P" + numberOfProcesses, 
                                                Integer.parseInt(burstTimeField.getText()), 
                                                Integer.parseInt(arrivalTimeField.getText()), 
                                                Integer.parseInt(priorityField.getText())));
                numberOfProcesses++;
                arrivalTimeField.clear();
                burstTimeField.clear();
                priorityField.clear();
            }
        });

        beginScheduler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scheduler.beginSchedule(inputProcesses);
                primaryStage.setScene(scene2);
            }
        });
        
        primaryStage.setTitle("Preemptive Priority CPU Shceduler"); 
        primaryStage.setScene(scene1); 
        primaryStage.show(); 
    }    
    public static void main(String args[]){          
        launch(args);     
    }         
} 
