
import javax.swing.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFile;

import java.awt.*;
import java.awt.event.*;
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

public class MOCBMinutes extends JPanel{
	JFrame f=new JFrame();	// This frame is used for Contains all the JOptionPane Pop-Ups
	JButton Select_Button;	// It is a button "Choose" came in the tabs 
	JLabel Select_File_Name;	// Its contains the text select file name
	JTextArea File_Name=new JTextArea();	// Its stores the text file name
	JLabel Select_File_Path;	// Its contains the text select file path
	JTextArea File_Path=new JTextArea();	// Its stores the text file path
	JTextField SourceFileTextField=new JTextField();	// It contains the options selected 
	JButton Upload;			// upload button 
	JTextField DestinationTextField=new JTextField();	 // Not used in the program
	JLabel Result;
	
	/*		All are drop box as requirements		*/
	
	JComboBox sc;
	JComboBox ss;
	JComboBox month;
	JComboBox year;
	String Month_Name;
	String YEAR_Name;
	String SUB_NAME_Name;
	String Sat_CODE_Name;
	JLabel suc=new JLabel();
	JTextArea File_Name_Without_Extension=new JTextArea();
	int response=2;
	
	MOCBMinutes() 
	{
			JLabel Select_File;
			suc.setText(null);
			//System.out.println("Helo -3");
			
			// Adding List for Selection of satellite 
			
			Collection col=System.getProperties().values();
			
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


			//JLabel S_year=new JLabel("Year");
			
			//System.out.println("Helo -4");

			/*add(S_year);
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
						//System.out.println("F_Name : "+name);
						//System.out.println("F_Path : "+selected);
						
										
						/*Object YEAR=year.getItemAt(year.getSelectedIndex());
						YEAR_Name=YEAR.toString();*/
						//System.out.println("year ="+YEAR);
					}
				}
			});


				int flag=1;
			Upload=new JButton("Upload");	
			Upload.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
				
					//System.out.println("FTP CLIENT CODE COME IN 1");
					FTPClient client=new FTPClient();
					FileInputStream fis=null;
					try{
						String Fil_name=File_Name.getText();
						if(File_Path.getText().trim().length()<=0||File_Name.getText().trim().length()<=0)								
						{
							//System.out.println("is null");
							JOptionPane.showMessageDialog(f,"Please Fill All Details");
							f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
						//System.out.println("FTP CLIENT CODE COME IN 2");
						
						/*
						
						ArrayList<String> list_directory=null;
						list_directory=new ArrayList<>();
						FTPFile[] files=client.listFiles("/oprnrpt/MOCB/MOCB-Minutes/MOCB");
						for(FTPFile file1:files)
						{
							String details=file1.getName();
							list_directory.add(details);
							System.out.println(details);
						}
						boolean File_in_ftp_contains=list_directory.contains(File_Name.getText());
						System.out.println("File is on or not on ftp:"+File_in_ftp_contains);
						
						*/

						// For converting file to Binary for safe transfer
						
						client.setFileType(FTP.BINARY_FILE_TYPE);
						/*
						  Get the location of the file selected.File_Path contains the path of the file
						  selected
						*/
						
						File loc=new File(File_Path.getText());
						//System.out.println("FTP CLIENT CODE COME IN 4");
						
						// Floc contains where on Ftp server the file is to uploaded
						
						String Flo="/oprnrpt/MOCB/MOCB-Minutes/MOCB/";
						String n=File_Name.getText();
						String Floc=Flo+n;
						//System.out.println(Floc);
						
						
						ArrayList<String> list_directory=new ArrayList<String>();
						if(client.isConnected()){
							FTPFile[] files=null;
							String[] fileNames=null;
							try{
								fileNames=client.listNames("/oprnrpt/MOCB/MOCB-Minutes/MOCB/");
								
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
						//System.out.println("complete process 1 "+done);
						
						// F_add_html store the link to be added to the HTML File of MOCB 
						
						String linkadd ="<p class=MsoNoSpacing style='margin-left:.25in;text-indent:-.25in;mso-list:l0 level1 lfo2'><![if !supportLists]><span class=MsoHyperlink><span style='font-family:Symbol;mso-fareast-font-family:Symbol;mso-bidi-font-family:Symbol;text-decoration:none;text-underline:none'><span style='mso-list:Ignore'>·<span style='font:7.0pt \"Times New Roman\""+"'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span></span></span></span><![endif]><span class=MsoHyperlink><b style='mso-bidi-font-weight:normal'><span style='font-family:\"Arial\",\"sans-serif\""+"'><a href=";
						String F_name= File_Name.getText(); 
						String aa=">";
						String FName=File_Name_Without_Extension.getText();
						String end="</a><o:p></o:p></span></b></span></p>"; 
						
						// F_add_html store the link to be added to the HTML File of MOCB
						
						String F_add_html=linkadd+F_name+aa+FName+end;
						//System.out.println("F_add_html   :"+F_add_html);
						
						// loc_mocb stores the location of the html file in MOCB Folder
						
						String loc_mocb="/oprnrpt/MOCB/MOCB-Minutes/MOCB/mocb.html";
						//String Name="mocb";
						
						// Comment is already set inside the MoCB html file
						
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
						
						String comment="<!--ayushTheGreatDeveloper-->";
						//System.out.println("Yes");
						
						/*boolean connected=client.isConnected();
						if(connected){System.out.println("YEs it is connected");}else{System.out.println("YEs it is connected");}
						*/
						
						
						 BufferedReader reader=null;
					     BufferedWriter writer=null;
					     ArrayList<String> list=null;
					     list=new ArrayList<>();
						
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
						InputStreamReader isr=new InputStreamReader(client.retrieveFileStream("/oprnrpt/MOCB/MOCB-Minutes/MOCB/mocb.htm"));
						try{
							reader=new BufferedReader(isr);
							String line;
							while((line=reader.readLine())!=null){
								list.add(line);
								//System.out.println(line);
							}
							reader.close();
							boolean cont=list.contains(comment);
							//System.out.println("contains or not ="+cont);
							int j=list.indexOf(comment);
							//System.out.println("j="+j);
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
							
							
							String remoteFile1 = "/oprnrpt/MOCB/MOCB-Minutes/MOCB/mocb.htm";
					        
							/*If want to make changes in location please make sure of adding it like shown
							File downloadFile1 = new File("D:\\Project Trainee\\Ayush_HCST_mathura_2019\\backup\\"+SUB_NAME_Name+".htm"); 
							SUB_NAME_Name is the selected Satellite code from user
							*/
							
							File downloadFile1 = new File("backup\\mocb1.htm");
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
					        
					        File file = new File ("backup\\mocb1.htm"); // put download address of file
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
							//System.out.println("FTP CLIENT CODE COME IN 2");
							client.setFileType(FTP.BINARY_FILE_TYPE);}
							File loc1=new File("backup\\mocb1.htm"); // location of file downloade
							FileInputStream inputstream1=new FileInputStream(loc1);
							String Flo1="/oprnrpt/MOCB/MOCB-Minutes/MOCB/mocb.htm";
							boolean done1=client.storeFile(Flo1,inputstream1);
							//System.out.println("stored file on FTP "+done1);
			
							/*ArrayList<String> list_directory=null;
							list_directory=new ArrayList<>();
							FTPFile[] files=client.listFiles("/oprnrpt/MOCB/MOCB-Minutes/MOCB/");
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
						//System.out.println("input stream  closed ");
						if(done){System.out.println("Done");}
						
					}
						else{
							File_Name.setText("");
							File_Path.setText("");
						}
				}
				
					catch(IOException ex){
			
						JOptionPane.showMessageDialog(f,"Connection to server Failed. Try Again Later.");
						//f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );ex.printStackTrace();*/
					}
					finally {
						try{
								if(client.isConnected()){
								//System.out.println("Connected TO FTP");
								client.logout();
								client.disconnect();
								//System.out.println("Disconnected TO FTP");
								
		

								}

					}
					
						
							catch(IOException ex){
								
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
		}
		

}




