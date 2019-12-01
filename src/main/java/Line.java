import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
public class Line implements Comparable<Line> {
    private String number;
    private String name;
    private AtomicReference<String> color;
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

    @Override
    public int compareTo(Line o) {
        Double curNumber = Double.parseDouble(this.number.replace("А",".5"));
        Double newNumber = Double.parseDouble(o.getNumber().replace("А",".5"));
        return curNumber.compareTo(newNumber);
    }
}
