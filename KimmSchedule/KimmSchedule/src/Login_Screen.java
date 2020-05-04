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


public class Login_Screen extends JFrame implements ActionListener{
	public static Socket s;
	JLabel lblUsername;
	JLabel lblPassword;
	JLabel lblRegister;
	JTextField txtUsername;
	JPasswordField textPassword;
	JButton btnLogin;
	JButton btnRegister;
	JTextArea txtS;
	
	public Login_Screen() 
	{
		this.setTitle("Login Page: ");
        this.setBounds(200, 200, 600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lbl_Title = new JLabel("ESU Gym Check-In");
		lbl_Title.setFont(new Font("Tahoma", Font.BOLD, 16));
		lbl_Title.setBounds(193, 18, 158, 32);
		getContentPane().add(lbl_Title);
		
		lblUsername = new JLabel("Username: ");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setBounds(130, 64, 90, 17);
		getContentPane().add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(232, 63, 140, 20);
		getContentPane().add(txtUsername);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPassword.setBounds(130, 96, 90, 17);
		getContentPane().add(lblPassword);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(232, 95, 140, 20);
		getContentPane().add(textPassword);
		
		lblRegister = new JLabel("Need to register? ");
		lblRegister.setBounds(118, 168, 119, 17);
		getContentPane().add(lblRegister);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setBounds(252, 127, 99, 32);
		btnLogin.addActionListener(this);
		getContentPane().add(btnLogin);
		
		btnRegister = new JButton("Click Here");
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegister.setBounds(232, 160, 140, 32);
		btnRegister.addActionListener(this);
		getContentPane().add(btnRegister);
		
		txtS = new JTextArea();
	    txtS.setBounds(82, 197, 405, 200);
	    getContentPane().add(txtS);

	    this.setVisible(true);
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		s = new Socket("localhost", 8788); // make it global variable
		
		new Login_Screen();
		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (e.getSource().equals(btnLogin)) {
            try {
                processInfo1();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        else if (e.getSource().equals(btnRegister)) {
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

		
        DataInputStream dis = new DataInputStream(s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        
        String name = txtUsername.getText();
        String password = String.valueOf(textPassword.getPassword());
        dos.writeUTF("Login: "+name+ " - "+password);
        boolean success = dis.readBoolean();
        
        // tell the gui to either go to login screen with user verification failed,
        // or go to gym screen and welcome
        if(!success)
        {
        	JOptionPane.showMessageDialog(null, "Invalid username or password");
        	
        	
        }
        else
        {
        	JOptionPane.showMessageDialog(null, "You are successfully logged in");
        	Schedule_Window window = new Schedule_Window();
        	window.setVisible(true);
        	this.setVisible(false);
        	
        }
    
        
		/*
		ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

        String name = txtUsername.getText();
        String password = String.valueOf(textPassword.getPassword());

        
        p.writeObject(new Member(name, password));
        p.flush();

        // Here we read the details from server
        BufferedReader response = new BufferedReader(new InputStreamReader(
                s.getInputStream()));
        txtS.setText("The server respond: " + response.readLine());
        p.close();
        response.close();
        s.close();
        */


    }
	
	public void processInfo2() throws UnknownHostException, IOException 
	{
		Register_Screen register = new Register_Screen();
		register.setVisible(true);
		this.setVisible(false);

    }
	
	
}
