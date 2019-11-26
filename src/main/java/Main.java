
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    private static final String URL = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    private static final String OUTFILE = "src/main/resources/mosmetromap.json";

    public static void main(String[] args) throws ParseException {

        Parser metroParser = new Parser(URL);
        ArrayList<Station> stations = metroParser.parseStations();
        ArrayList<Line> lines = metroParser.parseLines(stations);
        ArrayList<ConnectionMos> connections = metroParser.parseConnections(URL,stations);
        Collections.sort(lines);
        subwayToJson(lines,connections);
      //  countFromJson();
    }
    private static void subwayToJson(ArrayList<Line> lines, ArrayList<ConnectionMos> connections)
    {
        JSONObject  mapOfSubway = new JSONObject();
        JSONObject stationsToMap = new JSONObject();
        JSONArray linesToMap = new JSONArray();
        //Lines and stations to JSON
        lines.forEach(line -> {
            JSONObject lineToObj = new JSONObject();
            JSONArray newline = new JSONArray();
            ArrayList<Station> stations = line.getStations();
            stations.forEach(station -> newline.add(station.getName()));
            stationsToMap.put(line.getNumber(),newline);
            lineToObj.put("number", line.getNumber());
            lineToObj.put("name", line.getName());
            lineToObj.put("color", line.getColor());
            linesToMap.add(lineToObj);
        });
        // Connections to JSON
        JSONArray connectionsToMap = new JSONArray();
        JSONArray newConnection = new JSONArray();
        JSONArray currentConnection = new JSONArray();
        connections.forEach(connection -> {
            newConnection.clear();
            for (Map.Entry<String, String> con : connection.getConnection().entrySet())
            {
                JSONObject thisConnection = new JSONObject();
                thisConnection.put("line",con.getKey());
                thisConnection.put("station",con.getValue());
                newConnection.add(thisConnection);
            }
            currentConnection.add(newConnection);
        });
        connectionsToMap.add(currentConnection);

        //Add Everything to global JSON object
        mapOfSubway.put("stations",stationsToMap);
        mapOfSubway.put("lines", linesToMap);
        mapOfSubway.put("connections", connectionsToMap);

        writeToJson(mapOfSubway);

    }
    private static void writeToJson (JSONObject map)
    {
        try {
            FileOutputStream os=new FileOutputStream(OUTFILE,false);
            BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(os));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonOutput = gson.toJson(map);
            bw.append(jsonOutput);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void countFromJson ()
    {
        JSONParser parser = new JSONParser();
        try {
            JSONObject subway = (JSONObject) parser.parse(new FileReader(OUTFILE));
            JSONObject stations = (JSONObject) subway.get("stations");
            for(Iterator iterator = stations.keySet().iterator(); iterator.hasNext();) {
                String key = (String) iterator.next();
                JSONArray jsonArray = (JSONArray) stations.get(key);
                System.out.println("Line: " + key + " Number of Stations: " + jsonArray.size());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
