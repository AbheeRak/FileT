import java.io.*;
import java.net.*;


public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket, BufferedReader in, PrintWriter out) throws IOException {
        this.client = clientSocket;
        this.in = in; //= new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.out = out; //= new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
        CommandHandler minions = null;
        try {
            minions = new CommandHandler(client);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread masterthread = new Thread(minions);
        System.out.println("Starting a minion thread");
        masterthread.start();
        
    }
}
