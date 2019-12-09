
import javax.swing.*;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class MonthlyReport extends JPanel{
	JFrame f=new JFrame();
	JTextField F_NAME_FIELD=new JTextField();
	JTextArea File_name_full_name=new JTextArea();
	
	JTextArea File_Path_full_name=new JTextArea();
	JButton Select_Button;
	JLabel Select_File_Name;
	JTextArea File_Name=new JTextArea();
	JLabel Select_File_Path;
	JTextArea File_Path=new JTextArea();
	JTextField SourceFileTextField=new JTextField();
	JButton Upload;
	JTextField DestinationTextField=new JTextField();
	JLabel Result;
	JComboBox sc=null;
	JComboBox ss=null;
	JComboBox month=null;
	JComboBox year=null;
	String Month_Name;
	String YEAR_Name=null;
	String SUB_NAME_Name=null;
	String Sat_CODE_Name=null;
	JComboBox ear;
	JLabel suc=new JLabel();
	int response=2;
	FTPClient client=new FTPClient();
	MonthlyReport() 
	{
			JLabel Select_File;
			suc.setText(null);
	
			
			// Adding List for Selection of satellite 
			
			Collection col=System.getProperties().values();

			ArrayList<String> Sat_Code1=new ArrayList<>();
			Sat_Code1.add("AOCE");Sat_Code1.add("PLDH");Sat_Code1.add("Power");Sat_Code1.add("RCS");Sat_Code1.add("Sensors");Sat_Code1.add("Thermal");Sat_Code1.add("TTC");
			String m_onth[]={"January","February","March","April","May","June","July","August","September","October","November","December",};
			ArrayList<String> Sub=new ArrayList<>();
			Sub.add("as1");Sub.add("c2c");Sub.add("c2d");Sub.add("c2e");Sub.add("c2f");Sub.add("carto-1");Sub.add("carto-2");Sub.add("carto-2a");Sub.add("carto-2b");Sub.add("m01");Sub.add("mom");Sub.add("mt1");Sub.add("os2");Sub.add("rs2");Sub.add("rs2a");Sub.add("sc1");Sub.add("sl1");
			
			class ArrayListComboBoxModel1 extends AbstractListModel implements ComboBoxModel{
				private Object selectedItem;
				private ArrayList anal;
				
				public ArrayListComboBoxModel1(ArrayList Sub){
					anal=Sub;
				}
				@Override
				public Object getElementAt(int i) {
							return anal.get(i);
				}

				@Override
				public int getSize() {
					 return anal.size();
				}

				@Override
				public Object getSelectedItem() {
					return selectedItem;
				}

				@Override
				public void setSelectedItem(Object new_value) {
					selectedItem=new_value;
					
				}
				
			}

			
			ArrayListComboBoxModel1 model=new ArrayListComboBoxModel1(Sat_Code1);
			ArrayListComboBoxModel1 model1=new ArrayListComboBoxModel1(Sub);
			sc=new JComboBox(model);
			sc.setAlignmentX(0);
			sc.setAlignmentY(0);
			ss=new JComboBox(model1);
			ss.setAlignmentX(100);
			ss.setAlignmentY(500);
			JLabel S_sub=new JLabel("Satellite Code");
			add(S_sub);
			add(ss);
			ss.setEditable(true);
			JLabel S_code=new JLabel("Sub Stations");
			add(S_code);
			add(sc);
			sc.setEditable(true);
			
			JLabel S_month=new JLabel("Month");
			add(S_month);
			month=new JComboBox(m_onth);
			month.setAlignmentX(500);
			month.setAlignmentY(1000);
			add(month);
			JLabel S_year=new JLabel("Year");
			add(S_year);
			Calendar caal=new GregorianCalendar();
			int yea=caal.get(Calendar.YEAR);
			int yr[]=new int[12];String y_r[]=new String[12];//System.out.println("NOT tell the system year "+yea);
			for(int i=0;i<12;i++){yr[i]=yea+1-i;}//System.out.print(" "+yr[i]);}System.out.println();
			int aaa;String x;
			for(int i=0;i<12;i++){aaa=yr[i];x=Integer.toString(aaa);y_r[i]=x;}//System.out.print(" "+y_r[i]);}
			ear=new JComboBox(y_r);
			ear.setAlignmentX(1000);
			ear.setAlignmentY(10000);
			add(ear);

			add(Select_File=new JLabel("Select_File"));
			add(Select_Button=new JButton("Choose"));
			add(Select_File_Name=new JLabel("Select File Name : "));
			File_name_full_name.setText(null);

			JButton b=new JButton();
			b.setBounds(20,100,75,20);
			Select_Button.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					// Used for selection of file the variable use to store the 
					
					JFileChooser jFileChooser=new JFileChooser();
					int status =jFileChooser.showOpenDialog(new JPanel());
					if(status==JFileChooser.APPROVE_OPTION)
					{
						String name=jFileChooser.getSelectedFile().getName();
						String selected=jFileChooser.getSelectedFile().getAbsolutePath();
						SourceFileTextField.setText(selected);;
						File_name_full_name.setText(name);
						File_Path_full_name.setText(selected);
						//System.out.println("F_Name : "+name);
						//System.out.println("F_Path : "+selected);
						
										
						Object MONTH=month.getItemAt(month.getSelectedIndex());
						System.out.println("MONTH ="+MONTH);
						Month_Name=MONTH.toString();
						//System.out.println(" Month  ="+Month_Name);
						
						System.out.println(" "+Month_Name);
						Object Sat_CODE=sc.getItemAt(sc.getSelectedIndex()); 
						Sat_CODE_Name=Sat_CODE.toString();
						//System.out.println("Satellite Code ="+Sat_CODE);
						
						Object SUB_NAME=ss.getItemAt(ss.getSelectedIndex());
						SUB_NAME_Name=SUB_NAME.toString();
						//System.out.println("Sub Satellite="+SUB_NAME);
						
						Object YE_AR=ear.getItemAt(ear.getSelectedIndex());
						YEAR_Name=YE_AR.toString();
						//System.out.println("ear ="+YE_AR);
					}
				
				}
			});
			String dddd=File_name_full_name.getText();System.out.println("dddd="+dddd);
		  add(File_name_full_name);

				int flag=1;
			Upload=new JButton("Upload");	
			Upload.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
				
						FTPClient client=new FTPClient();
						FileInputStream fis=null;
						try{
							// Checking if any of the selection part
							//  is empty it will show please fill all details message in pop up box
							
							if(YEAR_Name==null||Month_Name==null||Sat_CODE_Name==null||SUB_NAME_Name==null||File_Path_full_name==null||File_name_full_name==null||File_Path_full_name.getText().trim().length()<=0||File_name_full_name.getText().trim().length()<=0)								
							{
								JOptionPane.showMessageDialog(f,"Please Fill All Details");
								
								f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
								
							}
							
							client.connect("10.32.10.100",21);    
							boolean log=client.login("oprnrpt","test123");
							/*
							 * for Checking the connection to FTP Server
							   
							   boolean connected=client.isConnected();
							   if(connected)
							   {
							   System.out.println("YEs it is connected");
							   }
							   else{System.out.println("Not connected");}
							
							*/
							
							client.enterLocalPassiveMode();
							
							// For converting file to Binary for safe transfer
							
							client.setFileType(FTP.BINARY_FILE_TYPE);
							
							/*
							  Get the location of the file selected.File_Path contains the path of the file
							  selected
							*/
							
							File loc=new File(File_Path_full_name.getText());
							
							// Floc contains where on Ftp server the file is to uploaded
							
							String Flo="/oprnrpt/MonthlyReports/";
							String adddd=Month_Name+"MonthlyReports"+"-"+YEAR_Name+"/"+Sat_CODE_Name+"/"+SUB_NAME_Name+"/";
							String n=File_name_full_name.getText();
							String Floc=Flo+adddd+n;
							System.out.println(Floc);
							
							ArrayList<String> list_directory=new ArrayList<String>();
							if(client.isConnected()){
								FTPFile[] files=null;
								String[] fileNames=null;
								try{
									fileNames=client.listNames("/oprnrpt/MonthlyReports/"+Month_Name+"MonthlyReports"+"-"+YEAR_Name+"/"+Sat_CODE_Name+"/"+SUB_NAME_Name+"/");
									
								}catch (Exception e) {
									e.printStackTrace();
								}
								for(String s:fileNames){
									String File_exist=Floc;
									if(File_exist.equals(s))
									{
										System.out.println(s);
										response=JOptionPane.showConfirmDialog(f,"File Already Exist on the Server Want to overwrite it","Confirm",
										JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
										if(response==JOptionPane.NO_OPTION){response=0;System.out.println("response= "+response);}
										else if(response==JOptionPane.YES_OPTION){response=1;System.out.println("response= "+response);}
										
									}
								}
							}
							
							if(response==1||response==2){
							 /* 
						    here we put our file onto InputStream
							and then use command storeFile() which store the file to the Ftp
							server on the location been set.
							*/
							
							FileInputStream inputstream=new FileInputStream(loc);
							boolean done=client.storeFile(Floc,inputstream);
							System.out.println("complete process 1 "+done);
							inputstream.close();
							System.out.println("input stream  closed ");
							if(done){System.out.println("Done");}
							
							
							// It is the popup box that have a message that file is transfered.
							  // It Also Clear the text displayed on the Selected File Name & 
							  // Selected File Path onto the application 
							
							JOptionPane.showMessageDialog(f,"File Updated Successfully");
							File_name_full_name.setText("");
							File_Path_full_name.setText("");
							f.setDefaultCloseOperation( JFrame.ABORT );
						}
						else{
							File_name_full_name.setText("");
							File_Path_full_name.setText("");
						}
				}
						catch(IOException ex){
							JOptionPane.showMessageDialog(f,"Connection to server Failed. Try Again Later.");
							//f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );ex.printStackTrace();*/
							ex.printStackTrace();
						}
						finally {
							try{
								// Logging out From Server
								
									if(client.isConnected()){
									System.out.println("Connected TO FTP");
									client.logout();
									client.disconnect();
									System.out.println("Disconnected TO FTP");	
									
					
									
						
									}
							}
								
						
							
								catch(IOException ex){
										
									ex.printStackTrace();}

						}
			
					}
						});	
			// Adding to the Html File & other Variables to the panel  
			
			String F_Name_name=File_name_full_name.getText();
			File_name_full_name.setText(F_Name_name);
			add(File_name_full_name);

			add(Select_File_Path=new JLabel("File path : "));
			
			String F_Name_path=File_Path_full_name.getText();
			File_Path_full_name.setText(F_Name_path);
			add(File_Path_full_name);
			add(Upload);
			
			// setting the view ,location ,size of the Application
			
			setLayout(new GridLayout(18,10,10,10));
			setLocation(500,30);
			setSize(362,668);

}
}





	
	

