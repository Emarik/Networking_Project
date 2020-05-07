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
import javax.swing.JToggleButton;

import java.io.*;
import java.awt.Choice;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
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
import java.util.*;


public class Schedule_Window extends JFrame implements ActionListener {
	JLabel label_Title;
	JLabel label_Select_Equipment;
	JLabel lblAvailability;

	JTable table_Availability;

	ButtonGroup group;

	JDateChooser dateChooser;
	JLabel lblSelectTime;
	JLabel lblSelectDate;
	JLabel lblTimeSelected_1;
	JLabel lblMachineSelected_1;
	JLabel lblNewLabel;
	Choice choice;

	JLabel lblActiveUsers;
	JLabel lblUserCount;

	JButton btnSchedule;
	// JButton btnDelete;
	JButton btnCheckIn;
	JButton btnCheckOut;

	String[] columnNames = { "Time", "Status" };
	String[][] tabledata = { { "Time", "Status" }, { "9:00AM", "Open" }, { "10:00AM", "Open" },
			{ "11:00AM", "Open" }, { "12:00PM", "Open" }, { "1:00PM", "Open" }, { "2:00PM", "Open" },
			{ "3:00PM", "Open" }, { "4:00PM", "Open" }, { "5:00PM", "Open" } };

	JTextArea txtS;

