import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Main {
    private static final Logger MARKLOGGER = LogManager.getLogger(Main.class);
    private static final Marker INVALID_FILE = MarkerManager.getMarker("INVALID_FILE");

    private static final String URL = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    private static final String OUTFILE = "src/main/resources/mosmetromap.json";

    public static void main(String[] args)  {

        Parser metroParser = new Parser(URL);
        Object[] subway = metroParser.parseSubway();
        ArrayList<Line> lines = (ArrayList<Line>) subway[1];
        ArrayList<Station> stations = (ArrayList<Station>) subway[0];
        ArrayList<ConnectionMos> connections = metroParser.parseConnections(URL,stations);
        Collections.sort(lines);
        Collections.sort(stations);
      //  connections.forEach(connectionMos -> System.out.println(connectionMos.getConnection()));
        subwayToJson(lines,connections);
        countFromJson();
    }
    private static void subwayToJson(ArrayList<Line> lines, ArrayList<ConnectionMos> connections)
    {
        Map<String, List<String>> linesStToJson = new TreeMap<>();
        ArrayList<LineForJson> linesForJson = new ArrayList<>();
        lines.forEach(line -> {
            ArrayList<Station> stations = line.getStations();
            ArrayList<String> stationNames = new ArrayList<>();
            stations.forEach(station -> stationNames.add(station.getName()));
            linesStToJson.put(line.getNumber(), stationNames);
            linesForJson.add(new LineForJson(line.getName(),line.getNumber(),line.getColor().toString()));
        });


        Metro jsonMap = new Metro();
        jsonMap.setStations(linesStToJson);
        jsonMap.setLines(linesForJson);

        String jsonFile =
                new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().
                        create().toJson(jsonMap);
        writeToJson(jsonFile);

    }
    private static void writeToJson (String map)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTFILE, false));
            writer.append(map);
            writer.close();
        } catch (Exception e) {
             MARKLOGGER.info(INVALID_FILE,"/writeToJson/ Path is invalid or FS error: {}",e.getMessage());
        }
    }

    private static void countFromJson () {
//    {
//       try {
//           Gson mosmetro = new Gson();
//
//           String test = mosmetro.fromJson(new FileReader(OUTFILE), String.class);
//           System.out.println(test);
//
//           //Object object = Gson.fromJson(new FileReader("C:\\fileName.json"), Object.class);
//          // String met = (Path.of(OUTFILE));
////           Metro mos = mosmetro.fromJson(new FileReader(OUTFILE), Metro.class);
////           Map<Double, List<String>> lines = mos.getStations();
////           for (Map.Entry st : lines.entrySet()) {
////               System.out.println(st.getKey() + "/" + st.getValue());
////
////           }
//
//       } catch (Exception ex) {
//           ex.printStackTrace();
//       }

//        JSONParser parser = new JSONParser();
//        try {
//            JSONObject subway = (JSONObject) parser.parse(new FileReader(OUTFILE));
//            JSONObject stations = (JSONObject) subway.get("stations");
//            for(Iterator iterator = stations.keySet().iterator(); iterator.hasNext();) {
//                String key = (String) iterator.next();
//                JSONArray jsonArray = (JSONArray) stations.get(key);
//                System.out.println("Line: " + key + " Number of Stations: " + jsonArray.size());
//            }
//        } catch (Exception e) {
//            MARKLOGGER.info(INVALID_FILE, "/countFromJson/ File invalid/does not exist or FS error: {}", e.getMessage());
//        }
    }

}
