import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class Line implements Comparable<Line> {
    private String number;
    private String name;
    AtomicReference<String> color;
    private ArrayList<Station> stations;

    public Line(String number, String name, AtomicReference<String> color, ArrayList<Station> stations)
    {
        this.number = number;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public AtomicReference<String> getColor() {
        return color;
    }

    public ArrayList<Station> getStations() {return stations;}

//    public void setColor(String color) {
//        this.color = color;
//    }

    @Override
    public int compareTo(Line o) {
        if (Double.parseDouble(o.getNumber().replaceAll("А",".5"))
                > Double.parseDouble(this.getNumber().replaceAll("А",".5")))
        {
            return -1;
        }
        if (Double.parseDouble(o.getNumber().replaceAll("А",".5"))
                < Double.parseDouble(this.getNumber().replaceAll("А",".5")))
        {
            return 1;
        }
        return 0;
    }

//    @Override
//    public int compareTo(Object o) {
//        return 0;
//    }
}
