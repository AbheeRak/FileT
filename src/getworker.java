import java.net.*;
import java.io.*;
import java.util.*;
public class getworker implements Runnable{

    private static BufferedReader input;
    private String secondHalf;
    private static Socket client;
//    private static PrintWriter fTest;
//    private static ProcessBuilder build;
    private static BufferedReader clientInput;
    private static long size;

    public getworker (Socket client, String secondHalf, long size) throws IOException {
        this.client = client;
        this.secondHalf = secondHalf;
        this.clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.size = size;

    }

    @Override
    public void run() {

        File test = new File(secondHalf);
        int input = 0;
        PrintWriter fTest = null;
        try {
            fTest = new PrintWriter(new BufferedWriter(new FileWriter(test)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (input != -1) { // checks for end of file
            try {
                input = clientInput.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (input != -1) {
                fTest.write(input);
            }
            i++;
            if (i >= size) { // breaks if all bytes are read
                break;
            }
        }
        fTest.flush();
        try {
            test.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fTest.close();
        try {
            clientInput.readLine(); // reads a blank character after file is got and prevents output of it
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
