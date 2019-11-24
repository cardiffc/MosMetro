import java.util.ArrayList;

public class Line {
    private String number;
    private String name;
    private String color;

    public Line(String number, String name, String color)
    {
        this.number = number;
        this.name = name;
        this.color = color;
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
}
