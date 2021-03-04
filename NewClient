import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NewClient {

    private static Socket socket;
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_PORT = 9090;
    private static BufferedReader input;
    private static BufferedReader keyboardinput;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        socket = new Socket(SERVER_IP, SERVER_PORT);

        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        keyboardinput = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(socket.getOutputStream(),true);

        while(true) {
            System.out.println(">");
            String command = keyboardinput.readLine();
            if (command.equals("quit")) break;
            out.println(command);

            String serverResponse = input.readLine();
            System.out.println("Server says:" + serverResponse);
        }
            socket.close();
            System.exit(0);
        }

    }
