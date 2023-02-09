import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private String host;
    private int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        var localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.println("Напишите 'Пока' чтобы выйти ");
        System.out.println("Напишите ваше сообщение серверу.");
        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, "UTF-8");
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            try (scanner; writer) {
                while (true) {
                    String message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();

                    var isr = new InputStreamReader(socket.getInputStream());
                    Scanner scan = new Scanner(isr);
                    var serverDate = scan.nextLine();
                    System.out.println("Ответ от Сервера\n" + serverDate + "\n");
                    if ("Пока".equalsIgnoreCase(message)) {
                        return;
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.printf("Соединение потеряно!%n");
            }
        } catch (IOException e) {
            System.out.printf("Не могу подключиться %s:%s!%n", host, port);
            e.printStackTrace();
        }
    }
}
