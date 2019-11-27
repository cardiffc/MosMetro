import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class Parser {
    private String url;
    private static final Logger MARKLOGGER = LogManager.getLogger(Parser.class);
    private static final Marker URL_ERROR = MarkerManager.getMarker("URL_ERROR");
    private static final Marker INVALID_STRING = MarkerManager.getMarker("INVALID_STRING");
    private static final String STRINGTOESC = "14 Московское центральное кольцо";
    public Parser(String url) {
        this.url = url;
    }

    public Object[] parseSubway()
    {
        Document mosMetroWeb = connectToUrl(url);
        ArrayList<Line> parsedLines = new ArrayList<>();
        TreeMap<String,String> lines = new TreeMap<>();
        ArrayList<Station> stations = new ArrayList();
        for (int i = 3; i <= 5 ; i++) {
            ArrayList<Element> allSubway = mosMetroWeb.select("table").get(i).select("tr");
            allSubway.forEach(station -> {
                String lineColor = "undefined";
                if (station.text().matches("^\\d{1}.+$") && !station.text().equals(STRINGTOESC)  ) {
                    String stationName = station.select("td").get(1).select("a").first().text();
                    String lineNumber = station.select("td").get(0).select("span").first().text();
                    String lineName = station.select("td").get(0).select("span").get(1).attr("title");
                    lineName = lineName.replaceAll(" линия", "");
                    String colorCode = station.select("td").get(0).attr("style");
                    if (colorCode.matches("^(background:)(#.{6})(.+)?")) {
                        lineColor = colorCode.substring(colorCode.indexOf(":") + 1, colorCode.indexOf(":") + 8);
                    }
                    lines.put(lineNumber,lineName);
                    stations.add(new Station(stationName, lineNumber, lineColor));
                } else {
                    MARKLOGGER.info(INVALID_STRING, "/parseStations/ String is invalid and not parsed: {}", station.text());
                }
            });
        }
        for (Map.Entry line : lines.entrySet()) {
            ArrayList<Station> stationsOnLine = new ArrayList<>();
            AtomicReference<String> lineColor = new AtomicReference<>("");
            stations.stream().filter(station -> station.getLine().equals(line.getKey()))
                    .forEach(stationOnLine -> {
                        stationsOnLine.add(stationOnLine);
                        lineColor.set(stationOnLine.getLineColor());
                        });
            parsedLines.add(new Line(line.getKey().toString(),line.getValue().toString(),lineColor,stationsOnLine));
        }
        Object[] parsedSubway = new Object[] {stations,parsedLines};
        return parsedSubway;
    }

    public ArrayList<ConnectionMos> parseConnections (String url, ArrayList<Station> stations)
    {
        Document mosMetroWeb = connectToUrl(url);
        ArrayList<Element> tableForConnections = mosMetroWeb.select("table").get(3).select("tr");
        ArrayList<ConnectionMos> connections = new ArrayList<>();
        for (int i = 1; i <= tableForConnections.size() - 1; i++) {
            Element columnWithConnection =  tableForConnections.get(i).select("td").get(3);
            if (!columnWithConnection.text().isEmpty())
            {
                String lineFrom = tableForConnections.get(i).select("td").get(0).select("span").first().text();
                String stationFrom = tableForConnections.get(i).select("td").get(1).select("a").first().text();
                String stationTo = "";
                String lineTo = "";
                ArrayList<Element> linesTo = columnWithConnection.select("span");
                TreeMap<String, String> connection = new TreeMap<>();
                connection.put(lineFrom,stationFrom);
                for (int j = 0; j < linesTo.size() ; j += 2) {
                    if (j % 2 == 0) {
                        lineTo = linesTo.get(j).text();
                        String conText = linesTo.get(j + 1).select("a").attr("title");
                        for (Station st : stations)
                        {
                            if (st.getLine().equals(lineTo)) {
                                stationTo = (conText.contains(st.getName())) ? st.getName() : stationTo;
                            }
                        }
                    }
                    connection.put(lineTo,stationTo);
                }
                connections.add(new ConnectionMos(connection));
            }
        }
        connections = deleteDuplicateConnections(connections);
        return connections;
    }

    private Document connectToUrl(String url)
    {
        try {
            Document mosMetroWeb = Jsoup.connect(url).maxBodySize(0).get();
            return mosMetroWeb;
        } catch (Exception ex) {
            MARKLOGGER.info(URL_ERROR,"URL Error {}", ex.getMessage());
        }
        return null;
    }
    private ArrayList<ConnectionMos> deleteDuplicateConnections (ArrayList<ConnectionMos> connections)
    {
        for (int i = 0; i < connections.size() ; i++) {
            TreeMap<String, String> tempConnection = connections.get(i).getConnection();
            for (int j = 0; j < connections.size() ; j++) {
                TreeMap<String, String> toDelete = connections.get(j).getConnection();
                if (i != j && tempConnection.equals(toDelete)) {
                    connections.remove(i);
                }
            }
        }
        return connections;
    }
}



