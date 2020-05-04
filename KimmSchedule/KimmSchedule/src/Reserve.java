import java.io.Serializable;  

public class Reserve implements Serializable {

    private String machine;
    private String time;
    private String date;

    public Reserve(String machine, String time, String date) {
        this.machine = machine;
        this.time = time;
        this.date = date;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }    
    
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}