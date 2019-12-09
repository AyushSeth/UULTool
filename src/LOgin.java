
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class LOgin extends JPanel
{
	JFrame f=new JFrame(); // This frame is used for Contains all the JOptionPane Pop-Ups 
	JLabel usernemr;		// Label is for username 
	JTextField input_user;	// This if field where user will write username
	JTextField m_input_user;	
	/* I was taking input username and Password in JTextField "input_user" & "input_pass", but as it was common to other JFrame used so, when i was entering 
	   details in other JFrame the errors are visible or i many say reflect the other JFrames so i have to make a new JTextFieldfo main login 
			the problem was i have used same  JTextField in whole program
		 */
	JLabel pass;			// Label is for username
	JPasswordField input_pass;	// This if field where user will write passsword
	JPasswordField m_input_pass;	
	JLabel lag; 			// It store "login" as text
	//JPopupMenu jpm;
	JButton Add_new;		// In main Login screen it has this button to add a new User
	int flag=0;
	JButton Remove_user;	// In main Login screen it has this button to remove a User
	JFrame remove_user=new JFrame("Remove User"); 		// On click remove button  an new frame is created
	JFrame admin_login=new JFrame("Admin Login");		// On click remove button you need to login as admin so it is the frame for Admin Login  
	ETLTool  mf;				// Object of Main file so that i can excess class methods
	HashMap<String,String> users=new HashMap<String,String>();		// This Data Structure is used to read File in which users password and name is saved
	JFrame add_new=new JFrame("Add New User");			// In main Login screen it has this button to Add a User

	LOgin(ETLTool m)
	{
		// It loads the config files are some conditions when the file is found or not
		
		String loc="config\\UPConfig_File.txt";
		File f1 = new File(loc);
		try {
			new FileReader(f1);
		} catch (FileNotFoundException e3) {
			e3.printStackTrace();
			JOptionPane.showMessageDialog(f,"Configuration file not found . Software to be reinstalled.");
			System.exit(-1);
		}
		 
		// This methods will read the config file and store it in hash map
		
		BufferedReader br=null;
		String line;
		BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(loc));
			while ((line = reader.readLine()) != null)
			{
				String[] parts = line.split(":", 2);
				if (parts.length >= 2)
				{
					String key = parts[0];
					//System.out.println("Key :-"+key);
					String value = parts[1];
					//System.out.println("Value :-"+value);
					users.put(key, value);
				} else {
					//System.out.println("ignoring line: " + line);
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		// This is the method for remove user
		// What happens on clicking remove button from login Screen
		
		
		Remove_user=new JButton("Remove User");
		Remove_user.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				// the new JPanel is used due to new JFrame same way login is made it  is just  ac copy and paste
				
				
				JPanel add_new_panel=new JPanel();
				JOptionPane.showMessageDialog(f,"Login With Admin ID");
				usernemr=new JLabel("Username");
				add_new_panel.add(usernemr);
				input_user=new JTextField(20);
				add_new_panel.add(input_user);
				pass=new JLabel("Password");
				add_new_panel.add(pass);
				input_pass=new JPasswordField(20);
				add_new_panel.add(input_pass);
				
				JButton login=new JButton("Login");
				
				// On clicking remove button you first need to login as admin
				
				login.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e){
						String a=null;
						a=input_user.getText();
						String b=null;
						b=input_pass.getText();
						if(a==null||b==null || a.length()<=0 || b.length()<=0){
							JOptionPane.showMessageDialog(f,"Please Fill All Details");
						}else if(a.equals("admin")&&b.equals("istrac123")){
							JOptionPane.showMessageDialog(f," Admin Login Successfully !!");
							admin_login.setVisible(false);
							
							
							// After having successful login the new frame is created which ask for username to be removed
							
							
							add_new_panel.removeAll();
							JOptionPane.showMessageDialog(f,"Now if you want to remove an user, type it's Username \n "
									+ "and Click Remove Button");

							JLabel admin_loginId=new JLabel("Type the User's Username want to be removed.");
							add_new_panel.add(admin_loginId);
							usernemr=new JLabel("Username");
							add_new_panel.add(usernemr);
							input_user=new JTextField(20);
							add_new_panel.add(input_user);
							/*	input_user.addActionListener(new ActionListener()
								{
									public void actionPerformed(ActionEvent e) 
									{
										String a=input_user.getText();
										System.out.println(a);
									}
								});*/
							
							/* After entering the name of user to be removed then on clicking remove it will 
							read the file ad delete the specified username and writes its back
							*/
							
							
							JButton up1=new JButton("Remove");
							up1.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e){
									String a=null;
									a=input_user.getText();
									System.out.println("its inside remove area : "+a);
									int j=0;
									boolean contaiona=users.containsKey(a);
									if(a==null||a.length()<=0){
										JOptionPane.showMessageDialog(f,"Please Fill All Details");
									}
									else if(contaiona){
										users.remove(a);

										try {
											PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(loc)));
											for(Map.Entry<String, String> entry:users.entrySet()){
												pw.println(entry.getKey()+":"+entry.getValue());
											}
											pw.close();
											JOptionPane.showMessageDialog(f," User is removed.");
											input_user.setText("");
										} catch (IOException e1) {
											e1.printStackTrace();
										}
									}else{
										JOptionPane.showMessageDialog(f," No users with this name present to remove");
									}
								}
							});

							add_new_panel.add(up1);
							remove_user.add(add_new_panel);	
							remove_user.setVisible(true);
							remove_user.setSize(335,130);
							remove_user.setLocation(500,300);
						}
						else{
							JOptionPane.showMessageDialog(f,"Username or Password Invalid");
						}


					}
				});
				add_new_panel.add(login);
				admin_login.add(add_new_panel);
				admin_login.setVisible(true);
				admin_login.setSize(335,130);
				admin_login.setLocation(500,300);
			}

		});

