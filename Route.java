import java.util.ArrayList;
import java.util.List;


public class Route{

    private int route_ID;
    private int route_Number;
    private String route_Name;
    public List<Integer> rte_stops = new ArrayList();
    public List<Bus> busses = new ArrayList();

    public Route(int id, int num, String name){
        this.route_ID = id;
        this.route_Number = num;
        this.route_Name = name;

    }


    public String get_Name(){
        return this.route_Name;
    }

    public int getRoute_ID(){
        return this.route_ID;
    }

    public void extend_route(int stop_id){
        this.rte_stops.add(stop_id);
    }

    public int get_num_stops(){
        return this.rte_stops.size();
    }

    public int get_stop_id(int index){
        return rte_stops.get(index);
    }



    public double get_distance(int stop1_id, int stop2_id){
        double s1x, s1y, s2x, s2y;
        Stop stop1 = EventControl.get_stop(stop1_id);
        Stop stop2 = EventControl.get_stop(stop2_id);
        s1x = stop1.get_x();
        s1y = stop1.get_y();
        s2x = stop2.get_x();
        s2y = stop2.get_y();

        double dist = 70.0 * Math.sqrt(Math.pow((s1x - s2x), 2) + Math.pow((s1y - s2y), 2));

        return dist;
    }
}
