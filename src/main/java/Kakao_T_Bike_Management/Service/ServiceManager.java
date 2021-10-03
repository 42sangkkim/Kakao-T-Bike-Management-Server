package Kakao_T_Bike_Management.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ServiceManager {

    private static ServiceManager instance = null;

    private int problem;
    private int day;
    private State state;
    private int SuccessCount;
    private int FailureCount;
    private float Distance;
    private int time;
    private int width;

    private Map<Integer, Location> locationMap;
    private Map<Integer, Truck> truckMap;
    private Map<Integer, Object> bookMap;
    private Map<Integer, Object> returnMap;
    private List<Command> commandList;

    public static ServiceManager getInstance() {
        return instance;
    }

    public List<Object> getLocations() {
        List<Object> locations = new ArrayList<>();
        for (int id : locationMap.keySet()) {
            locations.add(locationMap.get(id).getInfoMap());
        }
        return locations;
    }

    public List<Object> getTrucks() {
        List<Object> trucks = new ArrayList<>();
        for (int id : truckMap.keySet()) {
            trucks.add(truckMap.get(id).getInfoMap());
        }
        return trucks;
    }

    public float getScore() {
        if (state != State.Finished)
            return 0;

        float S = problem == 1 ? Global.P1_S : Global.P2_S;
        float s = problem == 1 ? Global.P1_s : Global.P2_s;
        float T = problem == 1 ? Global.P1_T : Global.P2_T;

        float SuccessScore = (this.SuccessCount - s) / (S - s) * 100;
        float EfficientScore = (T - this.Distance) / T * 100;

        float TotalScore = SuccessScore * (float) 0.95 + EfficientScore * (float) 0.05;

        instance = null;

        return TotalScore;
    }

    public static int start(int problem) {
        instance = new ServiceManager();
        return instance._init(problem);
    }

    private int _init(int problem) {
        this.problem = problem;
//        this.day = (int) (Math.random() * 3) + 1;
        this.day = 1;
        this.state = State.Null;
        this.SuccessCount = 0;
        this.FailureCount = 0;
        this.Distance = 0;
        this.time = 0;
        _initLocations(problem);
        _initTrucks(problem);
        this.bookMap = ScenarioManager.getBookMap(this.problem, this.day);
        this.returnMap = new HashMap<>();
        return this.problem;
    }

    private void _initLocations(int problem) {
        this.locationMap = new HashMap<>();
        switch (problem) {
            case 1:
                for (int i=0; i<Global.P1_Locations; i++) {
                    locationMap.put(i, new Location(i, Global.P1_Bikes_per_Location));
                }
                break;
            case 2:
                for (int i=0; i<Global.P2_Locations; i++) {
                    locationMap.put(i, new Location(i, Global.P2_Bikes_per_Location));
                }
        }
    }

    private void _initTrucks(int problem) {
        this.truckMap = new HashMap<>();
        switch (problem) {
            case 1:
                for (int i=0; i<Global.P1_Trucks; i++) {
                    truckMap.put(i, new Truck(i));
                }
                break;
            case 2:
                for (int i=0; i<Global.P2_Trucks; i++) {
                    truckMap.put(i, new Truck(i));
                }
        }
    }

    public Map<String, Object> Simulate(Map<Integer, int[]> commandMap) {

        returnBikes();
        rentBikes();
        readCommand(commandMap);
        DoCommand();

        this.time += 1;
        Map<String, Object> response = new HashMap<>();
        response.put("status", this.state);
        response.put("time", this.time);
        response.put("failed_requests_count", this.FailureCount);
        response.put("distance", this.Distance);
        return response;
    }

    private void returnBikes() {
        if (!returnMap.containsKey(Integer.toString(this.time)))
            return;

        List<Integer> returnList =
                (List<Integer>) returnMap.remove(Integer.toString(this.time));
        for (Integer returnInfo : returnList) {
            locationMap.get(returnInfo).returnBike();
        }
    }

    private void rentBikes() {
        System.out.println(bookMap.get(this.time));

        if (!bookMap.containsKey(this.time))
            return;

        List<BookInfo> bookList =
                (List<BookInfo>) bookMap.get(this.time);
        for (BookInfo bookInfo : bookList) {
            if (locationMap.get(bookInfo.getFrom()).getLocated_bikes_count() > 0) {
                this.SuccessCount += 1;
                locationMap.get(bookInfo.getFrom()).takeBike();
                List<Integer> returnList = new ArrayList<>();
                returnList.add(bookInfo.getTo());
                if (returnMap.containsKey(this.time + bookInfo.getDuration()))
                    returnList.addAll((List<Integer>) returnMap.get(this.time + bookInfo.getDuration()));
                returnMap.put(this.time + bookInfo.getDuration(), returnList);
            } else {
                this.FailureCount += 1;
            }
        }
    }

    private void readCommand(Map<Integer, int[]> commandMap) {
        commandList = new ArrayList<>();
        for (int i=0; i<10; i++) {
            for (int truckId : commandMap.keySet()) {
                int[] commands = commandMap.get(truckId);
                if (commands.length > i) {
                    commandList.add(new Command(truckId, commands[i]));
                }
            }
        }
    }

    private void DoCommand() {
        for (Command command : this.commandList) {
            int location_id = truckMap.get(command.getTruckId()).getLocation_id();
            int loaded_bikes_count = truckMap.get(command.getTruckId()).getLoaded_bikes_count();
            switch (command.getAction()) {
                case 1: // Up
                    if (location_id % width != width - 1) {
                        truckMap.get(command.getTruckId()).setLocation_id(location_id + 1);
                        this.Distance += 0.1;
                    }
                    break;
                case 2: // Right
                    if (location_id / width != width - 1) {
                        truckMap.get(command.getTruckId()).setLocation_id(location_id + width);
                        this.Distance += 0.1;
                    }
                    break;
                case 3: // Down
                    if (location_id % width != 0) {
                        truckMap.get(command.getTruckId()).setLocation_id(location_id - 1);
                        this.Distance += 0.1;
                    }
                    break;
                case 4: // Left
                    if (location_id / width != 0) {
                        truckMap.get(command.getTruckId()).setLocation_id(location_id - width);
                        this.Distance += 0.1;
                    }
                    break;
                case 5: // Load
                    if (loaded_bikes_count < 10 && locationMap.get(location_id).getLocated_bikes_count() > 0) {
                        locationMap.get(location_id).takeBike();
                        truckMap.get(command.getTruckId()).loadBike();
                    }
                    break;
                case 6: // Unload
                    if (loaded_bikes_count > 0) {
                        locationMap.get(location_id).returnBike();
                        truckMap.get(command.getTruckId()).unloadBike();
                    }
                    break;
            }
        }
    }


}
