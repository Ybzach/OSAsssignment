import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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
        beginScheduler.setVisible(false);

        // beginScheduler.visibleProperty().bind(new SimpleIntegerProperty(numberOfProcesses).greaterThan(1));        


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

        Scene inputScene = new Scene(form ,600, 400); 
        
        // Scene 2
        TableView<Process> table = new TableView<Process>();
        // table.
        // table.setPrefWidth(273);
        table.setFixedCellSize(25);
        table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());

        TableColumn<Process, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Process, Integer> arrivalTimeCol = new TableColumn<>("Arrival Time");
        arrivalTimeCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        TableColumn<Process, Integer> burstTimeCol = new TableColumn<>("Burst Time");
        burstTimeCol.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        TableColumn<Process, Integer> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        table.getColumns().addAll(Arrays.asList(nameCol, arrivalTimeCol, burstTimeCol, priorityCol));
        
        nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        arrivalTimeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        burstTimeCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
        priorityCol.prefWidthProperty().bind(table.widthProperty().multiply(0.24));
        
        Label totalTurnaroundTimeLabel = new Label();
        Label totalWaitingTimeLabel = new Label();
        Label averageTurnaroundTimeLabel = new Label();
        Label averageWaitingTimeLabel = new Label();

        GridPane resultsGrid = new GridPane();
        resultsGrid.setAlignment(Pos.CENTER);
        resultsGrid.setHgap(10);
        resultsGrid.addRow(0, totalTurnaroundTimeLabel, averageTurnaroundTimeLabel);
        resultsGrid.addRow(1, totalWaitingTimeLabel, averageWaitingTimeLabel);
        
        Group ganttChartWrapper = new Group();
        // ganttChartWrapper.set

        VBox resultsWrapper = new VBox();
        resultsWrapper.setSpacing(10);
        resultsWrapper.setPrefWidth(primaryStage.getWidth());
        resultsWrapper.getChildren().add(table);
        resultsWrapper.getChildren().add(resultsGrid);
        resultsWrapper.getChildren().add(ganttChartWrapper);

        Scene resultScene = new Scene(resultsWrapper, 600, 400); 

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
                IntegerProperty numberofProcessesProperty = new SimpleIntegerProperty(numberOfProcesses);
                beginScheduler.visibleProperty().bind(numberofProcessesProperty.greaterThan(2));        
                submitProcess.visibleProperty().bind(numberofProcessesProperty.lessThan(10));        

            }
        });

        beginScheduler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DecimalFormat decimalFormatter = new DecimalFormat("#.##");
                ArrayList<Burst> ganttChart = scheduler.beginSchedule(inputProcesses);
                addProcessesToTable(table, inputProcesses.iterator());
                primaryStage.setScene(resultScene);
                totalTurnaroundTimeLabel.setText("Total Turnaround Time: " + decimalFormatter.format(scheduler.getTotalTurnaroundTime()));
                totalWaitingTimeLabel.setText("Total Waiting Time: " + decimalFormatter.format(scheduler.getTotalWaitingTime()));
                averageTurnaroundTimeLabel.setText("Average Turnaround Time: " + decimalFormatter.format(scheduler.getAverageTurnaroundTime()));
                averageWaitingTimeLabel.setText("Average Waiting Time: " + decimalFormatter.format(scheduler.getAverageWaitingTime()));
                int startCoorX = 0;
        
                Text startTimeText = new Text("" + ganttChart.get(0).getTime());
                startTimeText.setX(startCoorX);
                startTimeText.setY(45);
                ganttChartWrapper.getChildren().add(startTimeText);

                for (int i = 1; i < ganttChart.size(); i++){
                    Burst burst = ganttChart.get(i);
                    Rectangle rectangle = new Rectangle(60, 30);
                    ganttChartWrapper.getChildren().add(rectangle);
                    rectangle.setX(startCoorX);
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(Color.BLACK);
                    Text processNameText = new Text(burst.getName());
                    processNameText.setX(startCoorX + 10);
                    processNameText.setY(20);
                    ganttChartWrapper.getChildren().add(processNameText);
                    startCoorX += 60;
                    Text processTimeText = new Text("" + burst.getTime());
                    processTimeText.setX(startCoorX - 5);
                    processTimeText.setY(45);
                    ganttChartWrapper.getChildren().add(processTimeText);
                }
            }
        });

        
        
        primaryStage.setTitle("Preemptive Priority CPU Scheduler"); 
        primaryStage.setScene(inputScene); 
        primaryStage.show(); 


    }    

    private void addProcessesToTable(TableView<Process> table, Iterator<Process> it){
        while(it.hasNext()){
            Process tempProcess = it.next();
            table.getItems().add(new Process(tempProcess.getName(), tempProcess.getBurstTime(), tempProcess.getArrivalTime(), tempProcess.getPriority()));
        }
    }
    public static void main(String args[]){          
        launch(args);     
    }         
} 
