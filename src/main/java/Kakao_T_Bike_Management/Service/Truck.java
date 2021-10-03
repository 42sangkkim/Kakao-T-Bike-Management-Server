package Kakao_T_Bike_Management.Service;

import java.util.HashMap;
import java.util.Map;

public class Truck {
    private int id;
    private int location_id;
    private int loaded_bikes_count;

    public Truck(int id) {
        this.id = id;
        this.location_id = 0;
        this.loaded_bikes_count = 0;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public int getLoaded_bikes_count() {
        return loaded_bikes_count;
    }

    public void loadBike() {
        this.loaded_bikes_count += 1;
    }

    public void unloadBike() {
        this.loaded_bikes_count -= 1;
    }

    public Map<String, Object> getInfoMap() {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("id", this.id);
        infoMap.put("location_id", this.location_id);
        infoMap.put("loaded_bikes_count", this.loaded_bikes_count);
        return infoMap;
    }
}