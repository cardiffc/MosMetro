import Jsons.LineForJson;
import Jsons.MetroFromJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import java.io.*;
import java.util.*;

public class Main {
    private static final Logger MARKLOGGER = LogManager.getLogger(Main.class);
    private static final Marker INVALID_FILE = MarkerManager.getMarker("INVALID_FILE");

    private static final String URL = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";

    private static final String OUTFILE = "src/main/resources/mosmetromap.json";

    public static void main(String[] args) {

        Parser metroParser = new Parser(URL);
        Object[] subway = metroParser.parseSubway();
        ArrayList<Line> lines = (ArrayList<Line>) subway[1];
        ArrayList<Station> stations = (ArrayList<Station>) subway[0];
        ArrayList<ConnectionMos> connections = metroParser.parseConnections(URL, stations);
        Collections.sort(lines);
        Collections.sort(stations);
        subwayToJson(lines, connections);
        countFromJson();
    }
    private static void subwayToJson(ArrayList<Line> lines, ArrayList<ConnectionMos> connections) {

        // Тут бы добавляем в JSON станции и линии
        Map<Double, List<String>> linesStToJson = new TreeMap<>();
        ArrayList<LineForJson> linesForJson = new ArrayList<>();
        lines.forEach(line -> {
            ArrayList<Station> stations = line.getStations();
            ArrayList<String> stationNames = new ArrayList<>();
            stations.forEach(station -> stationNames.add(station.getName()));
            linesStToJson.put(Double.parseDouble(line.getNumber().replace("А", ".5")), stationNames);
            linesForJson.add(new LineForJson(line.getName(), line.getNumber(), line.getColor().toString()));
        });

        // А вот тут, таким вот хитро...вывернутым способом connections. Ибо в самом JSON должен быть массив массивов объектов
        ArrayList<ArrayList<TreeMap<String,String>>> connectionsToJson = new ArrayList<>();
        for (int i = 0; i < connections.size() ; i++) {
            ArrayList<TreeMap<String,String>> thisConnection = new ArrayList<>();
            TreeMap<String,String> newConnection = connections.get(i).getConnection();
            for (Map.Entry connection : newConnection.entrySet())
            {
                TreeMap<String,String> station = new TreeMap<>();
                station.put("line",connection.getKey().toString());
                station.put("station",connection.getValue().toString());
                thisConnection.add(station);
            }
            connectionsToJson.add(thisConnection);
        }

        // Создаем новый объект класса метро и инициализируем его переменные.
        Metro jsonMap = new Metro();
        jsonMap.setStations(linesStToJson);
        jsonMap.setLines(linesForJson);
        jsonMap.setConnections(connectionsToJson);

        //Формируем JSON и передаем специальному методу на вывод.
        String jsonFile =
                new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().
                        create().toJson(jsonMap);
        writeToJson(jsonFile.replace(".5", "A").replace(".0", ""));

    }

    private static void writeToJson(String map) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTFILE, false));
            writer.append(map);
            writer.close();
        } catch (Exception e) {
            MARKLOGGER.info(INVALID_FILE, "/writeToJson/ Path is invalid or FS error: {}", e.getMessage());
        }
    }

    private static void countFromJson() {
        {
            try {
                Gson mosmetro = new Gson();
                MetroFromJson mos = mosmetro.fromJson(new FileReader(OUTFILE), MetroFromJson.class);
                Map<String, List<String>> lines = mos.getStations();
                for (Map.Entry st : lines.entrySet()) {
                   String number = st.getKey().toString();
                   number = (number.length() == 1 || number.equals("11A")) ? 0 + number : number;
                   String[] stations = st.getValue().toString().split(",");
                   System.out.println("Line number: " + number + " Stations: " + stations.length);
                }

            } catch (Exception ex) {
                MARKLOGGER.info(INVALID_FILE, "/countFromJson/ Path is invalid or FS error: {}", ex.getMessage());
            }
        }

    }
}
