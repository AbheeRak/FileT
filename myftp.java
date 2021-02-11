import java.net.*;
import java.io.*;
import java.util.Scanner;

public class myftp {

	public void start(String system, int port) {
		try {
			Socket client = new Socket(system, port);
		} catch (IOException test) {
					
		}
	}
	
	public static void main(String[] args) {
		//Scanner input = new Scanner(System.in);

			
		//myftpserver server = new myftpserver();
			
		String system = args[0];
		int port = Integer.parseInt(args[1]);
		//myftp connection = new myftp(); // creates a client object
		//connection.start(system, port); // connects to the server
		try {
			Socket client = new Socket(system, port);
			PrintWriter clientOutput = new PrintWriter(client.getOutputStream(),true);
			BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			BufferedReader clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
					
			boolean check = true;
			String fullCommand;
			String command;
			String secondHalf;
			while (check) {
				System.out.print("mytftp>");
				fullCommand = console.readLine();
				int index = fullCommand.indexOf(" ");
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
					long size = Long.parseLong(sizeString); // byte size of file
					//System.out.println(size);
					secondHalf = secondHalf.substring(1);
					File test = new File(secondHalf);
					//System.out.println("File path is: " + test.getAbsolutePath());
					int input = 0;
					PrintWriter fTest = new PrintWriter(new BufferedWriter(new FileWriter(test)));
					int i = 0;
					//System.out.println("check");
					while (input != -1) {
						//System.out.println((char)input);
						//input = clientInput.readLine();
						input = clientInput.read();
						if (input != -1) {
							//System.out.println(input);
								fTest.write(input);
							//}
						}
						i++;
						if (i >= size) {
							break;
						}
					}
						fTest.flush();
						test.createNewFile();
						//System.out.println("ummmmmmmm");
						//System.out.println(test.exists());
						fTest.close();
						System.out.println(clientInput.readLine());
				} else if (command.equals("put")) {
					clientOutput.println(fullCommand); // here for testing right now
					secondHalf = secondHalf.substring(1);
					File putFile = new File(secondHalf);
					BufferedReader bFileInput = new BufferedReader(new FileReader(putFile));
					int i = 0;
					while (i != -1) {
						i = bFileInput.read();
						if (i != -1) {
							clientOutput.write(i);
						}
					}
					bFileInput.close();
					System.out.println(clientInput.readLine()); // for testing
				} else {
				//System.out.println("test 2");
				clientOutput.println(fullCommand);
				//System.out.println("what is this");
				System.out.println(clientInput.readLine());
				}
			}
			clientOutput.close();
			console.close();
			clientInput.close();
			client.close();
			System.exit(0);
		} catch (IOException test) {
			
		}

	}			

}
