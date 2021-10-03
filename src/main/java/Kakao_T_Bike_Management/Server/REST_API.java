package Kakao_T_Bike_Management.Server;

import Kakao_T_Bike_Management.Service.ServiceManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class REST_API {

    @PostMapping("/postTest")
    public String postTest (
            @RequestHeader("X-Auth-Token") String x_auth_token,
            @RequestBody String qBody) {


        try {
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(qBody);

            return body.get("problem").toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "OK";
    }

    @PostMapping("/start")
    public Map<String, Object> Start(
            @RequestHeader("X-Auth-Token") String x_auth_token,
            @RequestBody String qBody) {

        String auth_key = "";
        if ((auth_key = Authorization.getAuth(x_auth_token)) == null)
            throw new Unauthorized();

        int problem = 0;
        try {
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(qBody);
            problem = Integer.parseInt(body.get("problem").toString());
            System.out.println(problem);
            problem = ServiceManager.getInstance().start(problem);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BadRequest();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("auth_key", auth_key);
        response.put("problem", problem);
        response.put("time", 0);
        return response;
    }

    @GetMapping("/locations")
    public Map<String, Object> Locations(
            @RequestHeader("Authorization") String auth_key) {

        if (!Authorization.isAuth(auth_key))
            throw new Unauthorized();

        List<Object> locations = ServiceManager.getInstance().getLocations();
        Map<String, Object> response = new HashMap<>();
        response.put("locations", locations);
        return response;
    }

    @GetMapping("/trucks")
    public Map<String, Object> Trucks(
            @RequestHeader("Authorization") String auth_key) {

        if (!Authorization.isAuth(auth_key))
            throw new Unauthorized();

        List<Object> trucks = ServiceManager.getInstance().getTrucks();
        Map<String, Object> response = new HashMap<>();
        response.put("trucks", trucks);
        return response;
    }

    @PutMapping("/simulate")
    public Map<String, Object> Simulate(
            @RequestHeader("Authorization") String auth_key,
            @RequestBody String qBody) {

        if (!Authorization.isAuth(auth_key))
            throw new Unauthorized();

        try {
            JSONParser parser = new JSONParser();
            JSONObject body = (JSONObject) parser.parse(qBody);
            Map<Integer, int[]> commandMap = new HashMap<>();
            if (!getCommandMap(body, commandMap))
                throw new BadRequest();
            Map<String, Object> response = ServiceManager.getInstance().Simulate(commandMap);
            return response;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/score")
    public Map<String, Object> Score(
            @RequestHeader("Authorization") String auth_key) {

        if (!Authorization.isAuth(auth_key))
            throw new Unauthorized();

        Map<String, Object> response = new HashMap<>();
        float score = ServiceManager.getInstance().getScore();
        response.put("score", score);
        return response;
    }

    public static boolean getCommandMap(JSONObject jsonObject, Map<Integer, int[]> commandMap) {

        JSONArray commandArrayJson;
        if ((commandArrayJson = (JSONArray) jsonObject.get("commands")) == null)
            return false;

        for (int i=0; i<commandArrayJson.size(); i++) {
            JSONObject commandJson = (JSONObject) commandArrayJson.get(i);
            if (!commandJson.containsKey("truck_id") || !commandJson.containsKey("command"))
                return false;
            int truckId = (int) commandJson.get("truck_id");

            JSONArray commandArray = (JSONArray) commandJson.get("command");

            int[] commands = new int[commandArray.size()];
            for (int j=0; j<commands.length; i++) {
                commands[i] = (int) commandArray.get(i);
            }

            commandMap.put(truckId, commands);
        }
        return true;
    }
}