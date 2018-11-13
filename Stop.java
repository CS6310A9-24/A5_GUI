import javax.swing.*;
import java.awt.*;

public class Stop{

    private int stop_ID;
    private String stop_Name;
    private int pax;
    private double x;
    private double y;
    private int stop_box_id;


    public Stop(int stop_ID, String name, int stop_Pax, double lat, double lon){
        this.stop_ID = stop_ID;
        this.stop_Name = name;
        this.pax = stop_Pax;
        this.x = lat;
        this.y = lon;
        this.stop_box_id = 0;

    }

    public String get_Name(){
        return this.stop_Name;
    }

    public double get_x(){
        return this.x;
    }

    public double get_y(){
        return this.y;
    }

    public int get_stop_ID(){
        return this.stop_ID;
    }

    public int set_stop_box_id(int id){
        return this.stop_box_id = id;
    }
}
