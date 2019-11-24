import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        ArrayList<Element> tableSubway = mosMetroWeb.select("table").get(3).select("tr");

        tableSubway.forEach(station -> {
            if (station.text().matches("^\\d{1}.+")) {
                String name = station.select("td").get(1).select("a").first().text();
                String line = station.select("td").get(0).select("span").first().text();;
                stations.add(new Station(name, line));
            } else {
                MARKLOGGER.info(INVALID_STRING,"/parseStations/ String is invalid and not parsed: {}",station.text());
            }
        });
        ArrayList<Element> tableMck = mosMetroWeb.select("table").get(5).select("tr");
        tableMck.forEach(station -> {
            if (station.text().matches("^\\d{4}.+$")) {

                String name = station.select("td").get(1).select("a").first().text();
                String line = station.select("td").get(0).select("span").first().text();
                stations.add(new Station(name, line));
            } else {
                MARKLOGGER.info(INVALID_STRING,"/parseStations/ String is invalid and not parsed: {}",station.text());
            }
        });
        return stations;
    }
    public ArrayList<Line> parseLines ()
    {
        ArrayList<Line> parsedLines = new ArrayList<>();
        Document mosMetroWeb = connectToUrl(url);
        ArrayList<Element> tableLines = mosMetroWeb.select("table").get(8).select("tr");
        Pattern lineChecker = Pattern.compile("(?<number>.{2,4})\\s(?<name>.+)");
        ArrayList<Element> lines = tableLines.get(1).select("td").select("dd");
        Element kLine = tableLines.get(4).select("td").first();
        lines.forEach(line ->{
            Matcher checkAndTake = lineChecker.matcher(line.text());
            if (checkAndTake.find()) {
                String lineNumber = checkAndTake.group("number");
                String lineName = checkAndTake.group("name");
                String lineColor = "#FFFFFF";
                ArrayList<Element> tableForColors = mosMetroWeb.select("table").get(3).select("tr");
                for (int i = 0; i < tableForColors.size() ; i++) {
                    if (tableForColors.get(i).text().matches("^\\d{1}.+")) {
                        String lineNumber2 = tableForColors.get(i).select("td").get(0).select("span").first().text();
                        String colorCode = tableForColors.get(i).select("td").get(0).attr("style").replaceAll(" ", "")
                                .replaceAll("background-color:", "");
                        if (colorCode.matches("^(background:)(#.{6})(.+)?") && lineNumber.equals(lineNumber2)){
                                lineColor = colorCode.substring(colorCode.indexOf(":") + 1, colorCode.indexOf(":") + 8);
                        } else {
                            MARKLOGGER.info(INVALID_STRING,"/parseStations/ Colors code is invalid and not parsed: {}",colorCode);
                        }
                    }
                }
                parsedLines.add(new Line(lineNumber,lineName,lineColor));
            } else {
                MARKLOGGER.info(INVALID_STRING,"/parseStations/ String is invalid and not parsed: {}",line.text());
            }
        });
        Matcher kahovskaya = lineChecker.matcher(kLine.text());
        if (kahovskaya.find()) {
            String lineNumber = kahovskaya.group("number");
            String lineName = kahovskaya.group("name");
            String lineColor = "not defined";
            parsedLines.add(new Line(lineNumber,lineName,lineColor));
        }
        return parsedLines;
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
}



