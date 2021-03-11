import java.net.*;
import java.io.*;
import java.util.HashMap;

/**
 * This class creates a client object that connects to a server for file transfer.
 */
public class myftp {
	
	/**
	 * The main method establishes a connection to a server and prompts for and handles user input.
	 */
	public static HashMap<Long,String> localTable;
	
	public static void delete(String argument) {
		argument = File.separator + argument;
		File dFile = new File(argument);
		dFile.delete();
	}
	
	public static void main(String[] args) {
		String system = args[0];
		int nport = Integer.parseInt(args[1]);
		int tport = Integer.parseInt(args[2]);
		localTable = new HashMap<Long,String>(300);;
		try {
			Socket client = new Socket(system, nport);
			PrintWriter clientOutput = new PrintWriter(client.getOutputStream(),true);
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			Socket terminate = new Socket(system, tport);
			PrintWriter tOutput = new PrintWriter(terminate.getOutputStream(),true);
			BufferedReader tInput = new BufferedReader(new InputStreamReader(terminate.getInputStream()));
			
			boolean check = true;
			String fullCommand;
			String command;
			String secondHalf;
			String endCommand;
			while (check) {
				System.out.print("mytftp>");
				fullCommand = console.readLine();
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
				//System.out.println(index); // for testing
				if (endCommand.equals("&")) { // handles "&" cases
	                fullCommand = fullCommand.substring(0,finalIndex - 1);
	                if (index < 0) {
						command = fullCommand;
						secondHalf = command;
					} else {
						command = fullCommand.substring(0,index);
						secondHalf = fullCommand.substring(index);
					}
				} else {
					if (index < 0) {
						command = fullCommand;
						secondHalf = command;
					} else {
						command = fullCommand.substring(0,index);
						secondHalf = fullCommand.substring(index);
					}
				}               
                
				if (command.equals("quit")) {
					check = false;
					tOutput.println(command);


				} else if (command.equals("get") && endCommand.equals("&")) {
					synchronized (client) {
					clientOutput.println(fullCommand + " " + endCommand);
					System.out.println(clientInput.readLine());
					//String store = clientInput.readLine();
					//System.out.println(store); // read out pid
					//int colon = store.indexOf(":");
					//store = store.substring(colon + 1);	
//						System.out.println(clientInput.readLine()); // trying to read pID as early as possible
//						String sizeString = clientInput.readLine();
//						if (sizeString.equals("false")) {
//							System.out.println(secondHalf + " does not exist in the current directory.");
//						} else {

//							long size = Long.parseLong(sizeString); // byte size of file
							secondHalf = secondHalf.substring(1);
							System.out.println("secondHalf " + secondHalf);

							getworker gworker = new getworker(client, secondHalf);

							Thread gthread = new Thread(gworker);
							//long providedId = Long.parseLong(store);
							//localTable.put(providedId,secondHalf);
							gthread.start();
					}
				}

				else if (command.equals("get")) {

					clientOutput.println(fullCommand);
					String sizeString = clientInput.readLine();
					if (sizeString.equals("false")) {
						secondHalf = secondHalf.substring(1);
						System.out.println(secondHalf + " does not exist in the current directory.");
					} else {
						//System.out.println(clientInput.readLine()); // moved from the bottom to try to receive id sooner
						long size = Long.parseLong(sizeString); // byte size of file
						secondHalf = secondHalf.substring(1);
						File test = new File(secondHalf);
						int input = 0;
						PrintWriter fTest = new PrintWriter(new BufferedWriter(new FileWriter(test)));
						int i = 0;
						while (input != -1) { // checks for end of file
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
						System.out.println(clientInput.readLine());
					}
				} else if (command.equals("put") && endCommand.equals("&")) {
					clientOutput.println(fullCommand + " " + endCommand);
					secondHalf = secondHalf.substring(1);
					putworker pworker = new putworker(client,secondHalf);


					Thread pthread = new Thread(pworker);
					pthread.start();
				}
				 else if (command.equals("put")) {
					clientOutput.println(fullCommand);
					secondHalf = secondHalf.substring(1);
					File putFile = new File(secondHalf);
					if (putFile.isFile()) {
						BufferedReader bFileInput = new BufferedReader(new FileReader(putFile));
						long fileLength = putFile.length();
						String convert = "" + fileLength + "\n";
						clientOutput.write(convert);
						String output = "";
						int value = 0;
						while (value != -1) {
							value = bFileInput.read();
							if (value != -1) {
								output += (char)value;
							}
						}
						bFileInput.close();
						clientOutput.println(output);
						System.out.println(clientInput.readLine());
					} else {
						System.out.println(secondHalf + " does not exist in current directory.");
						clientOutput.println("");
						//System.out.println(clientInput.readLine());
					}
				} else if (command.equals("terminate")) {
					secondHalf = secondHalf.substring(1);
					tOutput.println(secondHalf);
					/*Long id = Long.parseLong(secondHalf);
					if (localTable.containsKey(id)) {
						delete(secondHalf);
					}*/
					System.out.println(tInput.readLine()); // may need to be changed ----------------
				} else {
					clientOutput.println(fullCommand);
					System.out.println(clientInput.readLine());
				}
			}
			clientOutput.close();
			console.close();
			clientInput.close();
			client.close(); // close all connections once done executing
			tInput.close();
			tOutput.close();
			terminate.close();
			System.exit(0);
		} catch (IOException test) {
			System.out.println("Client-Server communication error caught.");
			System.out.println("If uploading/downloading file, file may not exist.");
		} catch (NumberFormatException error) {
			System.out.println("File transfer could not occur");
		}

	}			

}
