import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {
        EchoServer.bindToPort(8788).run();
    }
}