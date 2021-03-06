import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class myftpserver {
    private static ServerSocket serverSocket1;
    private static ServerSocket serverSocket2;
    private static Socket clientSocket1;
    private static Socket clientSocket2;

    private static BufferedReader in;
    private static PrintWriter out;
    private static int NPORT = 9090;
    private static int TPORT = 9080;

    private static ArrayList<Minion> clients = new ArrayList<>();
//        private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        serverSocket1 = new ServerSocket(NPORT);
        serverSocket2 = new ServerSocket(TPORT);


        
        while(true) {
            System.out.println("Server waiting for connection....");
            clientSocket1 = serverSocket1.accept();
            //clientSocket2 = serverSocket2.accept();
            
            System.out.println("Server Connected");
            //ClientHandler clientThread1 = new ClientHandler(clientSocket1);
            //ClientHandler clientThread2 = new ClientHandler(clientSocket2);
            Minion cRunnable = new Minion(clientSocket1);
            Thread c1 = new Thread(cRunnable);
            clients.add(cRunnable);
            /*Thread t1 = new Thread(clientThread1);
              Thread t2 = new Thread(clientThread2);*/
            c1.start();
                
            /*clients.add(clientThread1);
              clients.add(clientThread2);
            t1.start();
            t2.start();*/
//            pool.execute(clientThread);
        }
    }



}
