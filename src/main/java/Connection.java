import java.util.TreeMap;

public class Connection {


    private TreeMap<String, String> connection;
    public Connection(TreeMap<String, String> connection) {
        this.connection = connection;
    }

    public TreeMap<String, String> getConnection() {
        return connection;
    }
}
