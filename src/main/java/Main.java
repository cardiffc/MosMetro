
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final String URL = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
    private static final String OUTFILE = "src/main/out/test.json";

    public static void main(String[] args) {

        Parser metroParser = new Parser(URL);
        ArrayList<Station> stations = metroParser.parseStations();
        ArrayList<Line> lines = metroParser.parseLines(stations);
        ArrayList<ConnectionMos> connections = metroParser.parseConnections(URL,stations);
//        connections.forEach(connection -> {
//            System.out.println("#####################################################");
//            for (Map.Entry<String, String> con : connection.getConnection().entrySet())
//            {
//
//                System.out.println(con.getKey() + "/" + con.getValue());
//            }
//        });

     createOut(lines,connections);


    }
    public static void createOut (ArrayList<Line> lines, ArrayList<ConnectionMos> connections)
    {
        JSONObject  mapOfSubway = new JSONObject();
        JSONObject stationsToMap = new JSONObject();
        JSONArray linesToMap = new JSONArray();
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

        JSONArray connectionsToMap = new JSONArray();
        connections.forEach(connection -> {
            JSONArray newConnection = new JSONArray();
            TreeMap<String, String> tempConnection = connection.getConnection();
            JSONObject thisConnection = new JSONObject();
            for (Map.Entry<String, String> con : tempConnection.entrySet())
            {
                thisConnection.put("line",con.getKey());
                thisConnection.put("station",con.getValue());
                newConnection.add(thisConnection);
            }

            connectionsToMap.add(newConnection);
        });
        mapOfSubway.put("stations",stationsToMap);
        mapOfSubway.put("connections", connectionsToMap);
        mapOfSubway.put("lines", linesToMap);

        writeToJson(mapOfSubway);

    }
    public static void writeToJson (JSONObject map)
    {
        try (FileWriter file = new FileWriter(OUTFILE)) {
            file.write(map.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
