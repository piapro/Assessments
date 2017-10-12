import org.kohsuke.args4j.Option;


public class CmdLineArgs {

    @Option(required = true, name = "-h", aliases = {"--host"}, usage = "Hostname")
    private String host;

    @Option(required = false, name = "-p", usage = "Port number")
    private int port = 3939;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}