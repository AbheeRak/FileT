import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.util.Iterator;
import java.util.Map;


public class ClientHandler2 implements Runnable {
    private Socket client2;
    private BufferedReader in;
    private PrintWriter output;

    public ClientHandler2(Socket clientSocket2) throws IOException {
        this.client2 = clientSocket2;
        in = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        // 1912
        output = new PrintWriter(client2.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            System.out.println("Existing hashmap is" + myftpserver.idTable);

            //Iterator idTableIterator = myftpserver.idTable.entrySet().iterator();
            boolean check = true;
            while (check) {
            //while (idTableIterator.hasNext()) {
            // Map.Entry mapElement = (Map.Entry) idTableIterator.next();
                String value = in.readLine();
                if (value != null && value.equals("quit")) {
                    check = false;
                    // close current client connections
                    in.close();
                    output.close();
                    client2.close();
                } else {
                    Long id = Long.parseLong(value);
                    System.out.println("id table : " + myftpserver.idTable);
                    if (myftpserver.idTable.get(id) != null) {
                        myftpserver.idTable.put(id, "Terminate");
                        //output.println("Terminated Process ID: " + id);
                        output.println("id table : " + myftpserver.idTable);
                    }
                }
            }
        } catch (IOException e) {
        }
        
    }

}
