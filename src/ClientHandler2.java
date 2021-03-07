import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientHandler2 implements Runnable{
    private Socket client2;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler2(Socket clientSocket2) throws IOException {
        this.client2 = clientSocket2;
        in = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        // 1912
        out = new PrintWriter(client2.getOutputStream(),true);
    }
    @Override
    public void run() {
//        try {

//            while (true) {

        CommandHandler2 minions = null;
        try {
            minions = new CommandHandler2(client2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //if(in contains terminate) {
        Thread masterthread2 = new Thread(minions);
        System.out.println("Starting a minion thread");
//        clients.add(masterthread.getId());
        masterthread2.start();

//            }
//        }
//        catch (IOException e){
//            System.err.println("IO Exception handler");
//
//        }finally{
//            out.close();
//            try {
//                in.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
    }

}
