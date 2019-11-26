import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Parser {
    private String url;
    private static final Logger MARKLOGGER = LogManager.getLogger(Parser.class);
    private static final Marker URL_ERROR = MarkerManager.getMarker("URL_ERROR");
    private static final Marker INVALID_STRING = MarkerManager.getMarker("INVALID_STRING");
    public Parser(String url) {
        this.url = url;
    }

    public ArrayList<Station> parseStations()
    {
        ArrayList<Station> stations = new ArrayList();
        Document mosMetroWeb = connectToUrl(url);
        for (int i = 3; i <= 5 ; i++) {
            ArrayList<Element> allSubway = mosMetroWeb.select("table").get(i).select("tr");
            allSubway.forEach(station -> {
                if (station.text().matches("^\\d{1}.+$") && !station.text().equals("14 Московское центральное кольцо")  ) {
                    String name = station.select("td").get(1).select("a").first().text();
                    String line = station.select("td").get(0).select("span").first().text();
                    stations.add(new Station(name, line));
                } else {
                    MARKLOGGER.info(INVALID_STRING, "/parseStations/ String is invalid and not parsed: {}", station.text());
                }
            });
        }
        return stations;
    }

    public ArrayList<Line> parseLines (ArrayList<Station> stations)
    {
        Document mosMetroWeb = connectToUrl(url);
        ArrayList<Line> parsedLines = new ArrayList<>();
        ArrayList<Element> tableLines = mosMetroWeb.select("table").get(8).select("tr");

        ArrayList<Element> lName = tableLines.get(1).select("td").select("a");
        lName.add(tableLines.get(4).select("td").select("a").last());

        ArrayList<Element> lNum = tableLines.get(1).select("td").select("span");
        lNum.add(tableLines.get(4).selectFirst("td").select("span").first());

        lName = (ArrayList<Element>) lName.stream().filter(name -> !name.text().equals("")).collect(Collectors.toList());
        lNum = (ArrayList<Element>) lNum.stream().filter(num -> !num.text().equals("")).collect(Collectors.toList());

        for (int i = 0; i < lName.size() ; i++) {
            String lineName = lName.get(i).text();
            String lineNumber = lNum.get(i).text();
            String lineColor = "undefined";

            //Подумать как убрать вот эту ебанину!!!!
            for (int j = 3; j <= 5 ; j++) {
                ArrayList<Element> tableForColors = mosMetroWeb.select("table").get(j).select("tr");
                for (int k = 0; k < tableForColors.size(); k++) {
                    if (tableForColors.get(k).text().matches("^\\d{1}.+") && !tableForColors.get(k).text().equals("14 Московское центральное кольцо")) {
                        String lineNumber2 = tableForColors.get(k).select("td").get(0).select("span").first().text();
                        String colorCode = tableForColors.get(k).select("td").get(0).attr("style").replaceAll(" ", "")
                                .replaceAll("background-color:", "");
                        if (colorCode.matches("^(background:)(#.{6})(.+)?") && lineNumber.equals(lineNumber2)) {
                            lineColor = colorCode.substring(colorCode.indexOf(":") + 1, colorCode.indexOf(":") + 8);
                        }
                    }
                }
            }
            ArrayList<Station> stationsOnLine = (ArrayList<Station>) stations.stream()
                    .filter(station -> station.getLine().equals(lineNumber)).collect(Collectors.toList());
            parsedLines.add(new Line(lineNumber,lineName,lineColor,stationsOnLine));
        }
        return parsedLines;
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
                String conText = "";
                String stationTo = "";
                String lineTo = "";
                ArrayList<Element> linesTo = columnWithConnection.select("span");
                TreeMap<String, String> connection = new TreeMap<>();
                connection.put(lineFrom,stationFrom);
                for (int j = 0; j < linesTo.size() ; j += 2) {
                    if (j % 2 == 0) {
                        lineTo = linesTo.get(j).text();
                        conText = linesTo.get(j + 1).select("a").attr("title");
                        for (Station st : stations)
                        {
                            if (st.getLine().equals(lineTo))
                            {
                                if (conText.contains(st.getName())) {
                                    stationTo = st.getName();
                                }
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
                if (i != j && tempConnection.equals(toDelete))
                {
                    connections.remove(i);
                }
            }
        }
        return connections;
    }
}



