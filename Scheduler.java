import java.text.DecimalFormat;
import java.util.*;

public class Scheduler {
    private int totalTurnaroundTime;
    private int totalWaitingTime;
    private double averageTurnaroundTime;
    private double averageWaitingTime;

    public int getTotalTurnaroundTime() {
        return totalTurnaroundTime;
    }

    public int getTotalWaitingTime() {
        return totalWaitingTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }

    public double getAverageWaitingTime() {
        return averageWaitingTime;
    }

        public Scheduler(){
        this.totalTurnaroundTime = 0;
        this.totalWaitingTime = 0;
        this.averageTurnaroundTime = 0;
        this.averageWaitingTime = 0;
    }

    public Process selectProcess(PriorityQueue<Process> inputQueue, Process previousProcess, int time){
        PriorityQueue<Process> queueClone = new PriorityQueue<Process>(inputQueue);
        Process selectedProcess = inputQueue.peek();
        while(!(queueClone.isEmpty())){
            Process tempProcess = queueClone.poll();
            if(tempProcess.getArrivalTime() <= time && (!(tempProcess.isFinished()))){
                selectedProcess = tempProcess;
                break;
            }
        }

        if (previousProcess.getName() != selectedProcess.getName()) {
            inputQueue.remove(previousProcess);
            previousProcess.setOnHoldTime(time);
            inputQueue.add(previousProcess);
        } 

        return selectedProcess;
    }

    public void emptyBurst(ArrayList<Burst> chart, int time){
        chart.add(new Burst(null, time));
    }

    public void beginSchedule(PriorityQueue<Process> inputProcesses){
        
        PriorityQueue<Process> sortedProcesses = new PriorityQueue<Process>(Comparator.comparing(Process :: getPriority).thenComparing(Process :: getOnHoldTime));
        ArrayList<Burst> ganttChart = new ArrayList<Burst>();

        
        int numberOfProcesses = inputProcesses.size();
        int processesCompleted = 0;
        Process currentProcess = inputProcesses.peek();
        int time = currentProcess.getArrivalTime();
                
        Iterator<Process> it = inputProcesses.iterator();
        while(it.hasNext()){
            sortedProcesses.add(it.next());
        }
        
        do{
            currentProcess = selectProcess(sortedProcesses, currentProcess, time);
            
            if(currentProcess.isFinished()){
                emptyBurst(ganttChart, time);
                time++;
                continue;
            }

            currentProcess.setRemainingBurstTime(currentProcess.getRemainingBurstTime() - 1);
            ganttChart.add(new Burst(currentProcess.getName(), time));

            if(currentProcess.isFinished()){
                processesCompleted++;
                currentProcess.setFinishingTime(time + 1);
                currentProcess.calculateTurnaroundTime();
                currentProcess.calculateWaitingTime();
                this.totalTurnaroundTime += currentProcess.getTurnaroundTime();
                this.totalWaitingTime += currentProcess.getWaitingTime();
                this.averageTurnaroundTime = (double)totalTurnaroundTime / processesCompleted;
                this.averageWaitingTime = (double)totalWaitingTime / processesCompleted;
            }
            time++;
        }while(processesCompleted != numberOfProcesses);

        for (int i = 0; i < ganttChart.size(); i++){
            System.out.println(ganttChart.get(i));
        }
    }
   
    public static void main(String[] args){
        PriorityQueue<Process> inputProcesses = new PriorityQueue<Process>(Comparator.comparing(Process :: getArrivalTime).thenComparing(Process :: getPriority));

        inputProcesses.add(new Process("P0", 8, 0, 2));
        inputProcesses.add(new Process("P5", 6, 0, 1));
        inputProcesses.add(new Process("P1", 15, 4, 5));
        inputProcesses.add(new Process("P4", 13, 9, 4));
        inputProcesses.add(new Process("P2", 9, 7, 3));
        inputProcesses.add(new Process("P3", 5, 13, 1));
        Scheduler scheduler = new Scheduler();

        scheduler.beginSchedule(inputProcesses);
        System.out.println(scheduler.getTotalTurnaroundTime());
        System.out.println(scheduler.getTotalWaitingTime());
        System.out.println(scheduler.getAverageWaitingTime());
        System.out.println(scheduler.getAverageTurnaroundTime());

    }
}
