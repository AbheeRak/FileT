import java.io.*;
import java.net.*;


public class ClientHandler implements Runnable{
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.client = clientSocket;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
        try {

            while (true) {

                CommandHandler minions = new CommandHandler(client);

                Thread masterthread = new Thread(minions);
                System.out.println("Starting a minion thread");
                masterthread.start();

            }
        }
        catch (IOException e){
            System.err.println("IO Exception handler");

        }finally{
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
