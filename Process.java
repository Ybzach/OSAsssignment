public class Process{
    private String name;
    private int priority, burstTime, remainingBurstTime, arrivalTime, finishingTime,  turnaroundTime, waitingTime, onHoldTime;

    public Process(String name, int burstTime, int arrivalTime, int priority){
        this.name = name;
        this.burstTime = burstTime;
        this.remainingBurstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
        this.finishingTime = 0;
        this.turnaroundTime = 0;
        this.waitingTime = 0;
        this.onHoldTime = arrivalTime;
    }

    public String getName(){
        return name;
    }

    public int getburstTime(){
        return burstTime;
    }

    public int getRemainingBurstTime(){
        return remainingBurstTime;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getFinshingTime(){
        return finishingTime;
    }

    public int getPriority(){
        return priority;
    }

    public int getTurnaroundTime(){
        return turnaroundTime;
    }

    public int getWaitingTime(){
        return waitingTime;
    }

    public int getOnHoldTime(){
        return onHoldTime;
    }

    // public void setName(String name){
    //     this.name = name;
    // }

    public void setRemainingBurstTime(int remainingBurstTime){
        this.remainingBurstTime = remainingBurstTime;
    }

    // public void setArrivalTime(int arrivalTime){
    //     this.arrivalTime = arrivalTime;
    // }

    // public void setPriority(int priority){
    //     this.priority = priority;
    // }
    public void setFinishingTime(int finishingTime){
        this.finishingTime = finishingTime;
    }

    public void setOnHoldTime(int onHoldTime){
        this.onHoldTime = onHoldTime;
    }

    public String toString(){
        return this.name + " B:" + this.burstTime + " A:" + this.arrivalTime + " P:" + this.getPriority();
    }

    public void calculateTurnaroundTime(){
        this.turnaroundTime = finishingTime - arrivalTime;
    }

    public void calculateWaitingTime(){
        this.waitingTime = turnaroundTime - burstTime;
    }

    public boolean isFinished(){
        return (remainingBurstTime == 0);
    }

}
