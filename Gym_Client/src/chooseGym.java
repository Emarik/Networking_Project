import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextArea;

//import Calculator.Simple_Calculator;
//import Scheduler.Schedule_Window;
//import Gym_Home.Gym_Home;


public class chooseGym extends JFrame implements ActionListener{
	JLabel lbl_Title;
	JLabel lblMattioli;
	JLabel lblRecB;
	JTextField txtUsername;
	JPasswordField textPassword;
	JButton btnMattioli;
	JButton btnRecB;
	//JTextArea txtS;
	ButtonGroup group;
	
	public chooseGym() 
	{
		
		DataInputStream dis;
		DataOutputStream dos;
		String str;
		try {
			dis = new DataInputStream(Login_Screen.s.getInputStream());
			dos = new DataOutputStream(Login_Screen.s.getOutputStream());
			dos.writeUTF("Get_Gyms_Info");
			str = dis.readUTF();
		} catch (IOException e) {
			str = "Server Failure";
			e.printStackTrace();
			// Possibly end program here
		}
		List<String> gyms = new ArrayList<String>();
		int first = str.indexOf("|");
		int second = str.indexOf("|",first+1);
		if (str != "") {
			if(first > 0) {
				gyms.add(str.substring(0,first-1));
			} else {
				gyms.add(str);
			}
		}
		while (first != -1) {
			if (second == -1) {
				gyms.add(str.substring(first + 2));
				first = -1;
			} else {
				gyms.add(str.substring(first + 2, second - 1));
				first = second;
				second = str.indexOf("|", first + 1);
			}
		}

		group = new ButtonGroup();

		lbl_Title = new JLabel("Select ESU Gym");
		lbl_Title.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl_Title.setBounds(170, 50, 147, 32);
		getContentPane().add(lbl_Title);
		
		int last_width = 0;
		for(int I = 0; I< gyms.size();I++) {
			JRadioButton new_machine = new JRadioButton(gyms.get(I));
			new_machine.setBounds(147+last_width,100,(int) new_machine.getPreferredSize().getWidth(),23);
			group.add(new_machine);
			getContentPane().add(new_machine);
			last_width+= new_machine.getPreferredSize().getWidth();
		}
		this.setTitle("Login Page: ");
        this.setBounds(200, 200, 600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		
		
        
		
		btnMattioli = new JButton("Select Gym");
		btnMattioli.setBounds(297, 243, 185, 99);
		btnMattioli.addActionListener(this);
		getContentPane().add(btnMattioli);
		
		/*btnRecB = new JButton("Recreation Center B");
		btnRecB.setBounds(81, 243, 171, 99);
		btnRecB.addActionListener(this);
		getContentPane().add(btnRecB);
		
		lblMattioli = new JLabel("Mattioli Rec Center");
		lblMattioli.setBounds(191, 215, 61, 16);
		getContentPane().add(lblMattioli);
		
		lblRecB = new JLabel("Recreation Center B");
		lblRecB.setBounds(358, 215, 61, 16);
		getContentPane().add(lblRecB);*/
		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (e.getSource().equals(btnMattioli)) {
            try {
                processInfo1();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
	
	public void processInfo1() throws UnknownHostException, IOException 
	{

		
        DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());
        
        JOptionPane.showMessageDialog(null, "Redirecting to Reservation Window");
        
        String machine = null;
		Enumeration<AbstractButton> en = group == null ? null : group.getElements();
        while (en != null && en.hasMoreElements()) {
            AbstractButton ab = en.nextElement();
            if (ab.isSelected()) {
            	machine = ab.getText();
            }
        }
        if(machine != null) {
        Schedule_Window window = new Schedule_Window(machine);
        //chooseGym window = new chooseGym();

        window.setVisible(true);
        	this.setVisible(false);
 
        
        }else {
        	JOptionPane.showMessageDialog(null, "Gym does not exist in system");

        }

        
        
    

    }
	
	public void processInfo2() throws UnknownHostException, IOException 
	{
		Register_Screen register = new Register_Screen();
		register.setVisible(true);
		this.setVisible(false);

    }
	
	
}
