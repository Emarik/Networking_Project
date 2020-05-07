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
	String login_filename = "appClientModule/Users.txt";
	String gyms_filename = "appClientModule/Gyms.txt";
	
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
					if(Check_In()) {
					checked_in = true;
					dos.writeBoolean(true);
					}else {
						dos.writeBoolean(false);
					}
				} else {
					dos.writeBoolean(false);
				}
			} else if(input.startsWith("Check_Out")) {
				if(checked_in) {
					if(Check_Out()) {
					checked_in = false;
						dos.writeBoolean(true);
					}else {
						dos.writeBoolean(false);
					}
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
			} else if(input.startsWith("Get_Gyms_Info")) {
				String ret = Get_Information();
				dos.writeUTF(ret);
			} else if(input.startsWith("Get_Gym_Info:")) {
				String Gym_Name = input.substring(14);
				String ret = Get_Gym_Information(Gym_Name);
				dos.writeUTF(ret);
			} else if(input.startsWith("Get_Reserve_Time:")) {
				int first = input.indexOf('|');
				String Machine_Name = input.substring(18,first-1);
				String Date = input.substring(first+2);
				String ret = Get_Reserve_Time(Machine_Name, Date);
				dos.writeUTF(ret);
			} else if(input.startsWith("Log out")) {
				if(Log_Out()) {
					dos.writeBoolean(true);
				} else {
					dos.writeBoolean(false);
				}
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
				break;
			}
		}
	}
	
	public boolean Loggin_In(String username, String password) throws IOException {
		File file = new File(login_filename);
		Scanner myReader = new Scanner(file);
		
		String st = "";
		while(myReader.hasNextLine()) {
			String line = myReader.nextLine();
			if(line.length() == 0)
				continue;
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
		if(checked_in) {
			Check_Out();
		}
		logged_in_id = 0;
		connected_gym = 0;
		
		return true;
	}
	
	public boolean Creating_User(String username, String password, String Email) {
		try {
			File file = new File(login_filename);
			Scanner myReader = new Scanner(file);
			int lastid=0;
		
			String st = "";
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if(line.length() == 0)
					continue;
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
		String st = "";
		try {
			File file = new File(gyms_filename);
			Scanner myReader = new Scanner(file);
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if(line.length() == 0)
					continue;
				if(line.charAt(0) == '#') {
					//Do not read line, is comment
				} else {
					if(line.startsWith("G = ")) {
						if(!st.equals("")) {
							st+=" | ";
						}
						st += line.substring(4);
					}
				}
				
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Server Failure";
		}
		return st;
	}
	
	public String Get_Gym_Information(String Gym_Name) {
		boolean inGym = false;
		String st = "";
		try {
			File file = new File(gyms_filename);
			Scanner myReader = new Scanner(file);
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if(line.length() == 0)
					continue;
				if(line.charAt(0) == '#') {
					//Do not read line, is comment
				} else {
					if(inGym) {
						if(line.startsWith("G = ")) {
							inGym = false;
						} else if(line.startsWith("id = ")) {
							connected_gym = Integer.parseInt(line.substring(5));
						} else if(line.startsWith("MaxU = ")) {
							st = line.substring(7);
						} else if(line.startsWith("users = ")) {
							st = st+=" | " + line.substring(8);
						} else if(line.startsWith("hours = ")) {
							st = st+=" | " + line.substring(8);
						} else if(line.startsWith("M = ")) {
							st = st+=" | " + line.substring(4);
						}
					} else {
						if(line.contains("G = "+Gym_Name)){
							inGym = true;
						}
					}
				}
				
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Server Failure";
		}
		return st;
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
			    		if(line.contains("M = "+Machine_Name)){
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
	public boolean Check_In() {
		List<String> newLines = new ArrayList<>();
		boolean inGym = false;
		boolean found = false;
		boolean success = false;
		int max = 0;
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
			    	if(found) {
			    		newLines.add(line);
			    	} else {
			    		if(line.startsWith("MaxU = ")){
			    			max = Integer.parseInt(line.substring(7));
		    				newLines.add(line);
			    		} else if(line.startsWith("users = ")) {
			    			found = true;
			    			int current = Integer.parseInt(line.substring(8));
			    			if(current+1>max) {
			    				newLines.add(line);
			    			} else {
			    				newLines.add("users = " + (current+1));
			    				success = true;
			    			}
			    		}
			    	}
			    }
			}
			Files.write(Paths.get(gyms_filename), newLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return success;
	}
	public boolean Check_Out() {
		List<String> newLines = new ArrayList<>();
		boolean inGym = false;
		boolean found = false;
		boolean success = false;
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
			    	if(found) {
			    		newLines.add(line);
			    	} else {
			    		if(line.startsWith("users = ")) {
			    			found = true;
			    			int current = Integer.parseInt(line.substring(8));
			    			if(current-1<0) {
			    				newLines.add(line);
			    			} else {
			    				newLines.add("users = " + (current-1));
			    				success = true;
			    			}
			    		} else {
		    				newLines.add(line);
			    		}
			    	}
			    }
			}
			Files.write(Paths.get(gyms_filename), newLines, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return success;
	}
	
	public String Get_Reserve_Time(String Machine_Name, String Date) {
		boolean inGym = false;
		boolean inMachine = false;
		String st = "";
		try {
			File file = new File(gyms_filename);
			Scanner myReader = new Scanner(file);
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine();
				if(line.length() == 0)
					continue;
				if(line.charAt(0) == '#') {
					//Do not read line, is comment
				} else {
					if(inGym) {
						if(line.startsWith("G = ")) {
							inGym = false;
							inMachine = false;
							break;
						} else if(inMachine) {
							if(line.startsWith("R = ")) {
								if(line.contains("R = "+Date)) {
									if(st != "") {
										st += " | ";
									}
									st += line.substring(Date.length()+7);
								}
							} else {
								inMachine = false;
							}
						} if(line.contains("M = "+Machine_Name)) {
							inMachine = true;
						}
					} else {
						if(line.contains("id = "+connected_gym )){
							inGym = true;
						}
					}
				}
				
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Server Failure";
		}
		return st;
	}
}
