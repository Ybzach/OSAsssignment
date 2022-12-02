public class Burst {
    private String name;
    private int time;
    // private int end;

    public Burst(String name, int time){
        this.name = name;
        this.time = time;
        // this.end = end;
    }

    public String getName(){
        return name;
    }

    public int getTime(){
        return time;
    }

    // public int getEnd(){
    //     return end;
    // }
    public String toString(){
        return this.name + " T:" + this.getTime();
    }
}
