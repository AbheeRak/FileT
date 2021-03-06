import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GreetClient {

    private static Socket socket1;
    private static Socket socket2;
    private static String SERVER_IP = "127.0.0.1";
    private static int SERVER_NPORT = 9090;
    private static int SERVER_TPORT = 9080;

    private static BufferedReader input;
    private static BufferedReader keyboardinput;
    private static PrintWriter out1;
    private static PrintWriter out2;

    public static void main(String[] args) throws IOException {
        socket1 = new Socket(SERVER_IP, SERVER_NPORT);
        socket2 = new Socket(SERVER_IP, SERVER_TPORT);

        input = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        keyboardinput = new BufferedReader(new InputStreamReader(System.in));
        out1 = new PrintWriter(socket1.getOutputStream(),true);
        out2 = new PrintWriter(socket2.getOutputStream(),true);


        while(true) {
            System.out.println(">");
            String command = keyboardinput.readLine();
            if (command.equals("quit")) break;
            out1.println(command);

            String serverResponse = input.readLine();
            System.out.println("Server says:" + serverResponse);
        }
            socket1.close();
            System.exit(0);
    }
    
}
