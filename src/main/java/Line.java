import java.util.ArrayList;

public class Line {
    private String number;
    private String name;
    private String color;
    private ArrayList<Station> stations;

    public Line(String number, String name, String color,ArrayList<Station> stations)
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

    public String getColor() {
        return color;
    }

    public ArrayList<Station> getStations() {return stations;}
}
