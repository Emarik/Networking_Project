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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
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
	
	public chooseGym() 
	{
		this.setTitle("Login Page: ");
        this.setBounds(200, 200, 600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		lbl_Title = new JLabel("Select ESU Gym");
		lbl_Title.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl_Title.setBounds(229, 69, 147, 32);
		getContentPane().add(lbl_Title);
		
		btnMattioli = new JButton("Mattioli Rec Center");
		btnMattioli.setBounds(297, 243, 185, 99);
		btnMattioli.addActionListener(this);
		getContentPane().add(btnMattioli);
		
		btnRecB = new JButton("Recreation Center B");
		btnRecB.setBounds(81, 243, 171, 99);
		btnRecB.addActionListener(this);
		getContentPane().add(btnRecB);
		
		lblMattioli = new JLabel("Mattioli Rec Center");
		lblMattioli.setBounds(191, 215, 61, 16);
		getContentPane().add(lblMattioli);
		
		lblRecB = new JLabel("Recreation Center B");
		lblRecB.setBounds(358, 215, 61, 16);
		getContentPane().add(lblRecB);
		
	}
	
	public static void main(String[] args) throws IOException
	{
		
		new Login_Screen();
		
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
        else if (e.getSource().equals(btnRecB)) {
            try {
                processInfo2();

            } catch (UnknownHostException e2) {
                e2.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
	
	public void processInfo1() throws UnknownHostException, IOException 
	{

		
        DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());
        
        String name = "Mattioli";
        dos.writeUTF("Get_Gym_Info:"+name);
        String success = dis.readUTF();
        

        if (name.equals(success))
        {
        	
        	JOptionPane.showMessageDialog(null, "Redirecting to Reservation Window");
        	Schedule_Window window = new Schedule_Window();
        	//chooseGym window = new chooseGym();

        	window.setVisible(true);
        	this.setVisible(false);
 
        }
        else
        {
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
