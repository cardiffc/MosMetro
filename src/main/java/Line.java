import java.util.ArrayList;

public class Line {
    private int number;
    private String name;
    private ArrayList<Station> stations;
    private String color;

    public Line(int number, String name, String color, ArrayList<Station> stations)
    {
        this.number = number;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public String getColor() {
        return color;
    }





}
