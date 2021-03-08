import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;


public class ClientHandler2 implements Runnable {
    private Socket client2;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler2(Socket clientSocket2) throws IOException {
        this.client2 = clientSocket2;
        in = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        // 1912
        out = new PrintWriter(client2.getOutputStream(), true);
    }

    @Override
    public void run() {

            System.out.println("Existing hashmap is" + myftpserver.idTable);

            Iterator idTableIterator = myftpserver.idTable.entrySet().iterator();


            while (idTableIterator.hasNext()) {
                Map.Entry mapElement = (Map.Entry) idTableIterator.next();
                if (mapElement.getKey() == in) {
                    myftpserver.idTable.put(mapElement.getKey(), "Deactive");
                    System.out.println("Updated entry :" + mapElement);
                }

            }
        }

    }


