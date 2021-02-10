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
					secondHalf = secondHalf.substring(1);
					//System.out.println(secondHalf);
					File test = new File(secondHalf);
					
					System.out.println("File path is: " + test.getAbsolutePath());
					String input = "";
					FileWriter fTest = new FileWriter(test);
					clientOutput.println(fullCommand); // putting this here to test stuff
					int i = 0;
					System.out.println("check");
					input = clientInput.readLine();//System.out.println("hit");
					System.out.println(input);
					//if (input != null) {
					fTest.write(input);
					fTest.flush();
					System.out.println("loop number: " + i++);
					//}//clientOutput.write(input);
					//input = clientInput.read();
					
					//System.out.println("------------");
					//System.out.println("-----------");
					//fTest.write(clientInput.readLine());
					System.out.println("end of the road");
					test.createNewFile();
					System.out.println(test.exists());
					fTest.close();
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
		//input.close();
				
	}			
		
		//input.close();


}
