package Kakao_T_Bike_Management.Service;

import java.util.HashMap;
import java.util.Map;

public class Location {
    private int id;
    private int located_bikes_count;

    public Location(int id, int located_bikes_count) {
        this.id = id;
        this.located_bikes_count = located_bikes_count;
    }

    public int getLocated_bikes_count() {
        return located_bikes_count;
    }

    public void setLocated_bikes_count(int located_bikes_count) {
        this.located_bikes_count = located_bikes_count;
    }

    public void returnBike() {
        this.located_bikes_count += 1;
    }

    public void takeBike() {
        this.located_bikes_count -= 1;
    }

    public Map<String, Object> getInfoMap() {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("id", this.id);
        infoMap.put("located_bikes_count", this.located_bikes_count);
        return infoMap;
    }
}