	public Schedule_Window(String gymName)
	{
		DataInputStream dis;
		DataOutputStream dos;
		String str;
		try {
			dis = new DataInputStream(Login_Screen.s.getInputStream());
			dos = new DataOutputStream(Login_Screen.s.getOutputStream());
			dos.writeUTF("Get_Gym_Info: " + gymName);
			str = dis.readUTF();
		} catch (IOException e) {
			str = "Server Failure";
			e.printStackTrace();
			// Possibly end program here
		}
		int Max_Users = 0;
		int Active_Users = 0;
		String Hours = "NAN";
		List<String> machines = new ArrayList<String>();
		if (str.equals("Server Failure")) {
			// This is if it fails.
		} else {
			int first = str.indexOf("|");
			int second = str.indexOf("|", first + 1);
			int start = str.indexOf("|", second + 1);
			int end = str.indexOf("|", start + 1);
			Max_Users = Integer.parseInt(str.substring(0, first - 1));
			Active_Users = Integer.parseInt(str.substring(first + 2, second - 1));
			Hours = str.substring(second + 2, start - 1);
			while (start != -1) {
				if (end == -1) {
					machines.add(str.substring(start + 2));
					start = -1;
				} else {
					machines.add(str.substring(start + 2, end - 1));
					start = end;
					end = str.indexOf("|", start + 1);
				}
			}
		}

		group = new ButtonGroup();

		this.setTitle("Gym Scheduler");
		this.setBounds(300, 500, 800, 650);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		label_Title = new JLabel("Gym Scheduling System");
		label_Title.setFont(new Font("Tahoma", Font.BOLD, 22));
		label_Title.setBounds(28, 12, 266, 27);
		getContentPane().add(label_Title);

		label_Select_Equipment = new JLabel("Select Equipment:");
		label_Select_Equipment.setBounds(28, 61, 113, 14);
		getContentPane().add(label_Select_Equipment);
		int last_width = 0;
		for(int I = 0; I< machines.size();I++) {
			JRadioButton new_machine = new JRadioButton(machines.get(I));
			new_machine.setBounds(147+last_width,57,(int) new_machine.getPreferredSize().getWidth(),23);
			new_machine.addActionListener(new ActionListener(){  
		    public void actionPerformed(ActionEvent e){  
	            Date_Changed(); 
	    }  
	    });  
			group.add(new_machine);
			getContentPane().add(new_machine);
			last_width+= new_machine.getPreferredSize().getWidth();
		}
		

		lblAvailability = new JLabel("Availability:");
		lblAvailability.setBounds(274, 91, 97, 14);
		getContentPane().add(lblAvailability);

		table_Availability = new JTable(tabledata, columnNames);
		table_Availability.setBounds(274, 107, 210, 179);
		table_Availability.setEnabled(false);
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
		lblSelectDate.setBounds(28, 157, 113, 14);
		getContentPane().add(lblSelectDate);

		dateChooser = new JDateChooser();
		dateChooser.setBounds(28, 175, 210, 20);
		dateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				Date_Changed();
			}
		});
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

		lblActiveUsers = new JLabel("Active Users: ");
		lblActiveUsers.setBounds(306, 22, 89, 14);
		getContentPane().add(lblActiveUsers);

		lblUserCount = new JLabel(Active_Users + "/" + Max_Users);
		lblUserCount.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblUserCount.setBounds(396, 21, 59, 16);
		getContentPane().add(lblUserCount);

		btnSchedule = new JButton("Schedule");
		btnSchedule.setBounds(552, 399, 103, 34);
		btnSchedule.addActionListener(this);
		getContentPane().add(btnSchedule);

		btnCheckIn = new JButton("Check In");
		btnCheckIn.setBounds(28, 204, 103, 34);
		btnCheckIn.addActionListener(this);
		getContentPane().add(btnCheckIn);

		btnCheckOut = new JButton("Check Out");
		btnCheckOut.setBounds(28, 250, 103, 34);
		btnCheckOut.addActionListener(this);
		getContentPane().add(btnCheckOut);

		txtS = new JTextArea();
		txtS.setBounds(19, 298, 521, 220);
		getContentPane().add(txtS);

		this.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(btnSchedule)) {
			try {
				processInformation();

			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource().equals(btnCheckIn)) {
			try {
				processInfo2();

			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		} else if (e.getSource().equals(btnCheckOut)) {
			try {
				processInfo3();

			} catch (UnknownHostException e3) {
				e3.printStackTrace();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		}

	}

	public void processInformation() throws UnknownHostException, IOException {
		// Used for Testing, output to server
		// ObjectOutputStream p = new ObjectOutputStream(s.getOutputStream());

		DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream());
		DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());

		// Select Machine
		String machine = null;
		Enumeration<AbstractButton> en = group == null ? null : group.getElements();
        while (en != null && en.hasMoreElements()) {
            AbstractButton ab = en.nextElement();
            if (ab.isSelected()) {
            	machine = ab.getText();
            }
        }
		lblMachineSelected_1.setText(machine);

		// Select Time
		String time = (String) choice.getSelectedItem();

		switch (time) {
		case "9:00AM - 10:00AM":
			lblTimeSelected_1.setText("9AM - 10AM");
			break;
		case "10:00AM - 11:00AM":
			lblTimeSelected_1.setText("10AM - 11AM");
			break;
		case "11:00AM - 12:00PM":
			lblTimeSelected_1.setText("11AM - 12PM");
			break;
		case "12:00PM - 1:00PM":
			lblTimeSelected_1.setText("12PM - 1PM");
			break;
		case "1:00PM - 2:00PM":
			lblTimeSelected_1.setText("1PM - 2PM");
			break;
		case "2:00PM - 3:00PM":
			lblTimeSelected_1.setText("2PM - 3PM");
			break;
		case "3:00PM - 4:00PM":
			lblTimeSelected_1.setText("3PM - 4PM");
			break;
		case "4:00PM - 5:00PM":
			lblTimeSelected_1.setText("4PM - 5PM");
			break;
		case "5:00PM - 6:00PM":
			lblTimeSelected_1.setText("5PM - 6PM");
			break;
		default:
			lblTimeSelected_1.setText("ERROR");
		}
		// lblTimeSelected_1.setText(time);
		// Select Date
		DateFormat Date_Selection = new SimpleDateFormat("MM-dd-yyyy");
		lblNewLabel.setText(Date_Selection.format(dateChooser.getDate()));
		String date = Date_Selection.format(dateChooser.getDate());

		dos.writeUTF("Reserve: " + machine + " | " + date + " | " + time);
		boolean success = dis.readBoolean();

		if (!success) {
			JOptionPane.showMessageDialog(null, "Not Reserved");

		} else {
			JOptionPane.showMessageDialog(null, "You have reserved successfully");
			Date_Changed();
		}

	}

	public void processInfo2() throws UnknownHostException, IOException {
		DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream());
		DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());

		// press check-in, send info
		String checkIn = "Check_In";
		dos.writeUTF(checkIn);

		boolean success = dis.readBoolean();

		if (!success) {
			JOptionPane.showMessageDialog(null, "Not Checked-In");

		} else {
			JOptionPane.showMessageDialog(null, "Checked-In");

		}

	}

	public void processInfo3() throws UnknownHostException, IOException {
		DataInputStream dis = new DataInputStream(Login_Screen.s.getInputStream());
		DataOutputStream dos = new DataOutputStream(Login_Screen.s.getOutputStream());

		// press check-out, send info
		// press check-in, send info
		String checkOut = "Check_Out";
		dos.writeUTF(checkOut);

		boolean success = dis.readBoolean();

		if (!success) {
			JOptionPane.showMessageDialog(null, "Not Checked-Out");

		} else {
			JOptionPane.showMessageDialog(null, "Checked-Out");

		}

	}

	public void Date_Changed() {
		DataInputStream dis;
		DataOutputStream dos;
		String str;
		if (dateChooser.getDate() != null) {
			try {
				dis = new DataInputStream(Login_Screen.s.getInputStream());
				dos = new DataOutputStream(Login_Screen.s.getOutputStream());

				String machine = null;
				Enumeration<AbstractButton> en = group == null ? null : group.getElements();
	            while (en != null && en.hasMoreElements()) {
	                AbstractButton ab = en.nextElement();
	                if (ab.isSelected()) {
	                	machine = ab.getText();
	                }
	            }
				
				if (machine != null) {
					DateFormat Date_Selection = new SimpleDateFormat("MM-dd-yyyy");
					lblNewLabel.setText(Date_Selection.format(dateChooser.getDate()));
					String date = Date_Selection.format(dateChooser.getDate());
					dos.writeUTF("Get_Reserve_Time: " + machine + " | " + date);
					str = dis.readUTF();
					List<String> times = new ArrayList<String>();
					int first = str.indexOf("|");
					int second = str.indexOf("|",first+1);
					if (str != "") {
						if(first > 0) {
							times.add(str.substring(0,first-1));
						} else {
							times.add(str);
						}
					}
					while (first != -1) {
						if (second == -1) {
							times.add(str.substring(first + 2));
							first = -1;
						} else {
							times.add(str.substring(first + 2, second - 1));
							first = second;
							second = str.indexOf("|", first + 1);
						}
					}
					for (int row = 1; row < table_Availability.getRowCount(); row++) {
						for (String time : times) {
							if (time.startsWith((String) table_Availability.getValueAt(row, 0))) {
								table_Availability.setValueAt("Busy", row, 1);
								break;
							} else {
								table_Availability.setValueAt("Open", row, 1);
							}
						}
					}

				} else {
					JOptionPane.showMessageDialog(null, "Please select a machine");
				}
			} catch (IOException e) {
				str = "Server Failure";
				e.printStackTrace();
				// Possibly end program here
			}
		}
	}

}
