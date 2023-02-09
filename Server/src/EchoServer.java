import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            try (var clientSocket = server.accept()) {
                handle(clientSocket);

            }
        } catch (IOException e) {
            String fmtMsg = "Вероятно всего порт %s занят. %n";
            System.out.printf(fmtMsg, port);
            e.printStackTrace();
        }
    }

    private void handle(Socket socket) throws IOException {
        var input = socket.getInputStream();
        var isr = new InputStreamReader(input, "UTF-8");
        var scanner = new Scanner(isr);

        try (scanner) {
            while (true) {
                var message = scanner.nextLine().strip();
                System.out.printf("Ответ: %s%n", message);
                if ("Пока".equalsIgnoreCase(message)) {
                    System.out.printf("Пока пока! %n");
                    PrintWriter writerBye = new PrintWriter(socket.getOutputStream());
                    writerBye.write("Пока пока!");
                    writerBye.write(System.lineSeparator());
                    writerBye.flush();
                    return;
                }
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                var rever = new StringBuilder(message).reverse().toString();
                writer.write(rever);
                writer.write(System.lineSeparator());
                System.out.printf("Возврат: %s\n", rever);
                writer.flush();
                System.out.print("Работа завершена\n");
            }

        } catch (NoSuchElementException e) {
            System.out.printf("Соединение с клиентом потеряно!%n");
        }
    }

}

