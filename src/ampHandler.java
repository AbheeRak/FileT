import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class creates a server that handles directory migration and file transfer.
 */
public class ampHandler implements Runnable {

    private BufferedReader pInput;
    private Socket client;
    private PrintWriter pWriter;
    private ProcessBuilder build;
    private String ampCommand;
    private String currDirectory;    
    private static final AtomicBoolean running = new AtomicBoolean(false);
    
    public ampHandler (Socket clientSocket, String command, String environment) throws IOException {
        client = clientSocket;
        ampCommand = command;
        //build = new ProcessBuilder();
        currDirectory = environment;
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
            //System.out.println("pathway: " + pathway);
            File getFile = new File(pathway);
            boolean check = getFile.isFile();
            System.out.println(getPathway());
            //System.out.println("pathway: " + pathway + " file check: " + getFile.isFile());
            if (check) {
                //System.out.println(getFile.isFile());
                pWriter.println("Command Id: " + Thread.currentThread().getId()); // returns current thread id to client
                BufferedReader bInput = new BufferedReader(new FileReader(getFile));
                long fileLength = getFile.length();
                String convert = "" + fileLength + "\n";
                //System.out.println(convert);
                pWriter.write(convert);
                String output = "";
                int value = 0;
                int total = value;
                int count = 0;
                String tCheck = "Active";
                while (value != -1) {
                    value = bInput.read();
                    if (value != -1) {
                        total += value;
                        //System.out.print((char)value);
                        output += (char)value;
                        count++;
                    }
                    //System.out.println("total: " + total);
                    if (total >= 1000 || count >= 1000) {
                        total = 0;
                        count = 0;
                        tCheck = myftpserver.idTable.get(Thread.currentThread().getId());
                        //System.out.println("tCheck: " + tCheck);
                        if (tCheck.equals("Terminate")) {
                            stop();
                            break;
                        }
                    }
                }
                //System.out.println(output);
                bInput.close();
                pWriter.println(output); //+ "Command Id: " + Thread.currentThread().getId());//"File Downloaded");
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
            //pWriter.println("Command Id: " + Thread.currentThread().getId());
            String sizeString = pInput.readLine();
            //System.out.println(sizeString +": is the size");
            long size = Long.parseLong(sizeString);
            pathway = getPathway() + File.separator + pathway;
            File test = new File(pathway);
            PrintWriter pOutput = new PrintWriter(new BufferedWriter(new FileWriter(test)));
            //System.out.println(pathway);
            int value = 0;
            int i = 0;
            int total = value;
            String tCheck = "Active";
            while (value != -1) {
                value = pInput.read();
                if (value != -1) {
                    total += value;
                    pOutput.write(value);
                }
                if (total >= 1000) {
                    total = 0;
                    tCheck = myftpserver.idTable.get(Thread.currentThread().getId());
                    if (tCheck.equals("Terminate")) {
                        delete(pathway);
                        stop();
                        break;
                    }
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
        //synchronized (client) {
            try {
            //ServerSocket server = new ServerSocket(port);
            //while (running.get()) {
            myftpserver.idTable.put(Thread.currentThread().getId(), "Active");
            boolean status = true;
            String fullCommand;
            String command;
            String secondHalf;
            //client = server.accept();
            pInput = new BufferedReader(new InputStreamReader(client.getInputStream())); // receives client input
            System.out.println(myftpserver.idTable); // -- test --------------------------------------------------
            pWriter = new PrintWriter(client.getOutputStream(),true); // used to output to client
            build = new ProcessBuilder(); // handles method processes
            build.directory(new File(currDirectory)); // sets current directory
            
            fullCommand = ampCommand; // assigns fullCommand to the ampCommand
            //System.out.println("ampHandler fullCommand: " + fullCommand);
            int len = fullCommand.length();
            fullCommand = fullCommand.substring(0,len - 1); // gets rid of space at end of fullCommand
            //System.out.println("ampHandler fullCommand: " + fullCommand + " 2");
            if (fullCommand == null || fullCommand.equals("quit")) {
                // close current client connections
                //System.out.println("ampHandler fullCommand: " + fullCommand + " 3");
                pInput.close();
                pWriter.close();
                client.close();
            } else if (fullCommand != null) {
                //System.out.println("ampHandler fullCommand: " + fullCommand + " 4");
                int index = fullCommand.indexOf(" ");
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
                    pWriter.println("Command Id: " + Thread.currentThread().getId());
                    put(secondHalf);
                } else {
                    if (!command.equals("")) {
                        //System.out.println(command + ": is unrecognized");
                        pWriter.println("Unrecognized command");
                    }
                }
            }
            //stop();
            myftpserver.idTable.remove(Thread.currentThread().getId());
        } catch (IOException test){
            System.out.println("Server-Client Connection error");
        }
            //}
    }

    public void stop() {
        running.set(false);
    }
    
}
