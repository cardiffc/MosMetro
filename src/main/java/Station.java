public class Station implements Comparable<Station> {
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

    @Override
    public int compareTo(Station o) {
        Double curNumber = Double.parseDouble(line.replace("А",".5"));
        Double newNumber = Double.parseDouble(o.getLine().replace("А",".5"));
        return curNumber.compareTo(newNumber);
    }
}
