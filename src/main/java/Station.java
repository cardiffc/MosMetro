public class Station {
    private String name;
    private String line;
    private String lineColor;

    public Station(String name, String line, String lineColor) {
        this.name = name;
        this.line = line;
        this.lineColor = lineColor;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public String getLineColor() {return lineColor;}

}
