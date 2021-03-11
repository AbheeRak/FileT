import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class myftpserver {
    //private static ServerSocket serverSocket1;
    //private static ServerSocket serverSocket2;
    //private Socket clientSocket1;
    //private Socket clientSocket2;

    //private static BufferedReader in;
    //private static PrintWriter out;
    private static int NPORT = 19020;
    private static int TPORT = 19090;
    
    public static HashMap<Long,String> idTable; // used to store process ids of threads
    
//private static ArrayList<ClientHandler> clients = new ArrayList<>();
//        private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(NPORT);
        ServerSocket serverSocket2 = new ServerSocket(TPORT);
        idTable = new HashMap<Long,String>(300);
        //Socket clientSocket1 = null;
        
        while(true) {
            System.out.println("Server waiting for connection....");
            Socket clientSocket1 = serverSocket1.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket1.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket1.getOutputStream(),true);
            Socket clientSocket2 = serverSocket2.accept();
            
            System.out.println("Server Connected");
            ClientHandler clientThread1 = new ClientHandler(clientSocket1,in,out);
            ClientHandler2 clientThread2 = new ClientHandler2(clientSocket2);
            //CommandHandler clientThread1 = new CommandHandler(clientSocket1);
            Thread t1 = new Thread(clientThread1);
            Thread t2 = new Thread(clientThread2);
            //Thread t1 = new Thread(clientThread1);
            /*  Thread t2 = new Thread(clientThread2);*/
            t1.start();
            t2.start();
                
            /*clients.add(clientThread1);
              clients.add(clientThread2);
            t1.start();
            t2.start();*/
//            pool.execute(clientThread);

        }
    }
}
