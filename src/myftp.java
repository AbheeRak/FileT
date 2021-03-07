import java.net.*;
import java.io.*;

/**
 * This class creates a client object that connects to a server for file transfer.
 */
public class myftp {
	
	/**
	 * The main method establishes a connection to a server and prompts for and handles user input.
	 */
	public static void main(String[] args) {
		String system = args[0];
		int nport = Integer.parseInt(args[1]);
		int tport = Integer.parseInt(args[2]);
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
			String lastPart;
			while (check) {
				System.out.print("mytftp>");
				fullCommand = console.readLine();
				int index = fullCommand.indexOf(" ");
				int finalIndex = 0;
				String endCommand = fullCommand;
				int storedValue;
				while (endCommand.indexOf(" ") > 0) {
					storedValue = endCommand.indexOf(" ");
					finalIndex += storedValue;
					endCommand = endCommand.substring(storedValue + 1);
				}
				endCommand = fullCommand;
				if (finalIndex <= 0) {
					finalIndex = index;
				} else {
					endCommand = fullCommand.substring(finalIndex + 1); // will be used to check if ends in &
				}
				//System.out.println(index); // for testing
				if (index < 0) {
					command = fullCommand;
					secondHalf = command;
				} else {
					command = fullCommand.substring(0,index);
					secondHalf = fullCommand.substring(index);
				}
				if (command.equals("quit")) {
					check = false;
				} else if (command.equals("get")) {
					clientOutput.println(fullCommand);
					String sizeString = clientInput.readLine();
					if (sizeString.equals("false")) {
						secondHalf = secondHalf.substring(1);
						System.out.println(secondHalf + " does not exist in the current directory.");
					} else {
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
				} else if (command.equals("put")) {
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
					tOutput.println(fullCommand);
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
			System.exit(0);
		} catch (IOException test) {
			System.out.println("Client-Server communication error caught.");
			System.out.println("If uploading/downloading file, file may not exist.");
		} catch (NumberFormatException error) {
			System.out.println("File transfer could not occur");
		}

	}			

}
