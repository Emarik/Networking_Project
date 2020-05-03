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
	JLabel lblUsername;
	JLabel lblPassword;
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
		lbl_Title.setBounds(113, 11, 158, 32);
		getContentPane().add(lbl_Title);
		
		lblUsername = new JLabel("Username: ");
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setBounds(74, 81, 90, 17);
		add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(162, 81, 140, 20);
		add(txtUsername);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblPassword.setBounds(74, 132, 90, 17);
		add(lblPassword);
		
		textPassword = new JPasswordField();
		textPassword.setBounds(162, 129, 140, 20);
		add(textPassword);
		
		btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setBounds(65, 198, 99, 32);
		btnLogin.addActionListener(this);
		add(btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegister.setBounds(175, 198, 99, 32);
		btnRegister.addActionListener(this);
		add(btnRegister);
		
		txtS = new JTextArea();
	    txtS.setBounds(70, 250, 290, 120);
	    add(txtS);

	    this.setVisible(true);
		
		
	}
	
	public static void main(String[] args)
	{
		
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
        Socket s = new Socket("localhost", 5000);
        ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

        String name = txtUsername.getText();
        String password = String.valueOf(textPassword.getPassword());

        p.writeObject(new Member(name, password));
        p.flush();
        
   
        // Here we read the details from server
        BufferedReader response = new BufferedReader(new InputStreamReader(
                s.getInputStream()));
        txtS.setText("The server respond: \n" + response.readLine());
        p.close();
        response.close();
        s.close();
    }
	
	public void processInfo2() throws UnknownHostException, IOException 
	{
        Socket s = new Socket("localhost", 5000);
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
    }
	
	
}
