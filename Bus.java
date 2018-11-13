import javax.swing.*;

public class Bus{
    private int bus_ID;
    public int max_num_pax;
    private int curr_num_pax;
    private int speed;
    private int init_fuel;
    private int curr_fuel;
    private Route rte;
    private int bus_rte_loc_index;
    private int pre_stop_pax;
    private int post_stop_pax;
    private int next_stop_index;

    public Bus(int id, Route route, int location, int init_pax, int max_pax, int fuel, int fuel_cap, int speed){
        this.bus_ID = id;
        this.rte = route;
        this.bus_rte_loc_index = location;
        this.curr_num_pax = init_pax;
        this.max_num_pax = max_pax;
        this.curr_fuel = 0;// = fuel (not worried about fuel at the moment)
        this.init_fuel = fuel_cap;
        this.speed = speed;
        this.next_stop_index = this.bus_rte_loc_index + 1;
    }

    public int get_bus_ID(){
        return this.bus_ID;
    }

    public String move_bus(Event e, int cur_time){

        int travel_time = 0;

        if(this.bus_rte_loc_index == rte.get_num_stops()-1)
        {
            this.next_stop_index = 0;
        }else{
            this.next_stop_index = bus_rte_loc_index + 1;
        }

        int bus_rte_loc = rte.rte_stops.get(this.bus_rte_loc_index);
        int next_stop = rte.rte_stops.get(this.next_stop_index);

        double dist = (int)(rte.get_distance(bus_rte_loc, next_stop));
        travel_time = 1 + (int)(dist * 60 / this.speed);

        EventControl.add_event(travel_time + cur_time, "move_bus", this.bus_ID);

        this.bus_rte_loc_index = this.next_stop_index;

        return ("\n" + "b:" + this.bus_ID + "->s:" + next_stop + "@" + (travel_time + cur_time) + "//p:"//bus_rte_loc is the destination index
               + this.curr_num_pax + "/f:" + this.curr_fuel);


        //System.out.println(sim_status_window.getText());
    }

    public int get_curr_stop(){
        return rte.get_stop_id(bus_rte_loc_index);
    }

    public int get_prev_stop(){
        if(bus_rte_loc_index - 1 < 0){
            return rte.get_stop_id(0);
        }else {
            return rte.get_stop_id(bus_rte_loc_index - 1);
        }
    }


}
