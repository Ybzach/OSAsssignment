import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Menu extends Application { 
    @Override     
    public void start(Stage primaryStage) throws Exception {                  
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
    
        //creating a Group object 
        Group group = new Group(); 
       
        //Creating a Scene by passing the group object, height and width   
        Scene scene = new Scene(form ,600, 300); 
        primaryStage.setTitle("Preemptive Priority CPU Shceduler"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }    
    public static void main(String args[]){          
      launch(args);     
    }         
} 
