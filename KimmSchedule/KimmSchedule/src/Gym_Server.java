import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Gym_Server extends Thread{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	int connected_gym = 0;
	int logged_in_id = 0;
	boolean checked_in = false;
	String login_filename = "src/Users.txt";
	String gyms_filename = "src/Gyms.txt";
	
	public Gym_Server(Socket s, DataInputStream dis, DataOutputStream dos) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
	}
	
	public boolean Reading_Input(String input) {
		try {
			if(input.startsWith("Login:")) {
				String User = input.substring(7,input.indexOf('-')-1);
				String Pass = input.substring(input.indexOf('-')+2);
				if(Loggin_In(User, Pass)) {
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Create:")) {
				int first = input.indexOf('-');
				int second = input.indexOf('-', first+1);
				String User = input.substring(8,first-1);
				String Email;
				String Pass;
				if(second == -1) {
					Pass = input.substring(first+2);
					Email = "No Email";
				} else {
					Pass = input.substring(first+2, second-1);
					Email = input.substring(second+2);
				}
				if(Creating_User(User, Pass, Email)) {
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Check_In")) {
				if(!checked_in) {
					checked_in = true;
					//increase current gym by 1
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Check_Out")) {
				if(checked_in) {
					checked_in = false;
					//decrease current gym by 1
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Reserve:")) {
				int first = input.indexOf('|');
				int second = input.indexOf('|', first+1);
				String Machine_Name = input.substring(9,first-1);
				String Date = input.substring(first+2, second-1);
				String Time = input.substring(second+2);
				if(Reserve_Time(Machine_Name,Date,Time)) {
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Get_Gyms_Info:")) {
				//Get the information for all the gyms
				//Not doing until later
				Get_Information();
			} else if(input.startsWith("Get_Gym_Info:")) {
				//Parse for Gym Name
				//Get the information for the current gym
				Get_Gym_Information();
			} else if(input.startsWith("Get_Reserve_Time:")) {
				//Find Machine Name, in current gym
				Get_Gym_Information();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return true;
		}
	}
	
	public void start() {
		while(true) {
			try {
				if(!Reading_Input(dis.readUTF())) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			//Reading_Input("Reserve: Machine_1 | Dates | Time");
		}
	}
	
	public boolean Loggin_In(String username, String password) throws IOException {
		File file = new File(login_filename);
		Scanner myReader = new Scanner(file);
		
		String st = "";
		while(myReader.hasNextLine()) {
			String line = myReader.nextLine();
			if(line.charAt(0) == '#') {
				//Do not read line, is comment
			} else {
				String user_name = line.substring(0, line.indexOf('-')-1);
				int index = line.indexOf('-', line.indexOf('-')+1);
				String pass_word = line.substring(line.indexOf('-')+2,index-1);
				if(user_name.equals(username) && pass_word.equals(password)) {
					int id = Integer.parseInt(line.substring(line.indexOf('-', index)+2,line.indexOf('-', line.indexOf('-', index+1))-1));
					logged_in_id = id;
					myReader.close();
					return true;
				}
			}
		
		}
		myReader.close();
		return false;
	}
	
	public boolean Log_Out() {
		logged_in_id = 0;
		connected_gym = 0;
		return true;
	}
	
	public boolean Creating_User(String username, String password, String Email) {
		try {
			File file = new File(login_filename);
			Scanner myReader = new Scanner(file);
			int lastid=0;
		
			//
			String st = "";
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if(line.charAt(0) == '#') {
					//Do not read line, is comment
				} else {
					String user_name = line.substring(0, line.indexOf('-')-1);
					int index = line.indexOf('-', line.indexOf('-')+1);
					String pass_word = line.substring(line.indexOf('-')+2,index-1);
					if(user_name.equals(username)) {
						System.out.println("Not creating new User");
						myReader.close();
						return false;
					}
					if(!myReader.hasNextLine()) {
						int id = Integer.parseInt(line.substring(line.indexOf('-', index)+2,line.indexOf('-', line.indexOf('-', index+1))-1));
						lastid = id;
					}
				}
		
			}
			myReader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(login_filename, true));
		    out.write("\n"+username+" - "+password+" - "+(lastid+1)+" - "+Email);
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String Get_Information() {
		connected_gym = 0;
		try {
			File file = new File(login_filename);
			Scanner myReader = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "Server Failure";
		}
		//return list of gyms with info
		return "";
	}
	
	public String Get_Gym_Information() {
		//Open specific gym file
		//Set server connected gym to that id, passed in as parameter
		//return information
		return "";
	}
	
	public boolean Reserve_Time(String Machine_Name, String Date, String Time) {
		List<String> newLines = new ArrayList<>();
		boolean inGym = false;
		boolean inMachine = false;
		boolean found = false;
		boolean alreadyContained = false;
		try {
			for (String line : Files.readAllLines(Paths.get(gyms_filename), StandardCharsets.UTF_8)) {
			    if(!inGym) {
			    	if(line.startsWith("id = "+connected_gym)) {
			    		inGym = true;
			    		newLines.add(line);
			    	} else {
			    		newLines.add(line);
			    	}
			    } else {
			    	if(!inMachine) {
			    		if(line.contains(Machine_Name)){
			    			inMachine = true;
			    			newLines.add(line);
			    		} else {
			    			newLines.add(line);
			    		}
			    	} else {
			    		if(found) {
			    			newLines.add(line);
			    			break;
			    		} else {
			    			if(line.startsWith("R =")) {
			    				if(line.contains(Date+" | "+Time)) {
			    					found = true;
			    					alreadyContained = true;
					    			newLines.add(line);
			    				} else {
					    			newLines.add(line);
			    				}
			    				//Check if same as current reservation
			    			} else {
			    				found = true;
				    			newLines.add("R = "+Date+" | "+Time);
			    				newLines.add(line);
			    			}
			    		}
			    	}
			    }
			}
			if(!found && inMachine) {
    			newLines.add("R = "+Date+" | "+Time);
			}
			Files.write(Paths.get(gyms_filename), newLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return !alreadyContained;
	}
}
