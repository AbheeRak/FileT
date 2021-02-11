import java.io.*;
import java.net.*;
import java.util.Scanner;

public class myftpserver {

    private static BufferedReader input;// = new BufferedReader(new InputStreamReader(client.getInputStream()));
    private static Socket client;
    private static PrintWriter pWriter;
    private static ProcessBuilder build;
    
	public void startUp(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			Socket client = server.accept();
		} catch (IOException test){
			System.out.println("System Connection error");
		}
	}
	
	
	public static String pwd() {
		String output = "";
		try {
            build.command("pwd");
            Process test = build.start();
            //test = Runtime.getRuntime().exec("pwd");
            BufferedReader bRead = new BufferedReader(new InputStreamReader(test.getInputStream(),"US-ASCII"));
            int value = 0; //bRead.read();
            while (value != -1) {
                value = bRead.read();
                if ((char)value != '\n' && value != -1) {
                    output += (char)value;
                }
                //value = bRead.read();
            }
            //System.out.println(output);
            //System.out.println(output);
            pWriter.println(output); // writes the pathway to the client
            pWriter.flush();
            client.getOutputStream().flush();
            bRead.close();
            test.destroy();
		} catch (IOException test) {
			System.out.println("Error");
			
		}
		return output;
	}
	
	public static void ls() {
		try {
            build.command("ls");;
			Process test = build.start();
            //test = Runtime.getRuntime().exec("ls");
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
            System.out.println("Error");	
        }		
	}
	
	public static void delete(String argument) {
		try {
            build.command("rm",argument);
            Process test = build.start();
			//Process test = Runtime.getRuntime().exec("rm " + argument);
			pWriter.println("Deleted" + argument);
            pWriter.flush();
            client.getOutputStream().flush();
            test.destroy();
		} catch (IOException Error) {
		
		}
	}
	
	public static void mkdir(String argument) {
		try {
            build.command("mkdir",argument);
            Process test = build.start();
			//Process test = Runtime.getRuntime().exec("mkdir" + argument);
			pWriter.println("Directory Created");
            pWriter.flush();
            client.getOutputStream().flush();
            test.destroy();
		} catch (IOException Error) {
		
		}
	}
	
	public static void cd(String argument) {
        try {
            build.command("pwd");
            Process test = build.start();
            //test = Runtime.getRuntime().exec("pwd");
            BufferedReader bRead = new BufferedReader(new InputStreamReader(test.getInputStream(),"US-ASCII"));
            int value = 0; //bRead.read();
            String output = "";
            while (value != -1) {
                value = bRead.read();
                if ((char)value != '\n' && value != -1) {
                    output += (char)value;
                }
                //value = bRead.read();
            }
            bRead.close();
            test.destroy();
            String path = output;
        
            if (argument.equals("..")) {
                int len = path.length();
                boolean flag = true;
                int realSlash = 0;
                String sub = path;
                //int count = 0;
                while (flag) {
                    int firstSlash = sub.indexOf("/");
                    //System.out.println("firstSlash: " + firstSlash);
                    if (firstSlash < 0 || firstSlash >= len - 1) {
                        flag = false;
                    } else {
                        //System.out.println("oops");
                        sub = sub.substring(firstSlash + 1);
                        //System.out.println(sub + " is the substring");
                        realSlash += firstSlash + 1;
                        /*count++;
                        if (count > 50) {
                            break;
                            }*/
                    }
                }	
                if (realSlash == 1) {
                    path = "/home";
                } else {
                    path = path.substring(0,realSlash - 1);
                }
                //System.out.println(path + " end version of path");
                build.directory(new File(path));
                pWriter.println("Directory Changed");
                /*} catch (IOException Exception) {
                  
                  }*/
            } else {
                //System.out.println("hit");
                build.directory(new File(path + File.separator + argument));
                System.out.println(build.directory());
                pWriter.println("Directory Changed");
            }
        } catch (IOException Exception) {
            
        }        
	}
	
	public static void get(String pathway) {
        try {
            File getFile = new File(pathway);
            BufferedReader bInput = new BufferedReader(new FileReader(getFile));
            //BufferedOutputStream bOutput = new BufferedOutputStream(new FileOutputStream());
            long fileLength = getFile.length();
            String convert = "" + fileLength + "\n";
            pWriter.write(convert);
            System.out.println("test");
            String output = "";
            int value = 0;
			while (value != -1) {
				value = bInput.read();
                if (value != -1) {
                    System.out.print((char)value);
                    output += (char)value;
                }
			}
            bInput.close();
            //bOutput.close();
            System.out.println("hello there");
            //pWriter = new PrintWriter(client.getOutputStream(), true);
            pWriter.println(output);
		} catch (IOException Error) {
            
		}
		
	}

	public static void put(String pathway) {
	try {
            String sizeString = input.readLine();
            long size = Long.parseLong(sizeString);
	        File test = new File(pathway);
            PrintWriter pOutput = new PrintWriter(new BufferedWriter(new FileWriter(test)));
            System.out.println(pathway);
            int value = 0;
            int i = 0;
            while (value != -1) {
                //client.getOutputStream().write(value);
                value = input.read();
                //System.out.println(value);
                //pWriter.write(value);
                if (value != -1) {
                    pOutput.write(value);
                }
                i++;
                if (i >= size) {
                    break;
                }
            }
            pOutput.flush();
            //client.getOutputStream().close();
            test.createNewFile();
            pOutput.close();
            //bOutput.close();
            //System.out.println("hello there");
            pWriter.println("File Put");
        } catch (IOException Error) {

		}
    }
	
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
        try {
            ServerSocket server = new ServerSocket(port);
            boolean status = true;
            String fullCommand;
            String command;
            String secondHalf;
            boolean check;
            while (status) {
                client = server.accept();
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                pWriter = new PrintWriter(client.getOutputStream(),true);
                build = new ProcessBuilder();
                check = true;
                while (check) {
                    fullCommand = input.readLine();
                    int index = fullCommand.indexOf(" ");
                    if (index < 0) {
                        command = fullCommand;
                        secondHalf = command;
                    } else {
                        command = fullCommand.substring(0,index);
                        secondHalf = fullCommand.substring(index);
                    }
                    if (command.equals("quit")) {
                        check = false;
                        input.close();
                        pWriter.close();
                        client.close();                        
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
                        secondHalf = secondHalf.substring(1);
                        get(secondHalf);
                    } else if (command.equals("cd")) {
                        secondHalf = secondHalf.substring(1);
                        cd(secondHalf);
                    } else if (command.equals("put")) {
                        secondHalf = secondHalf.substring(1);
                        put(secondHalf);
                    }
                } // end of while check
            } // end of while status
        } catch (IOException test){
            System.out.println("System Connection error");
        } catch (NullPointerException check) {
            System.out.println("Null Pointer Error");
        }
        
    }
}
