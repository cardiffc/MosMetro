import java.util.ArrayList;

public class Main {

    private static String url = "https://ru.wikipedia.org/wiki/%D0%A1%D0%BF%D0%B8%D1%81%D0%BE%D0%BA_%D1%81%D1%82%D0%B0%D0%BD%D1%86%D0" +
            "%B8%D0%B9_%D0%9C%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%BE%D0%B3%D0%BE_%D0%BC%D0%B5%D1%82%D1%" +
            "80%D0%BE%D0%BF%D0%BE%D0%BB%D0%B8%D1%82%D0%B5%D0%BD%D0%B0";
    public static void main(String[] args) {

                Parser metroParser = new Parser(url);
                ArrayList<Station> stations = metroParser.parseStations();
          //      stations.stream().filter(station -> station.getLine().equals("14")).forEach(l -> System.out.println(l.getName()));
           //     stations.forEach(station -> System.out.println(station.getLine() + "/" + station.getName()));
        //stations.forEach(station -> System.out.println(station.getLine() + "/" + station.getName()));
     //   System.out.println(stations.size());
      ArrayList<Line> lines = metroParser.parseLines();
      lines.forEach(line -> System.out.println(line.getNumber() + "/" + line.getColor() + "/" + line.getName()));

    }
}
