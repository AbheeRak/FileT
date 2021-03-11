import java.net.*;
import java.io.*;
import java.util.*;
public class putworker implements Runnable{

    private static BufferedReader input;
    private String secondHalf;
    private static Socket client;
    //    private static PrintWriter fTest;
//    private static ProcessBuilder build;
    private static PrintWriter clientOutput;

    public putworker (Socket client, String secondHalf) throws IOException {
        this.client = client;
        this.secondHalf = secondHalf;
        this.clientOutput = new PrintWriter(client.getOutputStream(),true);

    }

    @Override
    public void run() {
        File putFile = new File(secondHalf);
        if (putFile.isFile()) {
            BufferedReader bFileInput = null;
            try {
                bFileInput = new BufferedReader(new FileReader(putFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            long fileLength = putFile.length();
            String convert = "" + fileLength + "\n";
            clientOutput.write(convert);
            String output = "";
            int value = 0;
            while (value != -1) {
                try {
                    value = bFileInput.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (value != -1) {
                    output += (char)value;
                }
            }
            try {
                bFileInput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clientOutput.println(output);
        }



    }
}
