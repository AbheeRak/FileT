import java.io.*;
import java.net.*;

public class ClientHandlerTest implements Runnable{
    //private Socket client;
    //private BufferedReader in;
    //private PrintWriter out;

    private static BufferedReader input;
    private static Socket client;
    private static PrintWriter pWriter;
    private static ProcessBuilder build;

    public ClientHandlerTest(Socket clientSocket) throws IOException {
        client = clientSocket;
        //in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //out = new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
        try {
            boolean status = true;
            String fullCommand;
            String command;
            String secondHalf;
            boolean check;
            while (status) {
                //client = server.accept();
                input = new BufferedReader(new InputStreamReader(client.getInputStream())); // receives client input
                pWriter = new PrintWriter(client.getOutputStream(),true); // used to output to client
                build = new ProcessBuilder(); // handles method processes
                //build.directory(new File(getPathway())); // sets current directory
                check = true;
                while (check) {
                    fullCommand = input.readLine();
                    if (fullCommand == null || fullCommand.equals("quit")) {
                        check = false;
                        // close current client connections
                        input.close();
                        pWriter.close();
                        client.close();
                    } else if (fullCommand != null) {
                        int index = fullCommand.indexOf(" ");
                        if (index < 0) {
                            command = fullCommand;
                            secondHalf = command;
                        } else {
                            command = fullCommand.substring(0,index);
                            secondHalf = fullCommand.substring(index + 1); // plus 1 skips " "
                        }
                        if (command.equals("pwd")) {
                            //pwd();
                            pWriter.println("pwd");
                        } else if (command.equals("ls")) {
                            //ls();
                            pWriter.println("ls");
                        } else if (command.equals("delete")) {
                            //delete(secondHalf);
                        } else if (command.equals("mkdir")) {
                            //mkdir(secondHalf);
                        } else if (command.equals("get")) {
                            //get(secondHalf);
                        } else if (command.equals("cd")) {
                            //cd(secondHalf);
                        } else if (command.equals("put")) {
                            //put(secondHalf);
                        } else {
                            if (!command.equals("")) {
                                //System.out.println(command + ": is unrecognized");
                                pWriter.println("Unrecognized command");
                            }
                        }
                    }
                } // end of while check
            } // end of while status
            /*while (true) {
                
                String request = in.readLine();
                if (request.contains("command")) {
                    out.println("Command read!");
                } else {
                    out.println("Finish Him!!");

                }

                }*/
        }
        catch (IOException e){
            System.err.println("IO Exception handler");

        }/*finally{
            out.close();
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            }*/

    }
}
