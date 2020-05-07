import java.io.*;
import java.util.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JTextField;
public class Main {
	public static void main(String[] args) throws IOException {
		JTextField textField = new JTextField();
	    JFrame jframe = new JFrame();
	    jframe.add(textField);
	    jframe.setSize(400, 350);
	    jframe.setVisible(true);
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    //List<Thread> connections = new ArrayList<Thread>();

		ServerSocket server_socket = new ServerSocket(8788);
		
		while(true) {
			Socket s = null;
			try {
				s = server_socket.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                new Thread(new Gym_Server(s, dis, dos)).start();
			}
			catch (Exception e) {
				//s.close();
				e.printStackTrace();
			}
		}
	}

}

