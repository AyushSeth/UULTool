
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.omg.CORBA.portable.OutputStream;

public class ETLTool 
{
	JFrame Main_frame=new JFrame("UUL Tool");
	JFrame Log_in =new JFrame("UUL Tool Login");
	
	ETLTool() throws HeadlessException
	{
		
		LOgin l_oin = null;
		Log_in.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Log_in.getContentPane().add(l_oin=new LOgin(this));
		Log_in.setSize(335,130);
		Log_in.setLocation(500,300);
		Log_in.setVisible(true);
	}
	void main_frame()
	{
		
		Log_in.getContentPane().removeAll();
		Log_in.setVisible(false);
		JTabbedPane tab = new JTabbedPane();
		MonthlyReport panel_mr;
		MOCBMinutes panel_mocb;
		StudyAnalysisReport panel_study;
		SpecialOperationsReport panel_sop;
		tab.addTab("Monthly Report",panel_mr =new MonthlyReport());
		tab.addTab("MOCB-Minutes",panel_mocb= new MOCBMinutes());
		tab.addTab("Study Analysis Report",panel_study= new StudyAnalysisReport());
		tab.addTab("Special Operations Report",panel_sop= new SpecialOperationsReport());
		Main_frame.add(tab);
		Main_frame.getContentPane().add(tab);
		Main_frame.setLocation(500,30);
		Main_frame.setSize(362,668);
		Main_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		Main_frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		ETLTool cf=new ETLTool();
	}
}
