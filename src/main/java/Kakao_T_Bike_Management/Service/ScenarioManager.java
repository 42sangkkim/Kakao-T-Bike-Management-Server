package Kakao_T_Bike_Management.Service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScenarioManager {
    private static final String filePath = "./src/main/java/Kakao_T_Bike_Management/Scenarios/";

    private static JSONObject scenario = null;

    public static List<BookInfo> getBookListOn(Object key) {
        if (scenario == null)
            return null;

        List<BookInfo> bookList = new ArrayList<>();
        if (!scenario.containsKey(key))
            return bookList;

        JSONArray bookArray = (JSONArray) scenario.get(key);
        for (Object bookObject : bookArray) {
            bookObject.toString();
            JSONArray bookJson = (JSONArray) bookObject;
            int from = Integer.parseInt(bookJson.get(0).toString());
            int to = Integer.parseInt(bookJson.get(0).toString());
            int duration = Integer.parseInt(bookJson.get(0).toString());

            BookInfo bookInfo = new BookInfo(from, to, duration);
            bookList.add(bookInfo);
        }

        return bookList;
    }

    public static Map<Integer, Object> getBookMap(int problem, int day) {
        try {
            String fileName = "problem"+problem+"_day-"+day+".json";
            Reader reader = new FileReader(filePath + fileName);
            JSONParser parser = new JSONParser();
            scenario = (JSONObject) parser.parse(reader);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<Integer, Object> bookMap = new HashMap<>();
        for (Object key : scenario.keySet()) {
            bookMap.put(Integer.parseInt((String) key), getBookListOn(key));
        }
        System.out.println(bookMap);
        return bookMap;
    }
}
