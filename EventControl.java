import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class EventControl extends JFrame{

    public static List<Event> events = new ArrayList();
    public int cur_time = 0;
    public int eventCounter = 0;

    public static List<Stop> stops = new ArrayList();
    public static List<Route> routes = new ArrayList();
    public static List<Bus> buses = new ArrayList();
    public static List<JPanel> stop_box = new ArrayList();

    public JFrame f = new JFrame("MTS Simulation Control");
    public JPanel sim_layout = new JPanel();
    public JTextArea sim_status = new JTextArea("Status window");
    public static JPanel world_layout = new JPanel();
    public JPanel button_layout = new JPanel();

    public static ImageIcon stop_icon = new ImageIcon("bus_stop_img.png");
    public static ImageIcon bus_icon = new ImageIcon("bus_img.png");

    public EventControl(){
        f.setPreferredSize(new Dimension(1200, 850));
        sim_layout.setLayout(null);
        sim_layout.setBounds(1000, 0, 200, 800);
        //sim_layout.setBackground(Color.green);
        world_layout.setLayout(null);
        world_layout.setBounds(0, 0, 1000, 750);
        world_layout.setBackground(Color.blue);
        button_layout.setLayout(null);
        button_layout.setBounds(0, 750, 800, 150);

        f.setLayout(null);

        f.getContentPane().add(sim_layout);
        f.getContentPane().add(world_layout);
        f.getContentPane().add(button_layout);

        JScrollPane sim_status_scroll = new JScrollPane(sim_status);
        sim_status_scroll.setBounds(0, 0, 200, 800);
        sim_layout.add(sim_status_scroll);

        JButton move_bus = new JButton("Move Next Bus");
        move_bus.setBounds(0, 0, move_bus.getPreferredSize().width, move_bus.getPreferredSize().height);
        button_layout.add(move_bus);

        move_bus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                execute_next();
            }
        });

        f.validate();
        f.pack();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    public static void add_stop(int stop_ID, String name, int stop_Pax, double lat, double lon){
        Stop new_stop = new Stop(stop_ID, name, stop_Pax, lat, lon);


        int pos_x = (int)(lon*2400);
        int pos_y = (int)(lat*1200);

        JLabel bus_stop_img = new JLabel(stop_icon);
        bus_stop_img.setName("bus_stop_img");
        //bus_stop_img.setBounds(pos_x, pos_y, 25, 20);
        JTextField bus_stop_info = new JTextField("Stop#" + stop_ID + " " + name);
        bus_stop_info.setFont(new Font("Courier", Font.BOLD,8));
        bus_stop_info.setEditable(false);
        bus_stop_info.setName("bus_stop_info");

        JTextField bus_info = new JTextField("BUS INFO");
        bus_info.setFont(new Font("Courier", Font.BOLD,8));
        bus_info.setEditable(false);
        bus_info.setName("bus_info");

        JLabel bus_img = new JLabel(bus_icon);
        bus_img.setName("bus_img");
        bus_img.setVisible(false);

        JPanel sp = new JPanel();
        sp.setLayout(new BorderLayout());
        sp.add(bus_stop_info, "North");
        sp.add(bus_stop_img, "West");
        sp.add(bus_info, "South");
        sp.add(bus_img, "East");
        sp.setName("stop_panel");

        sp.validate();
        sp.setBounds(pos_x, pos_y, sp.getPreferredSize().width, sp.getPreferredSize().height);
        sp.setName("" + stop_ID);

        stop_box.add(sp);
        new_stop.set_stop_box_id(stop_box.size()-1);
        stops.add(new_stop);

        world_layout.add(stop_box.get(stops.size()-1));
        world_layout.validate();
    }

    public static void add_route(int id, int num, String name){
        Route new_rte = new Route(id, num, name);
        routes.add(new_rte);
    }

    public static void extend_route(int route_id, int stop_id){
        for(int i = 0; i < routes.size(); i++){
            if(routes.get(i).getRoute_ID() == route_id){
                routes.get(i).extend_route(stop_id);
            }
        }
    }

    public static void add_bus(int bus_id, int route_id, int location, int init_pax, int max_pax, int fuel, int fuel_cap, int speed){
        int rte_index = 0;
        for(int i = 0; i < routes.size(); i++){
            if(routes.get(i).getRoute_ID() == route_id){
                rte_index = i;
            }
        }

        Bus new_bus = new Bus(bus_id, routes.get(rte_index), location, init_pax, max_pax, fuel, fuel_cap, speed);
        buses.add(new_bus);




        int sb_index = 0;

        for(int i = 0; i <stop_box.size(); i++){
            if(Integer.parseInt(stop_box.get(i).getName()) == routes.get(rte_index).rte_stops.get(location)){
                sb_index = i;
            }
        }

        for(int i = 0; i < stop_box.get(sb_index).getComponentCount(); i++){
            if(stop_box.get(sb_index).getComponent(i).getName().equals("bus_img")){
                stop_box.get(sb_index).getComponent(i).setVisible(true);
            }
        }

        world_layout.validate();
    }

    public static void add_event(int time, String type, int id){
        Event new_event = new Event(time, type, id);
        events.add(new_event);

        Collections.sort(events);

    }


    public void execute_next(){
        Event temp_event = events.get(0);
        while(temp_event.get_event_time() != this.cur_time){
            this.cur_time++;
        }

        for(int i = 0; i < stop_box.size(); i++){
            for(int j = 0; j < stop_box.get(i).getComponentCount(); j++){
                if(stop_box.get(i).getComponent(j).getName().equals("bus_info")){
                    ((JTextField)stop_box.get(i).getComponent(j)).setText("");
                }else if(stop_box.get(i).getComponent(j).getName().equals("bus_img")){
                    stop_box.get(i).getComponent(j).setVisible(false);
                }
            }
        }

        this.events.remove(0);
        switch (temp_event.event_type) {
            case "move_bus":
                this.move_bus(temp_event);
                break;

        }

    }

    public JTextArea getSim_status(){
        return this.sim_status;
    }

    private void move_bus(Event e){
        String s = "";
        int bus_index = 0;
        int sb_index = 0;
        int next_sb_index = 0;

        for(int i = 0; i < buses.size(); i++){
            if(buses.get(i).get_bus_ID() == e.get_obj_id()){
                s = buses.get(i).move_bus(e, this.cur_time);
                bus_index = buses.get(i).get_curr_stop();
            }
        }

        int next_stop_id = buses.get(sb_index).get_curr_stop();
        int curr_stop_id = buses.get(sb_index).get_prev_stop();

        getSim_status().append(s);

        for(int i = 0; i < stops.size(); i++) {
            if (Integer.parseInt(stop_box.get(i).getName()) == curr_stop_id) {
                sb_index = i;
            }
            if (Integer.parseInt(stop_box.get(i).getName()) == next_stop_id) {
                next_sb_index = i;
            }
        }

        //int text_index = 0;
        for(int i = 0; i < stop_box.get(sb_index).getComponentCount(); i++){
            if(stop_box.get(sb_index).getComponent(i).getName().equals("bus_info")){
                ((JTextField)(stop_box.get(sb_index).getComponent(i))).setText(s);
            }
        }

        for(int i = 0; i < stop_box.get(next_sb_index).getComponentCount(); i++){
            if(stop_box.get(next_sb_index).getComponent(i).getName().equals("bus_img")){
                stop_box.get(sb_index).getComponent(i).setVisible(true);
                //stop_box.get(sb_index).getComponent(i).setVisible(false);
            }
        }



        stop_box.get(sb_index).validate();

        world_layout.validate();
    }

    public static Stop get_stop(int stop_id){
        int i = 0;

        while(stops.get(i).get_stop_ID() != stop_id){
            i++;
        }
        return stops.get(i);
    }

    public int getCur_time(){
        return this.cur_time;
    }

    public static void build_environment(String [] args){
        final String DELIMITER = ",";
        String scenarioFile = args[0];
        //EventControl e = new EventControl();


        try {
            Scanner takeCommand = new Scanner(new File(scenarioFile));
            String[] tokens;

            do {
                String userCommandLine = takeCommand.nextLine();
                tokens = userCommandLine.split(DELIMITER);
                //System.out.print(tokens[0]);

                switch (tokens[0]) {
                    case "add_depot":
                        //System.out.println(", <ID>:" + tokens[1] + ", <Name>:" + tokens[2] + ", <X-Coord>:" + tokens[3] + ", <Y-Coord>:" + tokens[4]);
                        break;
                    case "add_stop":
                        //System.out.println(", <ID>:" + tokens[1] + ", <Name>:" + tokens[2] + ", <Riders>:" + tokens[3] + ", <Latitude>:" + tokens[4] + ", <Longitude>:" + tokens[5]);
                        add_stop(Integer.parseInt(tokens[1]), tokens[2], 0, Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                        break;
                    case "add_route":
                        //System.out.println(", <ID>:" + tokens[1] + ", <Number>:" + tokens[2] + ", <Name>:" + tokens[3]);
                        add_route(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3]);
                        break;
                    case "extend_route":
                        //System.out.println(", <Route ID>:" + tokens[1] + ", <Stop ID>:" + tokens[2]);
                        extend_route(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        break;
                    case "add_bus":
                        //System.out.println(", <ID>:" + tokens[1] + ", <Route>:" + tokens[2] + ", <Location>:" + tokens[3] + ", <Initial Passengers>:" + tokens[4] + ", <Passenger Capacity>:" + tokens[5] + ", <Initial Fuel>:" + tokens[6] + ", <Fuel Capacity>:" + tokens[7] + ", <Speed>:" + tokens[8]);
                        add_bus(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]),
                                Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]), Integer.parseInt(tokens[8]));
                        break;
                    case "add_event":
                        //System.out.println(", <Time>:" + tokens[1] + ", <Type>:" + tokens[2] + ", <ID>:" + tokens[3]);
                        add_event(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]));
                        break;
                    default:
                        //System.out.println(" command not recognized");
                        break;
                }
            } while (takeCommand.hasNextLine());

            takeCommand.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println();
        }
    }


}
