import java.util.TreeMap;

public class ConnectionMos {


    private TreeMap<String, String> connection;
    public ConnectionMos(TreeMap<String, String> connection) {
        this.connection = connection;
    }

    public TreeMap<String, String> getConnection() {
        return connection;
    }
}
