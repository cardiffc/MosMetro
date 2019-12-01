
import Jsons.LineForJson;

import java.util.*;


public class Metro {

    private Map<Double, List<String>> stations = new TreeMap<>();
    private List<LineForJson> lines = new ArrayList<>();
    private ArrayList<ArrayList<TreeMap<String,String>>> connections;

    public void setConnections(ArrayList<ArrayList<TreeMap<String,String>>> connections) {
        this.connections = connections;
    }

    public ArrayList<ArrayList<TreeMap<String,String>>> getConnections() {
        return connections;
    }

    public Map<Double, List<String>> getStations() {
        return stations;
    }

    public void setStations(Map<Double, List<String>> stations) {
        this.stations = stations;
    }

    public List<LineForJson> getLines() {
        return lines;
    }

    public void setLines(List<LineForJson> lines) {
        this.lines = lines;
    }
}
