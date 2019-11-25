
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeSet;

public class Main {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
    public static void main(String[] args) {

        Parser metroParser = new Parser(url);
        ArrayList<Station> stations = metroParser.parseStations();


      //  stations.forEach(station -> System.out.println(station.getLine() + "/" + station.getName()));
        //System.out.println(stations.size());
        ArrayList<Line> lines = metroParser.parseLines(stations);

        ArrayList<Connection> connections = metroParser.parseConnections(url,stations);

        for (Map.Entry<String, String> con : connections.get(6).getConnection().entrySet())
        {
            System.out.println(con.getKey() + "/" + con.getValue());
        }





        //metroParser.parseConnections(url, stations);
      //  lines.forEach(line -> System.out.println(line.getName()));

//        lines.stream().filter(line -> line.getNumber().equals("011А")).forEach(line -> line.getStations().forEach(station -> System.out.println(station.getName())));

      //  lines.forEach(line -> System.out.println(line.getName()));
        //System.out.println(lines.size());
        // stations.stream().filter(station -> station.getLine().equals("13")).forEach(station -> System.out.println(station.getName()));
        // lines.stream().filter(line -> line.getNumber().equals("13")).forEach(line -> System.out.println(line.getName()));
       // lines.forEach(line -> System.out.println(line.getNumber() + "/" + line.getColor() + "/" + line.getName()));

    }
}
