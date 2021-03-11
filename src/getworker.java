import java.net.*;
import java.io.*;
import java.util.*;
public class getworker implements Runnable {

    private BufferedReader input;
    private String secondHalf;
    private Socket client;
    //    private static PrintWriter fTest;
//    private static ProcessBuilder build;
    private BufferedReader clientInput;
    private long size;

    public getworker(Socket client, String secondHalf) throws IOException {
        this.client = client;
        this.secondHalf = secondHalf;
        this.clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.size = size;

    }

    @Override
    public void run() {
        synchronized (client) {
            
            String sizeString = null;
            try {
                	sizeString = clientInput.readLine();
            
                	if (sizeString.equals("false")) {
                		secondHalf = secondHalf.substring(1);
                		System.out.println(secondHalf + " does not exist in the current directory.");
                	} else {
                		long size = Long.parseLong(sizeString); // byte size of file
                		secondHalf = secondHalf.substring(1);


                		File test = new File(secondHalf);
                		int input = 0;
                		PrintWriter fTest = null;
                
                		fTest = new PrintWriter(new BufferedWriter(new FileWriter(test)));
	                
	                int i = 0;
	                while (input != -1) { // checks for end of file
	                	//if (localTable)
	                	
	                	input = clientInput.read();
                    
	                	if (input != -1) {
                        fTest.write(input);
	                	}
	                	i++;
	                	if (i >= size) { // breaks if all bytes are read
	                		break;
	                	}
	                }
	                fTest.flush();
                
	                test.createNewFile();
                
	                fTest.close();
    
	                clientInput.readLine(); // reads a blank character after file is got and prevents output of it

                	}
            } catch (IOException e) {
            	
            }
        }
    }
}
