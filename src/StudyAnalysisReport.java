
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class StudyAnalysisReport extends JPanel {
	JFrame f=new JFrame();	// This frame is used for Contains all the JOptionPane Pop-Ups
	JLabel Select_File;		// It is a text "select file" came in the tabs 
	JButton Select_Button;		// It is a button "Choose" came in the tabs 
	JLabel Select_File_Name;	// Its contains the text select file name	
	JTextArea File_Name=new JTextArea();		// Its stores the text file name	
	JLabel Select_File_Path;		// Its contains the text select file path
	JTextArea File_Path=new JTextArea();	// Its stores the text file path
	JTextField SourceFileTextField=new JTextField();		// It stores the file name
	JButton Upload;								// upload button 
	JTextField DestinationTextField=new JTextField();	// Not used in the program
	JLabel Result;			// Not used in the program	
	
	/*		All are drop box as requirements		*/
	
	JComboBox sc;				
	JComboBox ss;				
	JComboBox month;			
	JComboBox year;					
	JTextArea File_Name_Without_Extension=new JTextArea();			
	String Month_Name;					
	String YEAR_Name;		
	String SUB_NAME_Name;
	String Sat_CODE_Name;
	JLabel suc=new JLabel();
	JTextArea Selected_File=new JTextArea();
	int response=2; 	// It is used as a flag if the file want to upload if it already exist on the server
	FTPClient client=new FTPClient();
	StudyAnalysisReport(){
		int flag=0;
	System.out.println("Helo -3");
	
	// Adding List for Selection of satellite 
	
	Collection col=System.getProperties().values();
	ArrayList<String> Sub=new ArrayList<>();
	Sub.add("AS1");Sub.add("C02");Sub.add("C2A");Sub.add("C2B");Sub.add("CA1");Sub.add("EM1");Sub.add("HY1");Sub.add("IMS-01");Sub.add("IP6");Sub.add("M01");Sub.add("MOM");Sub.add("MT1");Sub.add("OS2");Sub.add("R2B");Sub.add("RI1");Sub.add("RS2");Sub.add("RS2A");Sub.add("YS1");
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

	ArrayListComboBoxModel1 model1=new ArrayListComboBoxModel1(Sub);
	JLabel S_sub=new JLabel("Satellite Code");
	ss=new JComboBox(model1);
	ss.setAlignmentX(100);
	ss.setAlignmentY(500);
	add(S_sub);
	add(ss);
	ss.setEditable(true);
	/*JLabel S_year=new JLabel("Year");
	
	System.out.println("Helo -4");

	add(S_year);
	Calendar caal=new GregorianCalendar();
	int yea=caal.get(Calendar.YEAR);
	int yr[]=new int[12];String y_r[]=new String[12];System.out.println("NOT tell the system year "+yea);
	for(int i=0;i<12;i++){yr[i]=yea+1-i;System.out.print(" "+yr[i]);}System.out.println();int aaa;String x;
	for(int i=0;i<12;i++){aaa=yr[i];x=Integer.toString(aaa);y_r[i]=x;System.out.print(" "+y_r[i]);}
	year=new JComboBox(y_r);
	year.setAlignmentX(1000);
	year.setAlignmentY(10000);
	add(year);
*/
		
	add(Select_File=new JLabel("Select_File"));
	add(Select_Button=new JButton("Choose"));
	add(Select_File_Name=new JLabel("Select File Name : "));

	// For selection of file from local system
	
	JButton b=new JButton();
	b.setBounds(20,100,75,20);
	Select_Button.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser jFileChooser=new JFileChooser();
			int status =jFileChooser.showOpenDialog(new JPanel());
			if(status==JFileChooser.APPROVE_OPTION)
			{
				String name=jFileChooser.getSelectedFile().getName();
				String selected=jFileChooser.getSelectedFile().getAbsolutePath();
				String Filename_without_extension=FilenameUtils.removeExtension(name);
				SourceFileTextField.setText(selected);
				File_Name.setText(name);
				File_Path.setText(selected);
				File_Name_Without_Extension.setText(Filename_without_extension);
				System.out.println("F_Name : "+name);
				System.out.println("F_Path : "+selected);
				
				/*	Objects are created to get selected fields	*/
								
				Object SUB_NAME=ss.getItemAt(ss.getSelectedIndex());
				SUB_NAME_Name=SUB_NAME.toString();
				System.out.println("Satelite Code  ="+SUB_NAME_Name);
				Selected_File.setText(SUB_NAME.toString());
				
			}
		}
	});


		 flag=0;
	Upload=new JButton("Upload");	
	Upload.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
		
			System.out.println("FTP CLIENT CODE COME IN 1");
			FTPClient client=new FTPClient();
			FileInputStream fis=null;
			try{
				if(SUB_NAME_Name==null||File_Path.getText().trim().length()<=0||File_Name.getText().trim().length()<=0)								
				{
					
					JOptionPane.showMessageDialog(f,"Please Fill All Details");
					//setVisible(true);
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
				System.out.println("FTP CLIENT CODE COME IN 2");
				
				// For converting file to Binary for safe transfer
				
				client.setFileType(FTP.BINARY_FILE_TYPE);
				
				/*
				  Get the location of the file selected.File_Path contains the path of the file
				  selected
				*/
				
				File loc=new File(File_Path.getText());
				System.out.println("FTP CLIENT CODE COME IN 4");
				
				// Floc contains where on Ftp server the file is to uploaded
				
				String Flo="/oprnrpt/AnomalyDatabase/";
				String adddd=SUB_NAME_Name+"_Reports"+"/"+"Study_Reports"+"/";
				String n=File_Name.getText();
				String Floc=Flo+adddd+n;
				System.out.println(Floc);
				
				
				ArrayList<String> list_directory=new ArrayList<String>();
				if(client.isConnected()){
					FTPFile[] files=null;
					String[] fileNames=null;
					try{
						fileNames=client.listNames("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports/Study_Reports/");
						
					}catch (Exception e) {
						e.printStackTrace();
					}
					for(String s:fileNames){
						String File_exist=Floc;
						if(File_exist.equals(s))
						{
							//System.out.println(s);
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
				String linkadd;
				String linkadd1;
				String aa;
				String FName;
				String end;
				String F_add_html;
				JTextArea WhatTo_add=new JTextArea();
				String loc_mocb;
				String loc_mocb1;
				String loc_mocb12;
				JTextArea htmlloc=new JTextArea();
				JTextArea htmlloc_INSUB=new JTextArea();
				JTextArea comment_put=new JTextArea();
				String F_add;
				/*
			 	The problem was seen that for selected satellite inside them the Special 
			 	operation report i.e.(the way used to write or i can say the name of that folder
			 	was not same for)
			 	Meaning :- 
			 	For example :
			 	1) I want to upload sop_file in AS1 Satellite 
			 		i am  inside FTPServer>Anamoly Database>AS1_Reports>Special_Operation_report
			 	
			 	2) I want to upload sop_file in C2C Satellite 
			 	i am  inside FTPServer>Anamoly Database>C2C_Reports>Special Operation report
			 	
			 	Do you see the problem there was differed 
			 	way of writing Special Operation report in C2C &
			 	Special_Operation_report in AS1.
			 	
			 	This was seen in all the Satellite Reports for both SOP & Study Reports
			 	
			 	Thats why i have used If-Else Condition to get correct location path for
			    each satellite.
			    This was the way i have came up with If-Else ,though there would be other 
			    good way to do it but this one i was able to make it happens
			 	
			
			*/

				// Some Information about the variable used inside if-Else are defined after
				//	the end of If-Else where the are used to upload file.
				// Scroll down until If-Else ends
				
				
				if(Selected_File.getText().equals("C2A")||Selected_File.getText().equals("OS2")||Selected_File.getText().equals("IP6")||Selected_File.getText().equals("AS1")||Selected_File.getText().equals("EM1")||Selected_File.getText().equals("HY1")||Selected_File.getText().equals("M01")||Selected_File.getText().equals("MOM")||Selected_File.getText().equals("MT1")||Selected_File.getText().equals("R2B")||Selected_File.getText().equals("RI1")||Selected_File.getText().equals("RS2")||Selected_File.getText().equals("RS2A"))
				{
					if(Selected_File.getText().equals("AS1"))
					{
					 linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-18.0pt;line-height:150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;color:#002060'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#002060'><a href=\"Study_Reports/";
					 linkadd1= File_Name.getText();
					 aa="\">";
					 FName=File_Name_Without_Extension.getText();
					 end="</a><o:p></o:p></span></b></p>"; 
					 F_add=linkadd+linkadd1+aa+FName+end;
					 WhatTo_add.setText(F_add);
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
					}
					if(Selected_File.getText().equals("EM1"))
					{
						linkadd="<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;line-height: 150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span class=MsoHyperlink><span style='font-size:13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family: Symbol;mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span></span><![endif]><span class=MsoHyperlink><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\"'><a href=\"Study_Reports/";
    					 linkadd1= File_Name.getText();
						 aa="\">";
						 FName=File_Name_Without_Extension.getText();
						 end="</a><o:p></o:p></span></b></span></p>"; 
						 F_add=linkadd+linkadd1+aa+FName+end;
						 WhatTo_add.setText(F_add);
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
									
					}
					if(Selected_File.getText().equals("HY1"))
					{
						linkadd="<p class=MsoListParagraph style='text-indent:-18.0pt;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:Symbol;  mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;color:#0066CC'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><u><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:\"Arial\",\"sans-serif\";color:#0066CC'><a href=\"Study_Reports/";
					 linkadd1= File_Name.getText();
					 aa="\">";
					 FName=File_Name_Without_Extension.getText();
					 end="</a><o:p></o:p></span></u></b></p>"; 
					 F_add=linkadd+linkadd1+aa+FName+end;
					 WhatTo_add.setText(F_add);
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
					}
					if(Selected_File.getText().equals("M01"))
					{
						 linkadd= "<p class=MsoListParagraphCxSpFirst style='text-indent:-18.0pt;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:Symbol; mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;color:#0066CC'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><u><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:\"Arial\",\"sans-serif\";color:#0066CC'><o:p><span style='text-decoration:none'>&nbsp;</span><a href=\"Study_Reports/";
    					 linkadd1= File_Name.getText();
						 aa="\">";
						 FName=File_Name_Without_Extension.getText();
						 end="</a></o:p></span></u></b></p>"; 
						 F_add=linkadd+linkadd1+aa+FName+end;
						 WhatTo_add.setText(F_add);
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
									
					}if(Selected_File.getText().equals("MOM"))
					{
						linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-18.0pt;mso-list:l0 level1 lfo2; mso-yfti-cnfc:68'><![if !supportLists]><span class=MsoHyperlink><span style='font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:Symbol;  mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </span></span></span></span><![endif]><span class=MsoHyperlink><span style='font-size:14.0pt;font-family:\"Arial\",\"sans-serif\"'><a href=\"Study_Reports/";
    					 linkadd1= File_Name.getText();
						 aa="\">";
						 FName=File_Name_Without_Extension.getText();
						 end="</a><o:p></o:p></span></span></p>"; 
						 F_add=linkadd+linkadd1+aa+FName+end;
						 WhatTo_add.setText(F_add);
							htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
    						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						}
						if(Selected_File.getText().equals("MT1"))
						{
							linkadd="<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span  class=MsoHyperlink><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt; font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol; text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><u><span  style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:\"Arial\",\"sans-serif\"; color:#0066CC'><a href=\"Study_Reports/";
	    					 linkadd1= File_Name.getText();
							 aa="\">";
							 FName=File_Name_Without_Extension.getText();
							 end="<o:p></o:p></a></span></u></b></p>"; 
							 F_add=linkadd+linkadd1+aa+FName+end;
							 WhatTo_add.setText(F_add);
							htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
	   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
										
						}if(Selected_File.getText().equals("R2B"))
						{
							 linkadd="<p class=MsoNormal align=center style='text-indent:-.25in;line-height:150%; mso-list:l0 level1 lfo2'><b  style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;mso-bidi-font-size: 12.0pt;font-family:\"Arial\",\"sans-serif\";color:red'></span></b><b style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;mso-bidi-font-size:12.0pt;font-family:\"Arial\",\"sans-serif\";color:#0070C0'><a href=\"Study_Reports/";
	    					 linkadd1= File_Name.getText();
							 aa="\">";
							 FName=File_Name_Without_Extension.getText();
							 end="</a><o:p></o:p></span></b></p>"; 
							 F_add=linkadd+linkadd1+aa+FName+end;
							 WhatTo_add.setText(F_add);
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
	    						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
							}
							if(Selected_File.getText().equals("RI1"))
							{
								System.out.println("yes iside if of RTI");
								linkadd="<p class=MsoListParagraphCxSpLast style='text-indent:-.25in;line-height:150%; mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol;  mso-bidi-font-family:Symbol;color:#002060'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#002060'><a href=\"Study_Reports/";
		    					 linkadd1= File_Name.getText();
								 aa="\">";
								 FName=File_Name_Without_Extension.getText();
								 end="</a><o:p>&nbsp;</o:p></span></b></p>"; 
								 F_add=linkadd+linkadd1+aa+FName+end;
								 WhatTo_add.setText(F_add);
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
		   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
											
							}
							if(Selected_File.getText().equals("RS2"))
							{
								linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-18.0pt;line-height: 150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size: 13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#002060'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\"; mso-fareast-font-family:\"Times New Roman\";color:#002060'><a href=\"Study_Reports/";
		    					 linkadd1= File_Name.getText();
								 aa="\">";
								 FName=File_Name_Without_Extension.getText();
								 end="</a><o:p></o:p></span></b></p>"; 
								 F_add=linkadd+linkadd1+aa+FName+end;
								 WhatTo_add.setText(F_add);
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
		   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
											
							}
							if(Selected_File.getText().equals("RS2A"))
							{		
								linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-18.0pt;line-height: 150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:  13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#002060'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#002060'><a href=\"Study_Reports/";
		    					 linkadd1= File_Name.getText();
								 aa="\">";
								 FName=File_Name_Without_Extension.getText();
								 end="</a><o:p></o:p></span></b></p>"; 
								 F_add=linkadd+linkadd1+aa+FName+end;
								 WhatTo_add.setText(F_add);
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
		   						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
											
							}
							if(Selected_File.getText().equals("OS2")){
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
								comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
								linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-18.0pt;line-height:125%;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-family:  Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;color:#7030A0'><span	  style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-family:\"Arial Black\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\"; color:#7030A0'><a href=\"Study_Reports/";
		    					linkadd1= File_Name.getText();
								aa="\">";
								FName=File_Name_Without_Extension.getText();
								end="</a><o:p></o:p></span></b></p>"; 
								F_add=linkadd+linkadd1+aa+FName+end;
								WhatTo_add.setText(F_add);
							}
							if(Selected_File.getText().equals("IP6")){
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
								comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
								linkadd="<p class=MsoListParagraphCxSpMiddle style='margin-left:.25in;mso-add-space: auto;text-indent:-.25in;line-height:150%;mso-list:l0 level1 lfo5'><![if !supportLists]><span style='font-size:14.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family: Symbol;mso-bidi-font-family:Symbol;color:#7030A0'><span style='mso-list:Ignore'>·<span  style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span  style='font-size:14.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";  mso-fareast-font-family:\"Times New Roman\";color:#7030A0'><a href=\"Study_Reports/";
		    					linkadd1= File_Name.getText();
								aa="\">";
								FName=File_Name_Without_Extension.getText();
								end="</a><o:p></o:p></span></b></p>"; 
								F_add=linkadd+linkadd1+aa+FName+end;
								WhatTo_add.setText(F_add);
							}
							if(Selected_File.getText().equals("C2A")){
								htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html");
								comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
								  

								linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;line-height:150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#0066CC'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><u><span style='font-size:13.0pt;line-height:150%;color:#0066CC'><a href=\"Study_Reports/";
		    					linkadd1= File_Name.getText();
								aa="\">";
								FName=File_Name_Without_Extension.getText();
								end="</a><o:p></o:p></span></u></b></p>"; 
								F_add=linkadd+linkadd1+aa+FName+end;
								WhatTo_add.setText(F_add);
							}
							
							
				}
				else
				{
					if(Selected_File.getText().equals("C02")){
						htmlloc_INSUB.setText("c02");
						String add_loc_name=htmlloc_INSUB.getText();
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+add_loc_name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#0070C0'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#0070C0'><a  href=\"Study_Reports/";
    					linkadd1= File_Name.getText();
						aa="\">";
						FName=File_Name_Without_Extension.getText();
						end="</a><o:p></o:p></span></b></p>"; 
						F_add=linkadd+linkadd1+aa+FName+end;
						WhatTo_add.setText(F_add);
							
					}
					if(Selected_File.getText().equals("C2B")){
						htmlloc_INSUB.setText("c2b");
						String add_loc_name=htmlloc_INSUB.getText();
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+add_loc_name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						linkadd="<p class=MsoListParagraphCxSpFirst style='margin-top:12.0pt;margin-right: 0in;margin-bottom:12.0pt;margin-left:.5in;mso-add-space:auto;text-indent:-.25in;line-height:120%;mso-list:l0 level1 lfo2;mso-yfti-cnfc:68'><![if !supportLists]><span style='font-size:14.0pt;line-height:120%;font-family:Symbol;mso-fareast-font-family: Symbol;mso-bidi-font-family:Symbol;color:#0070C0;mso-bidi-font-weight:bold'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span><![endif]><b><span style='font-size:14.0pt;line-height: 120%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\"; color:#0070C0'><a  href=\"Study_Reports/";
    					linkadd1= File_Name.getText();
						aa="\"><span style='font-weight:normal'>";
						FName=File_Name_Without_Extension.getText();
						end="</span></a><o:p></o:p></span></b></p>"; 
						F_add=linkadd+linkadd1+aa+FName+end;
						WhatTo_add.setText(F_add);
						

					}
					if(Selected_File.getText().equals("CA1")){
						htmlloc_INSUB.setText("ca1");
						String add_loc_name=htmlloc_INSUB.getText();
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+add_loc_name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						linkadd="<p class=MsoListParagraphCxSpFirst style='margin-left:.75in;mso-add-space:auto;text-indent:-.25in;line-height:150%;mso-list:l0 level1 lfo4'><![if !supportLists]><span class=MsoHyperlink><span style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span></span><![endif]><b><span style='font-size:14.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#0070C0'><a href=\"Study_Reports/";
    					linkadd1= File_Name.getText();
						aa="\">";
						FName=File_Name_Without_Extension.getText();
						end="</a></span></b><span class=MsoHyperlink><b style='mso-bidi-font-weight:normal'><span style='font-family:\"Arial\",\"sans-serif\"'><o:p></o:p></span></b></span></p>"; 
						F_add=linkadd+linkadd1+aa+FName+end;
						WhatTo_add.setText(F_add);
					}
					if(Selected_File.getText().equals("IMS-01")){
						htmlloc_INSUB.setText("ims01");
						String add_loc_name=htmlloc_INSUB.getText();
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+add_loc_name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						linkadd="<p class=MsoListParagraph style='text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#00B0F0'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family: \"Times New Roman\";color:#0070C0'></span></b><b style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#00B0F0'><a href=\"Study_Reports/";
    					linkadd1= File_Name.getText();
						aa="\">";
						FName=File_Name_Without_Extension.getText();
						end="</a><o:p></o:p></span></b></p>"; 
						F_add=linkadd+linkadd1+aa+FName+end;
						WhatTo_add.setText(F_add);
					}
					

					
					if(Selected_File.getText().equals("YS1")){
						htmlloc_INSUB.setText("ys1");
						String add_loc_name=htmlloc_INSUB.getText();
						htmlloc.setText("/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+add_loc_name+"_study.html");
						comment_put.setText("<!--ayushTheGtearDevelopEROfUNIVerse-->");
						linkadd="<p class=MsoListParagraphCxSpMiddle style='text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span style='font-size:14.0pt;font-family:Symbol;mso-fareast-font-family:Symbol; mso-bidi-font-family:Symbol;color:#00B0F0'><span style='mso-list:Ignore'>·<span  style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span></span></span><![endif]><b style='mso-bidi-font-weight:normal'><span style='font-size:14.0pt;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\";color:#00B0F0'><a href=\"Study_Reports/";
    					linkadd1= File_Name.getText();
						aa="\">";
						FName=File_Name_Without_Extension.getText();
						end="</a><o:p></o:p></span></b></p>"; 
						F_add=linkadd+linkadd1+aa+FName+end;
						WhatTo_add.setText(F_add);
					}
					
				}
				/*	
				  All the comments and what link is to be added in Array List is defined 
				  in the If-Else condition specified in above Code	
				  
				  Here are short details :-
				  ________________________
				  
				  1) WhatTo_add (a JTextArea contains the link to be added in Array List)
				  2) F_add_html (a String that holds the text of the WhatTo_add )
				        i.e. like :  F_add_html=WhatTo_add.getText();
				        ( Finally the result is that F_add_html contains the Link to be added
				         in the Array List )
				  3) comment(a String that holds the comment that is set in the original
				  			FTP_SOP_HTML_file in order to locate where the HTML Link to be added
				  			 in that SOP_HTML File)
				*/
				
				
				// If the Link of Uploaded File is displaying on wrong position then :-
				
				/*
				 double click the Html File or open the SOP Of that satellite from website
				 If the link is added on some other location (i.e. other than the location 
				 it should come).
				 Open the Html file in Notepad or other editors and check whether the comment
				 is set on the correct location or is been removed .
				 
				 If Removed:
				 Then From the code go to If-Else condition and copy the comment from 
				 corresponding Satellite,copy it to the html file 
				 opened in notepad or in some other application.
				 Add it just Before the last link being set on the file.
				 Otherwise it will show the link on the different location
				 
				 Be Cautious : there should be no spacing between the comment i.e. comment
				 should be set on the extreme left.With NO SPACEING .Then only it will work
				 otherwise it will fail again.   
				*/
				
				
				/*String linkadd ="<p class=MsoListParagraphCxSpFirst style='text-indent:-.25in;line-height:150%;mso-list:l0 level1 lfo2'><![if !supportLists]><span class=MsoHyperlink><span style='font-size:13.0pt;line-height:150%;font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span></span><![endif]><span class=MsoHyperlink><b style='mso-bidi-font-weight:normal'><span style='font-size:13.0pt;line-height:150%;font-family:\"Arial\",\"sans-serif\";mso-fareast-font-family:\"Times New Roman\"'><a href=\"../EM1_Reports/Study_Analysis/";
				String F_name= File_Name.getText(); 
				//String ff=     <p class=MsoNoSpacing style='margin-left:.25in;text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span class=MsoHyperlink><span style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<spanstyle='font:7.0pt "Times New Roman"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span></span><![endif]><span class=MsoHyperlink><bstyle='mso-bidi-font-weight:normal'><span style='font-family:"Arial","sans-serif"'><ahref="MOCB_16May19_C2C_C2D_CA1.pdf">MOCB_16May19_C2C_C2D_CA1</a><o:p></o:p></span></b></span></p>
				String aa="\">";
				String FName=File_Name_Without_Extension.getText();
				String end="</a><o:p></o:p></span></b></span></p>"; 
				String F_add_html=linkadd+F_name+aa+FName+end;
				System.out.println("F_add_html   :"+F_add_html);
				String loc_mocb="/oprnrpt/AnomalyDatabase/EM1_Reports/";
				//String Name="mocb";
				String comment="<!--ayushTheGtearDevelopEROfUNIVerseaaa-->";
				System.out.println("Yes");
				*//*boolean connected=client.isConnected();
				if(connected){System.out.println("YEs it is connected");}else{System.out.println("YEs it is connected");}
				*/
				
				F_add_html=WhatTo_add.getText();
				 BufferedReader reader=null;
			     BufferedWriter writer=null;
			     ArrayList<String> list=null;
			     list=new ArrayList<>();
			     String sop=htmlloc.getText();

					/*
					 Download html file i.e. sop file of selected satellite  onto the local 
					 system in your backup folder. Here we are only adding the HTML link of the 
					 File(SOP Satellite HTML File) want to be uploaded onto the FTP Server  
					 file. 				
					 Here we are 1st reading the whole HTML File line by line and storing it
					 into a array list
					 2nd Step is to search for the comment set in the original HTML File
					 with the help of( int j ) finding the index of that comment .
					 3rd Step after getting Some integer value in j then adding our Html Link
					 on j+1 location in Array List.  							 					 
					*/
			     if(response==2){
				InputStreamReader isr=new InputStreamReader(client.retrieveFileStream(sop));
				try{
					reader=new BufferedReader(isr);
					String line;
					while((line=reader.readLine())!=null){
						list.add(line);
						System.out.println(line);
					}
					reader.close();
					String comment=comment_put.getText();
					boolean cont=list.contains(comment);
					System.out.println("contains or not ="+cont);
					int j=list.indexOf(comment);
					System.out.println("j="+j);
					list.add(j+1,F_add_html);
					
					/*
					  Download html file i.e. sop file of selected satellite  onto the local 
					 system in your backup folder the problem is ,if there are multiple users 
					 then we have to set the location of the folder accordingly on that system 
					 and also in this code so that it does not fails.This backup folder contains 
					 backup of that original file and the changes make in that file i.e the new 
					 entry of the satellite uploaded is in that file is contained in this backup 
					 file.						 							 					 
					*/
					
					// This is how we download file from FTP Server to your local system.
					
					// Download file 
					
					
					String remoteFile1 = "/oprnrpt/AnomalyDatabase/"+SUB_NAME_Name+"_Reports"+"/"+SUB_NAME_Name+"_study.html";
					
					/*If want to make changes in location please make sure of adding it like shown
					File downloadFile1 = new File("D:\\Project Trainee\\Ayush_HCST_mathura_2019\\backup\\"+SUB_NAME_Name+".htm"); 
					SUB_NAME_Name is the selected Satellite code from user
					*/
					
					File downloadFile1 = new File("backup\\"+"SUB_NAME_Name"+"study.html");
			        BufferedOutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
			        client.retrieveFile(remoteFile1, outputStream1);
			        outputStream1.close();

			        /*
					 Download html file i.e. sop file of selected satellite onto the local 
					 system in your backup folder.This is how we write the changes onto the
					 file present in the backup i.e the original sop satellite file. This backup
					 folder contains backup of that original file and the changes make in that
					 file i.e the new entry of the satellite uploaded is in that file is
					 contained in this backup file.
					 We have already added the tags onto the file before, here we are only overwritting 
					 the original file present on ftp where the changes want to be done so that it will be visible 
					 on ISTRAC > SPOA >SOP Website.
					*/
			        
			        File file = new File ("backup\\"+SUB_NAME_Name+"_study.html"); // put download address of file
					  BufferedWriter out = new BufferedWriter(new FileWriter(file)); 
					  for(j=0;j<list.size();j++){ out.write(list.get(j)+"\n");}
					  out.close();
			        //list.clear();
			        inputstream.close();
			        
			        /*
		         	Finally uploading the overwrite file to the Ftp Server so that it will
		        	display teh uploaded fle
		            */
			        
					client.connect("10.32.10.100",21);
					boolean login1=client.login("oprnrpt","test123");
					if(login1){System.out.println("YES");
					client.enterLocalPassiveMode();
					System.out.println("FTP CLIENT CODE COME IN 2");
					client.setFileType(FTP.BINARY_FILE_TYPE);}
					File loc1=new File("backup\\"+SUB_NAME_Name+"_study.html"); // location of file downloade
					FileInputStream inputstream1=new FileInputStream(loc1);
					String Flo1=sop;
					boolean done1=client.storeFile(Flo1,inputstream1);
					System.out.println("stored file on FTP "+done1);
	
					/*ArrayList<String> list_directory=null;
					list_directory=new ArrayList<>();
					FTPFile[] files=client.listFiles("/oprnrpt/AnomalyDatabase/EM1_Reports/EM1_study.html");
					for(FTPFile file1:files)
					{
						String details=file1.getName();
						
						System.out.println(details);
					}*/
					
					/*
					  Closing the stream now no transfer is going to be happen
					  input stream is the way to transfer the file to the FTP Server
					*/
					inputstream.close();
	
		/*			
					FileOutputStream writer = null;
					
					Document doc = getTextComponent( ).getDocument( );
					writer = new FileOutputStream(file);
					
					*/
					
					// It is the popup box that have a message that file is transfered.
					  // It Also Clear the text displayed on the Selected File Name & 
					  // Selected File Path onto the application 
					
					
					JOptionPane.showMessageDialog(f,"File Updated Successfully");
					File_Name.setText("");
					File_Path.setText("");
					f.setDefaultCloseOperation( JFrame.ABORT );
					
					
				}
				catch(Exception e){
					e.printStackTrace();		
				}
				}
			     else{
						JOptionPane.showMessageDialog(f,"File Updated Successfully");
						File_Name.setText("");
						File_Path.setText("");
					}
				inputstream.close();
				System.out.println("input stream  closed ");
				if(done){System.out.println("Done");}
				}
				else{
					File_Name.setText("");
					File_Path.setText("");
				}
			}
			
			catch(IOException ex){
	
				JOptionPane.showMessageDialog(f,"Connection to server Failed. Try Again Later.");
				//f.setDefaultCloseOperation( JFrame.ABORT );*/
				ex.printStackTrace();
			
		}
			finally {
				try{
						if(client.isConnected()){
						System.out.println("Connected TO FTP");
						client.logout();
						client.disconnect();
						System.out.println("Disconnected TO FTP");
						
						
						
	
						

						}

			}
			
				
					catch(IOException ex){
						suc.setText("Error");
						suc.setForeground(Color.RED);
						add(suc);
						ex.printStackTrace();}
				}
			}

			
		
		});
	// Adding to the Html File & other Variables to the panel 
	add(File_Name);
	add(Select_File_Path=new JLabel("File path : "));
	add(File_Path);
	add(Upload);
	
	// setting the view ,location ,size of the Application
	
	setLayout(new GridLayout(18,10,10,10));
	setLocation(500,30);
	setSize(362,668);
	//main_Frame.setVisible(true);
	
	}

	
}