// For Add New User a JFrame is called. Just Type the unique name and password and click add !! 

		Add_new=new JButton("Add New User");
		Add_new.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{

				JPanel add_new_panel=new JPanel();
				usernemr=new JLabel("Username");
				add_new_panel.add(usernemr);
				input_user=new JTextField(20);
				add_new_panel.add(input_user);
				input_user.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) 
					{
						String a=input_user.getText();
						System.out.println(a);
					}
				});

				pass=new JLabel("Password");
				add_new_panel.add(pass);
				input_pass=new JPasswordField(20);
/*				input_pass.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e) {
						@SuppressWarnings("deprecation")
						String a=input_pass.getText();
						System.out.println(a);
					}
				});*/
				add_new_panel.add(input_pass);
				JButton up=new JButton("ADD");
				up.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						String a=null;
						a=input_user.getText();
						String b=null;
						b=input_pass.getText();
						//String loc="D:\\Project Trainee\\Ayush_HCST_mathura_2019\\Extract,Update & Transfer Files\\Extract,Update&Transfer Files\\UPConfig_File.txt";
						if(a==null||b==null || a.length()<=0 || b.length()<=0){
							JOptionPane.showMessageDialog(f,"Please Fill All Details");
						}
						else if(!(users.containsKey(a)))
						{
							
						/*	 It first read a file and store it line by line in array
							list and then add the user and then again write the file back
							*/
							
						BufferedReader reader=null;
						ArrayList<String> list=null;
						list=new ArrayList<>();
						String add_User=a+":"+b;
						FileReader isr;
						try {
							reader=new BufferedReader(new FileReader(loc));
						} catch (FileNotFoundException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
						String line1;
						try {
							while((line1=reader.readLine())!=null){
								list.add(line1);
								System.out.println(line1);
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							reader.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						int j=0;
						list.add(0,add_User);
						BufferedWriter out =null;
						// File file = new File (loc); // put download address of file
						try {
							out = new BufferedWriter(new FileWriter(loc));
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
						for(j=0;j<list.size();j++){ try {
							out.write(list.get(j)+"\r\n");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}}
						try {
							out.close();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}


						// String loc="D:\\Project Trainee\\Ayush_HCST_mathura_2019\\Extract,Update & Transfer Files\\Extract,Update&Transfer Files\\UPConfig_File.txt";
						//BufferedReader br=null;
						String line;
						//BufferedReader reader=null;
						System.out.println("new File Where All are Added");
						users.clear();
						for(int i=0;i<users.size();i++){System.out.println(users.get(i));}
						System.out.println("new File Where All are Added");
						reader=null;
						try {
							reader = new BufferedReader(new FileReader(loc));
							while ((line = reader.readLine()) != null)
							{
								String[] parts = line.split(":", 2);
								if (parts.length >= 2)
								{
									String key = parts[0];
									System.out.println("Key :-"+key);
									String value = parts[1];
									System.out.println("Value :-"+value);
									users.put(key, value);
								} else {
									System.out.println("ignoring line: " + line);
								}
							}
							JOptionPane.showMessageDialog(f,"Added to Directory");
							input_user.setText("");
							input_pass.setText("");
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					}
						else
						{JOptionPane.showMessageDialog(f,"Username Already Register. Try using other username");
						input_user.setText("");
						input_pass.setText("");
						}
					}

				});
				add_new_panel.add(up);
				add_new.add(add_new_panel);
				add_new.setVisible(true);
				add_new.setSize(335,130);
				add_new.setLocation(500,300);
			}

		});
		
		// Now we need to remove frames of both add users and remove users if opened
		
		add_new.setVisible(false);
		remove_user.setVisible(false);
		mf = m;    // This is the way to call the class methods
		usernemr=new JLabel("Username");
		add(usernemr);
		m_input_user=new JTextField(20);
		add(m_input_user);
		m_input_user.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent arg0) {
				m_input_pass.setText("");
			}
		});

		pass=new JLabel("Password");
		add(pass);
		m_input_pass=new JPasswordField(20);
		/*input_pass.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("deprecation")
				String a=input_pass.getText();
				System.out.println(a);
			}
		});*/

		add(m_input_pass);
		lag=new JLabel("Login");

		JButton up=new JButton("Login");
		up.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String a=null;
				a=m_input_user.getText();
				String b=null;
				b=m_input_pass.getText();
				if(a==null||b==null || a.length()<=0 || b.length()<=0){
					JOptionPane.showMessageDialog(f,"Please Fill All Details ");
					m_input_user.setText("");
					m_input_pass.setText("");
				}else{
					String s = users.get(a);
					if(s!=null){
						boolean founf=s.equals(b);

						System.out.println(a);
						System.out.println(b);
						if(founf)
						{
							JLabel res=new JLabel("Sucessful admin Login");
							flag=1;
							
							// it shows a popup screen giving instructions 
							
							JOptionPane.showMessageDialog(f,"Hello , This application is made to make"
									+ " your work easy during uploading the files onto the server\n\n"
									+ "Follow Few Steps : \n"
									+ "1) Configure your system first i.e. Create a folder and name it \"backup\" "
									+ "in your C Drive.\n     * ( It should be in small alphabets only )\n     * ( This step "
									+ "is necessary )"
									+ "\n2) If You are not Register to the directory then first add yourself "
									+ "give unique Username	\n     and Password \n     * ( length is not an the issue )\n"
									+ "     Close Both the Window i.e (Add New User Window And UUL Tool Login Window) \n     and then open the UUL Tool again.\n"
									+ "3) The software is able to Upload 4 Types of Reports "
									+ "\n     Monthly Reports (MR), MOCB Reports (MOCB), Special operations Reports (SOP), \n"
									+ "     & Study reports (SR)."
									+ "\n     As the Software is still in development mode it will take some time "
									+ "for others Reports \n     to add for the Uploads.\n"
									+ "4) The software is easy to handle just, first select which report to be upload\n"
									+ "     e.g. say MR Report click the MR Tab select which Year,which Satellite ,which Sub-Station\n     "
									+ "then select File	 to be Uploaded \n"
									+ "     WARNING : YOU NEED TO RENAME YOUR FILE ACCORDING TO THE DATE , YEAR AND\n"
									+ "     REPORT NAME BY YOURSELF BEFORE CLICKING UPLOAD "
									+ "\n    * ( RENAME THE FILE BEFORE UPLOAD )\n"
									+ "     After RENAMING File, just click Upload button . \n"
									+ "     \n    EASY TO UPLOAD A FILE !!");

							// Call the main frames that contains the 4 tabs.
							
							
							mf.main_frame();
							try
							{
								TimeUnit.SECONDS.sleep(1);
								add(res);
							} 
							catch (InterruptedException e1) 
							{
								e1.printStackTrace();
							}
						}else{
							JOptionPane.showMessageDialog(f,"Password not correct");
						}

					}else
					{
						JOptionPane.showMessageDialog(f,"Username not valid");
					}
				}
			}

			/*if(flag==1)
	{
		return true;
		}
	else
	{
	return false;
	}
	}*/


		});
		add(Add_new);
		add(Remove_user);
		add(up);
	}

}
