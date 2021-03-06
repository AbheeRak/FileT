import java.io.*;
import java.net.*;

/**
 * This class creates a server that handles directory migration and file transfer.
 */
public class CommandHandler implements Runnable {

    private BufferedReader input;
    private Socket client;
    private PrintWriter pWriter;
    private ProcessBuilder build;	

    
    public CommandHandler (Socket clientSocket) throws IOException {
        client = clientSocket;
        //in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        //out = new PrintWriter(client.getOutputStream(),true);
    }
    
    /**
     * This method finds the current working directory of ProcessBuilder build and sends it to the client socket.
     */
	public void pwd() {
		String output = "";
		try {
            build.command("pwd");
            Process test = build.start();
            BufferedReader bRead = new BufferedReader(new InputStreamReader(test.getInputStream(),"US-ASCII"));
            int value = 0; //bRead.read();
            while (value != -1) {
                value = bRead.read();
                if ((char)value != '\n' && value != -1) {
                    output += (char)value;
                }
            }
            pWriter.println(output); // writes the pathway to the client
            pWriter.flush();
            client.getOutputStream().flush();
            bRead.close();
            test.destroy();
		} catch (IOException test) {
			System.out.println("IOException error finding the current working directory");
			
		}
		//return output;
	}

    /**
     * This method finds and returns the current working directory of ProcessBuilder build as a String object.
     */
    private String getPathway() {
        String output = "";
        try {
            build.command("pwd");
            Process test = build.start();
            BufferedReader bRead = new BufferedReader(new InputStreamReader(test.getInputStream(),"US-ASCII"));
            int value = 0;
            while (value != -1) {
                value = bRead.read();
                if ((char)value != '\n' && value != -1) {
                    output += (char)value;
                }
            }
            bRead.close();
            test.destroy();
            return output;
        } catch (IOException Exception) {
            System.out.println("IOException error finding the current working directory");
        }
        return output;
    }

    /**
     * This method uses the ProcessBuilder build object to execute the Linux "ls" command and sends the results to the client
     * socket.
     */
	public void ls() {
		try {
            build.command("ls");
			Process test = build.start();
            BufferedReader bRead = new BufferedReader(new InputStreamReader(test.getInputStream()));
            int value = 0;
            String output = "";
            while (value != -1) {
                value = bRead.read();
                if (value != -1) {
                    if ((char)value == '\n') {
                        output += " ";
                    } else {
                        output += ((char)value);
                    }
                }
            }
            pWriter.println(output); // writes output to client
            pWriter.flush();
            client.getOutputStream().flush();
            bRead.close();
            test.destroy();
        } catch (IOException test) {
            System.out.println("IOException error listing out files in current directory");	
        }		
	}

    /**
     * This method deletes a specified file provided by the client command argument.
     */
	public void delete(String argument) {
            argument = getPathway() + File.separator + argument;
            File dFile = new File(argument);
            dFile.delete();
			pWriter.println("Deleted " + argument);
            pWriter.flush();
	}

    /**
     * This method creates a new directory specified by the client command argument.
     */
	public void mkdir(String argument) {
            argument = getPathway() + File.separator + argument;
            File dirFile = new File(argument);
            dirFile.mkdir();
            pWriter.println("Directory Created");
            pWriter.flush();
    }

    /**
     * This method changes directories either to a subdirectory specified in the argument or to the parent directory
     * of the current working directory if the argument is equal to ".."
     */
	public void cd(String argument) {
        String path = getPathway(); // gets the current directory path       
        if (argument.equals("..")) {
            int len = path.length();
            boolean flag = true;
            int realSlash = 0;
            String sub = path;
            while (flag) {
                int firstSlash = sub.indexOf("/");
                if (firstSlash < 0 || firstSlash >= len - 1) {
                    flag = false;
                } else {
                    sub = sub.substring(firstSlash + 1);
                    realSlash += firstSlash + 1;
                }
            }
            if (realSlash == 1) { // maximum cd .. will take a user is the home path
                path = "/home";
            } else {
                path = path.substring(0,realSlash - 1);
            }
            build.directory(new File(path));
            pWriter.println("Directory Changed");
        } else {
            File check = new File(path + File.separator + argument);
            if (check.isDirectory()) {
                build.directory(new File(path + File.separator + argument));
                pWriter.println("Directory Changed");
            } else {
                pWriter.println("Directory Does Not Exist");
            }
        }   
	}
    
    /**
     * This method gets a file from the current directory and sends it to the working directory of the client.
     */
	public void get(String pathway) {
        try {
            pathway = getPathway() + File.separator + pathway;
            File getFile = new File(pathway);
            boolean check = getFile.isFile();
            //System.out.println("pathway: " + pathway + " file check: " + getFile.isFile());
            if (check) {
                //System.out.println(getFile.isFile());
                //pWriter.println("Command Id: " + Thread.currentThread().getId()); // returns current thread id to client
                BufferedReader bInput = new BufferedReader(new FileReader(getFile));
                long fileLength = getFile.length();
                String convert = "" + fileLength + "\n";
                pWriter.write(convert);
                String output = "";
                int value = 0;
                while (value != -1) {
                    value = bInput.read();
                    if (value != -1) {
                        //System.out.print((char)value);
                        output += (char)value;
                    }
                }
                //System.out.println(output);
                bInput.close();
                // currently outputs file and then returns id
                pWriter.println(output + "Command Id: " + Thread.currentThread().getId()); //"File Downloaded");
            } else {
                pWriter.println(check);
            }
		} catch (IOException Error) {
            pWriter.println("IOException Error when downloading file");
		}
	}

