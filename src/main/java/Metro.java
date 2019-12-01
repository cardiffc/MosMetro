import java.util.*;


public class Metro {

    private Map<String, List<String>> stations = new TreeMap<>();
    private List<LineForJson> lines = new ArrayList<>();
    private List<ConnectionMos> connections = new ArrayList<>();


    public void setConnections(List<ConnectionMos> connections) {
        this.connections = connections;
    }

    public List<ConnectionMos> getConnections() {
        return connections;
    }



    public Map<String, List<String>> getStations() {
        return stations;
    }

    public void setStations(Map<String, List<String>> stations) {
        this.stations = stations;
    }

    public List<LineForJson> getLines() {
        return lines;
    }

    public void setLines(List<LineForJson> lines) {
        this.lines = lines;
    }
}
