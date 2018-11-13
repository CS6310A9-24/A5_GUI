public class Event implements Comparable {

    //public static List<Route> routes = new ArrayList();
    public int event_time;
    public String event_type;
    private int obj_id;

    public Event(int time, String type, int id){
        this.event_time = time;
        this.event_type = type;
        this.obj_id = id;
    }


    public int get_event_time(){
        return this.event_time;
    }

    @Override
    public int compareTo(Object o) {
        int compareTime = ((Event)o).get_event_time();
        return this.event_time-compareTime;
    }

    public int get_obj_id(){
        return this.obj_id;
    }
}
