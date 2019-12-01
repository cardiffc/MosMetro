
import Jsons.LineForJson;

import java.util.*;


public class Metro {

    private Map<Double, List<String>> stations = new TreeMap<>();
    private List<LineForJson> lines = new ArrayList<>();
    private Object[] connections;
    private ArrayList<String> test;

    public void setTest(ArrayList<String> test) {
        this.test = test;
    }

    public ArrayList<String> getTest() {
        return test;
    }




    public void setConnections(Object[] connections) {
        this.connections = connections;
    }

    public Object[] getConnections() {
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
