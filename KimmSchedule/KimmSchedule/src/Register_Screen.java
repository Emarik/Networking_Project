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
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;


public class Register_Screen extends JFrame implements ActionListener {
	//public static Socket s;
	JLabel lblUsername;
	JLabel lblPassword;
	JLabel lblEmail;
	JTextField txtUsername;
	JPasswordField textPassword;
	JTextField txtEmail;
	JButton btnRegister;
	JTextArea txtS;

	
	public Register_Screen() 
	{
		this.setTitle("Register Page: ");
        this.setBounds(200, 200, 600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JLabel lbl_Title = new JLabel("Registration Form");
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
		
		lblEmail = new JLabel("Email: ");
		lblEmail.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmail.setBounds(140, 130, 90, 17);
		getContentPane().add(lblEmail);
		
		txtEmail = new JTextField();
		txtEmail.setBounds(232, 127, 140, 20);
		getContentPane().add(txtEmail);
		
		btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegister.setBounds(242, 153, 99, 32);
		btnRegister.addActionListener(this);
		getContentPane().add(btnRegister);
		
		txtS = new JTextArea();
	    txtS.setBounds(82, 197, 405, 200);
	    getContentPane().add(txtS);
		

	    this.setVisible(true);
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		//s = new Socket("localhost", 5000); // make it global variable
		
		new Register_Screen();
		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) 
	{
        if (e.getSource().equals(btnRegister)) 
        {
            try {
                processInformation();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
       
    }
	
	public void processInformation() throws UnknownHostException, IOException 
	{
		// don't forget to change socket to one session (Login.s)
		DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());
        
        String name = txtUsername.getText();
        String password = String.valueOf(textPassword.getPassword());
        String email = txtEmail.getText();
        dos.writeUTF("Create: "+name+ " - "+password+ " - "+email);
        boolean success = dis.readBoolean();
        
        if(!success)
        {
        	JOptionPane.showMessageDialog(null, "New member not created");

        	
        }
        else
        {    	
        	JOptionPane.showMessageDialog(null, "New member created");
        	Login_Screen login = new Login_Screen();
        	login.setVisible(true);
        	this.setVisible(false);
        	
        	
        }
        
        
        
        
		//Used for testing, output to server
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
	
	

}
