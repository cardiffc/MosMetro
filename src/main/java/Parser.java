import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.ArrayList;

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
        try {
            ArrayList<Station> stations = new ArrayList();
            Document mosMetroWeb = Jsoup.connect(url).maxBodySize(0).get();
            ArrayList<Element> tableSubway = mosMetroWeb.select("table").get(3).select("tr");
            tableSubway.forEach(station -> {
                if (station.text().matches("^\\d{1}.+")) {
                    String name = station.select("td").get(1).select("a").first().text();
                    String line = station.select("td").get(0).text().substring(0, 2);
                    stations.add(new Station(name, line));
                } else {
                    MARKLOGGER.info(INVALID_STRING,"String is invalid and not parsed: {}",station.text());
                }
            });
            ArrayList<Element> tableMck = mosMetroWeb.select("table").get(5).select("tr");
            tableMck.forEach(station -> {
                if (station.text().matches("^\\d{4}.+$")) {
                    String name = station.select("td").get(1).select("a").first().text();
                    String line = station.select("td").get(0).text().substring(0, 2);
                    stations.add(new Station(name, line));
                } else {
                    MARKLOGGER.info(INVALID_STRING,"String is invalid and not parsed: {}",station.text());
                }
            });
            return stations;
        } catch (Exception ex) {
            MARKLOGGER.info(URL_ERROR,"URL Error {}", ex.getMessage());
        }
        return null;
    }
    public ArrayList<Line> parseLines()
    {
        return null;
    }




}
