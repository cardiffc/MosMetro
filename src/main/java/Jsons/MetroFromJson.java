package Jsons;

import Jsons.LineForJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MetroFromJson {
    private Map<String, List<String>> stations = new TreeMap<>();
    private List<LineForJson> lines = new ArrayList<>();
    private Object[] connections;


    public void setConnections(Object[] connections) {
        this.connections = connections;
    }

    public Object[] getConnections() {
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
