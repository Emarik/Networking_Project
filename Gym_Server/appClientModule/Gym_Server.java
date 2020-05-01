import java.io.*;
import java.net.*;
import java.util.*;

public class Gym_Server extends Thread{
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	int connected_gym = 0;
	int logged_in_id = 0;
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
				//Parse to get user and pass
				boolean success = Loggin_In("Emarik", "Something");
			} else if(input.startsWith("Create:")) {
				//Parse to get user and pass, and others
				Creating_User("Emariks2", "Something");
			} else if(input.startsWith("Check_In:")) {
				Check_In();
			} else if(input.startsWith("Check_Out:")) {
				Check_Out();
			} else if(input.startsWith("Reserve:")) {
				Reserve_Time();
			} else if(input.startsWith("Get_Gyms_Info:")) {
				Get_Information();
			} else if(input.startsWith("Get_Gym_Info:")) {
				Get_Gym_Information();
			} else if(input.startsWith("Get_Reserve_Time:")) {
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
		}
	}
	
	public boolean Loggin_In(String username, String password) throws IOException {
		File file = new File(login_filename);
		Scanner myReader = new Scanner(file);
		
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
				//System.out.println("User: " + user_name+ " : " + user_name.length());
				//System.out.println("Pass: " + pass_word+ " : " + pass_word.length());
				if(user_name.equals(username) && pass_word.equals(password)) {
					int id = Integer.parseInt(line.substring(line.indexOf('-', index)+2,line.indexOf('-', line.indexOf('-', index+1))-1));
					logged_in_id = id;
					myReader.close();
					//System.out.println("Connected");
					return true;
				}
			}
		
		}
		//System.out.println("Failure");
		myReader.close();
		return false;
	}
	
	public boolean Log_Out() {
		logged_in_id = 0;
		connected_gym = 0;
		return true;
	}
	
	public boolean Creating_User(String username, String password) {
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
					System.out.println("User: " + user_name+ " : " + user_name.length());
					System.out.println("Pass: " + pass_word+ " : " + pass_word.length());
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
			//Need to find the last id used
			myReader.close();
			BufferedWriter out = new BufferedWriter(new FileWriter(login_filename, true));
		    out.write("\n"+username+" - "+password+" - "+(lastid+1)+" - "+"NewEmail");
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public String Get_Information() {
		connected_gym = 0;
		//Open file
		//return list of gyms with info
		return "";
	}
	
	public String Get_Gym_Information() {
		//Open specific gym file
		//Set server connected gym to that id
		//return information
		return "";
	}
	
	public boolean Check_In() {
		//set checked in
		//increase number of current gym
		//return if successful
		return true;
	}
	
	public boolean Check_Out() {
		//set checked in
		//decrease number of current gym
		//return if successful
		return true;
	}
	
	public boolean Reserve_Time() {
		//Set reservation in file
		//return if setting was successful
		return true;
	}
}
