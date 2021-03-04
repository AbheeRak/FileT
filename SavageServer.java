import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SavageServer {
        private static ServerSocket serverSocket;
        private static Socket clientSocket;
        private static BufferedReader in;
        private static PrintWriter out;
        private static int PORT = 9090;

        private static ArrayList<ClientHandler> clients = new ArrayList<>();
        private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(PORT);

        while(true) {
            System.out.println("waiting for connection....");
            clientSocket = serverSocket.accept();

            System.out.println("Connected");
            ClientHandler clientThread = new ClientHandler(clientSocket);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
    }



}
