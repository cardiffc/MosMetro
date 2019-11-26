
import java.util.ArrayList;
import java.util.Map;

public class Main {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
    public static void main(String[] args) {

        Parser metroParser = new Parser(url);
        ArrayList<Station> stations = metroParser.parseStations();
        ArrayList<Line> lines = metroParser.parseLines(stations);
        ArrayList<ConnectionMos> connections = metroParser.parseConnections(url,stations);
        connections.forEach(connection -> {
            System.out.println("#####################################################");
            for (Map.Entry<String, String> con : connection.getConnection().entrySet())
            {

                System.out.println(con.getKey() + "/" + con.getValue());
            }
        });
    }
}
