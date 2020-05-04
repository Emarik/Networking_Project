import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import java.io.*;
import java.awt.Choice;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.DefaultComboBoxModel;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

//import com.toedter.calendar.JDateChooser;

public class Schedule_Window extends JFrame implements ActionListener{
	//public static Socket s;
	JLabel label_Title;
	JLabel label_Select_Equipment;
	JLabel lblAvailability;
	
	JTable table_Availability;
	
	JCheckBox checkBox_Treadmill;
	JCheckBox checkBox_FreeWeights;
	JCheckBox checkBox_Unknown;
	
	JDateChooser dateChooser;
	JLabel lblSelectTime;
	JLabel lblSelectDate;
	JLabel lblTimeSelected_1;
	JLabel lblMachineSelected_1;
	JLabel lblNewLabel;
	Choice choice;
	
	

	JButton btnSchedule;
	//JButton btnDelete;

	JTextArea txtS;

	public Schedule_Window()
	{
		
		//Server call of get gym information; send gym name
		
		this.setTitle("Gym Scheduler");
        this.setBounds(300, 500, 800, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		label_Title = new JLabel("Gym Scheduling System");
		label_Title.setFont(new Font("Tahoma", Font.BOLD, 22));
		label_Title.setBounds(140, 11, 266, 27);
		getContentPane().add(label_Title);
		
		label_Select_Equipment = new JLabel("Select Equipment:");
		label_Select_Equipment.setBounds(28, 61, 113, 14);
		getContentPane().add(label_Select_Equipment);
		
		checkBox_Treadmill = new JCheckBox("Treadmill");
		checkBox_Treadmill.setBounds(147, 57, 97, 23);
		getContentPane().add(checkBox_Treadmill);
		
		checkBox_FreeWeights = new JCheckBox("Free Weights");
		checkBox_FreeWeights.setBounds(274, 57, 116, 23);
		getContentPane().add(checkBox_FreeWeights);
		
		checkBox_Unknown = new JCheckBox("Other?");
		checkBox_Unknown.setBounds(416, 57, 97, 23);
		getContentPane().add(checkBox_Unknown);
		
		lblAvailability = new JLabel("Availability:");
		lblAvailability.setBounds(284, 87, 72, 14);
		getContentPane().add(lblAvailability);
		
		
		String[] columnNames = {"Time", "Status"};
		String[] [] tabledata = {{"Time", "Status"},
								 {"9:00 AM", "Open"},
								 {"10:00 AM", "Busy"},
								 {"11:00 AM", "Open"},
								 {"12:00 PM", "Open"},
								 {"1:00 PM", "Busy"},
								 {"2:00 PM", "Open"},
								 {"3:00 PM", "Open"},
								 {"4:00 PM", "Busy"}, 
								 {"5:00 PM", "Open"}};
		
		table_Availability = new JTable(tabledata, columnNames);
		//scrollPane.setViewportView(table_Availability);
		table_Availability.setBounds(274, 107, 210, 179);
		getContentPane().add(table_Availability);
		
		choice = new Choice();
		choice.add("9:00AM - 10:00AM");
		choice.add("10:00AM - 11:00AM");
		choice.add("11:00AM - 12:00PM");
		choice.add("12:00PM - 1:00PM");
		choice.add("1:00PM - 2:00PM");
		choice.add("2:00PM - 3:00PM");
		choice.add("3:00PM - 4:00PM");
		choice.add("4:00PM - 5:00PM");
		choice.add("5:00PM - 6:00PM");

		choice.setBounds(28, 130, 210, 20);
		getContentPane().add(choice);
		
		lblSelectTime = new JLabel("Select Time:");
		lblSelectTime.setBounds(28, 107, 113, 14);
		getContentPane().add(lblSelectTime);
		
		lblSelectDate = new JLabel("Select Date:");
		lblSelectDate.setBounds(28, 203, 113, 14);
		getContentPane().add(lblSelectDate);
		
		dateChooser = new JDateChooser();
		dateChooser.setBounds(28, 228, 210, 20);
		getContentPane().add(dateChooser);
		
		lblMachineSelected_1 = new JLabel("MACHINE SELECTED");
		lblMachineSelected_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblMachineSelected_1.setBounds(552, 293, 140, 27);
		getContentPane().add(lblMachineSelected_1);
		
		
		lblTimeSelected_1 = new JLabel("TIME SELECTED");
		lblTimeSelected_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTimeSelected_1.setBounds(552, 348, 140, 27);
		getContentPane().add(lblTimeSelected_1);
		
		lblNewLabel = new JLabel("DATE SELECTED");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(552, 318, 140, 27);
		getContentPane().add(lblNewLabel);
	
		btnSchedule = new JButton("Schedule");
		btnSchedule.setBounds(552, 399, 103, 34);
		btnSchedule.addActionListener(this);
		getContentPane().add(btnSchedule);
		
		/*
		btnDelete = new JButton("Delete");
		btnDelete.setBounds(387, 389, 103, 34);
		add(btnDelete);
		*/
		
		txtS = new JTextArea();
	    txtS.setBounds(19, 298, 521, 220);
	    getContentPane().add(txtS);

	    this.setVisible(true);
		
		
	}
	
	public static void main(String[] args) throws IOException
	{
		//s = new Socket("localhost", 5000); // make it global variable


		new Schedule_Window();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
        if (e.getSource().equals(btnSchedule)) {
            try {
                processInformation();

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        /*
        else if (e.getSource().equals(btnDelete)) 
        {
            try {
                processInfo2();

            } catch (UnknownHostException e2) {
                e2.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        */
    }
	

	public void processInformation() throws UnknownHostException, IOException 
	{
		//Used for Testing, output to server
		//ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());
        
		DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream()); 
        DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());
		
		
		// Select Machine
		String machine = null;
		if (checkBox_Treadmill.isSelected())
		{
			machine = checkBox_Treadmill.getText().toString();	
			lblMachineSelected_1.setText("Treadmill");
			
		}
		else if (checkBox_FreeWeights.isSelected())
		{
			machine = checkBox_FreeWeights.getText().toString();	
			lblMachineSelected_1.setText("Free Weights");

		}
		else if (checkBox_Unknown.isSelected())
		{
			machine = checkBox_Unknown.getText().toString();	
			lblMachineSelected_1.setText("Other");

		}
		else
		{
        	JOptionPane.showMessageDialog(null, "Please select a machine");
        	
		}
				
		
		// Select Time
		String time = (String)choice.getSelectedItem();
		
		switch (time) 
		{
			case "9:00AM - 10:00AM": lblTimeSelected_1.setText("9AM - 10AM");
				break;					
			case "10:00AM - 11:00AM": lblTimeSelected_1.setText("10AM -11AM");
				break;					
			case "11:00AM - 12:00PM": lblTimeSelected_1.setText("11AM -12PM");
				break;					
			case "12:00PM - 1:00PM": lblTimeSelected_1.setText("12PM -1PM");
				break;			
			case "1:00PM - 2:00PM": lblTimeSelected_1.setText("1PM - 2PM");
				break;		
			case "2:00PM - 3:00PM": lblTimeSelected_1.setText("2PM - 3PM");
				break;					
			case "3:00PM - 4:00PM": lblTimeSelected_1.setText("3PM - 4PM");
				break;					
			case "4:00PM - 5:00PM": lblTimeSelected_1.setText("4PM - 5PM");
				break;
			case "5:00PM - 6:00PM": lblTimeSelected_1.setText("5PM - 6PM");
				break;
		default: lblTimeSelected_1.setText("ERROR");
		}
	
		
		// Select Date
		DateFormat Date_Selection = new SimpleDateFormat("MM-dd-yyyy");
	    lblNewLabel.setText(Date_Selection.format(dateChooser.getDate()));
		String date = Date_Selection.format(dateChooser.getDate());
        
       
        dos.writeUTF("Reserve: "+machine+ " | "+date+ " | "+time);
        boolean success = dis.readBoolean();
		
        if(!success)
        {
        	JOptionPane.showMessageDialog(null, "Not Reserved");
        	
        	
        }
        else
        {
        	JOptionPane.showMessageDialog(null, "You have reserved successfully");
        	
        }
		
		//Used for Testing, print out to Text Area
		/*
        p.writeObject(new Reserve(machine, time, date));
        p.flush();

        // Here we read the details from server
        BufferedReader response = new BufferedReader(new InputStreamReader(
                s.getInputStream()));
        txtS.setText("The server respond: \n" + response.readLine());
        p.close();
        response.close();
        s.close();
        */
    }
	
	
	
}





