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

		ServerSocket server_socket = new ServerSocket(8788);
		
		while(true) {
			Socket s = null;
			try {
				s = server_socket.accept();
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
                DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				//DataInputStream dis = null;
                //DataOutputStream dos = null;
                Thread t = new Gym_Server(s,dis,dos);
                t.start();
			}
			catch (Exception e) {
				//s.close();
				e.printStackTrace();
			}
		}
	}

}