    /**
     * This method pulls a file from the client's working directory and sends it to the current directory.
     */
    public void put(String pathway) {
        try {
            pWriter.println("Command Id: " + Thread.currentThread().getId());
            String sizeString = input.readLine();
            System.out.println(sizeString + ": total number of bytes");
            long size = Long.parseLong(sizeString);
            pathway = getPathway() + File.separator + pathway;
            File test = new File(pathway);
            PrintWriter pOutput = new PrintWriter(new BufferedWriter(new FileWriter(test)));
            //System.out.println(pathway);
            int value = 0;
            int i = 0;
            while (value != -1) {
                value = input.read();
                System.out.println(value); // value println
                if (value != -1) {
                    pOutput.write(value);
                }
                i++;
                if (i >= size) {
                    break;
                }
            }
            pOutput.flush();
            test.createNewFile();
            pOutput.close();
            //pWriter.println("File Uploaded");
        } catch (IOException Error) {
            System.out.println("IOException Error when uploading file");
        } catch (NumberFormatException Error) {
            System.out.println("Error when uploading file");
        }
    }
    
    @Override
	public void run() {
		//int nport = Integer.parseInt(args[0]);
        //int tport = Integer.parseInt(args[1]);
        //int port = 8200;
            try {
                //ServerSocket server = new ServerSocket(port);
                //myftpserver.idTable.put(Thread.currentThread().getId(), "Active"); // for testing purposes ---------------
                //System.out.println(Thread.currentThread().getId());
                boolean status = true;
                String fullCommand;
                String command;
                String secondHalf;
                String endCommand;
                boolean check;
                input = new BufferedReader(new InputStreamReader(client.getInputStream())); // receives client input
                pWriter = new PrintWriter(client.getOutputStream(),true); // used to output to client
                build = new ProcessBuilder();
                
                //while (status) {
                //client = server.accept();
                //input = new BufferedReader(new InputStreamReader(client.getInputStream())); // receives client input
                //pWriter = new PrintWriter(client.getOutputStream(),true); // used to output to client
                //build = new ProcessBuilder(); // handles method processes
                build.directory(new File(getPathway())); // sets current directory
                check = true;
                while (check) {
                    fullCommand = input.readLine();
                    System.out.println("fullCommand: " + fullCommand);
                    if (fullCommand == null || fullCommand.equals("quit")) {
                        check = false;
                        // close current client connections
                        input.close();
                        pWriter.close();
                        client.close();
                    } else if (fullCommand != null) {
                        int index = fullCommand.indexOf(" ");
                        int finalIndex = 0;
                        endCommand = fullCommand;
                        int storedValue;
                        while (endCommand.indexOf(" ") > 0) {
                            storedValue = endCommand.indexOf(" ");
                            finalIndex += storedValue;
                            endCommand = endCommand.substring(storedValue + 1);
                            finalIndex += 1;
                        }
                        endCommand = fullCommand;
                        if (finalIndex <= 0) {
                            finalIndex = index;
                        } else {
                            endCommand = fullCommand.substring(finalIndex); // will be used to check if ends in &
                        }
                        /*System.out.println("Endcommand: " + endCommand);
                        System.out.println("finalIndex: " + finalIndex);
                        System.out.println("index: " + index);*/
                        // invokes ampHandler section
                        if (endCommand.equals("&")) { // handles "&" cases
                            fullCommand = fullCommand.substring(0,finalIndex);
                            ampHandler singleRun = new ampHandler(client, fullCommand, getPathway());
                            Thread tAmp = new Thread(singleRun); // runs instance of ampHandler
                            tAmp.start();
                            //int testing = 0;
                            while(tAmp.isAlive()) {
                                // halts execution in loop
                                }
                            System.out.println("is tAMp alive: " + tAmp.isAlive());
                        } else {
                            if (index < 0) {
                                command = fullCommand;
                                secondHalf = command;
                            } else {
                                command = fullCommand.substring(0,index);
                                secondHalf = fullCommand.substring(index + 1); // plus 1 skips " "
                            }
                            if (command.equals("pwd")) {
                                pwd();
                            } else if (command.equals("ls")) {
                                ls();
                            } else if (command.equals("delete")) {
                                delete(secondHalf);
                            } else if (command.equals("mkdir")) {
                                mkdir(secondHalf);
                            } else if (command.equals("get")) {
                                get(secondHalf);
                            } else if (command.equals("cd")) {
                                cd(secondHalf);
                            } else if (command.equals("put")) {
                                put(secondHalf);
                            } else {
                                if (!command.equals("")) {
                                    //System.out.println(command + ": is unrecognized");
                                    pWriter.println("Unrecognized command");
                                }
                            }
                        } // else for if endCommand.equals("&")
                    } // end of fullCommand not being null
                } // end of while check
                //} // end of while status
            } catch (IOException test){
                System.out.println("Server-Client Connection error");
            }
    }
}
