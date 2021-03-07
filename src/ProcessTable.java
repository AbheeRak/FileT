import java.io.*;
import java.net.*;
import java.util.HashMap;

public class ProcessTable {
    
    private static HashMap<Long,String> idTable; // used to store process ids of threads
    
//private static ArrayList<ClientHandler> clients = new ArrayList<>();
//        private static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws IOException {
        serverSocket1 = new ServerSocket(NPORT);
        serverSocket2 = new ServerSocket(TPORT);
        idTable = new HashMap<Long,String>(300);
        
        
        while(true) {
            System.out.println("Server waiting for connection....");
            clientSocket1 = serverSocket1.accept();
            clientSocket2 = serverSocket2.accept();
            
            System.out.println("Server Connected");
            ClientHandler clientThread1 = new ClientHandler(clientSocket1);
            ClientHandler2 clientThread2 = new ClientHandler2(clientSocket2);
            //Minion cRunnable = new Minion(clientSocket1);
            Thread t1 = new Thread(clientThread1);
            //clients.add(cRunnable);
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
