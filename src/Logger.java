import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

public class Logger {
	
	private JFrame logbox;
	private JTextArea logArea;
	private JScrollPane logScroll;
	private ArrayList<String> log;
	private VerboseLevel verboseLevel;
	
	Logger()
	{
		logbox = new JFrame("Log");
		logArea = new JTextArea();
		logScroll = new JScrollPane(logArea);
		log = new ArrayList<String>();
		verboseLevel = VerboseLevel.ALL;
		
		logArea.setBackground(Color.black);
		logArea.setForeground(Color.gray);
		logbox.add(logScroll);
		logbox.setVisible(true);
		logbox.setSize(300,500);
		logScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		logArea.setEditable(false);
	}
	
	private void log(String content)
	{
		log.add(content);
		
		String pane = "";
		for(String str : log)
			pane += str+"\n";
		
		logArea.setText(pane);
	}
	
	public void log(LogLevel level, String content)
	{
		String message = "["+level.toString()+"] "+content;
		
		if(verboseLevel.equals(VerboseLevel.OFF))
		{
			return;
		}
		else if(verboseLevel.equals(VerboseLevel.CRITICAL))
		{
			if(level.equals(LogLevel.CRIT))
				log(message);
		}
		else if(verboseLevel.equals(VerboseLevel.NORMAL))
		{
			if(!level.equals(LogLevel.DEBG))
				log(message);
		}
		else if(verboseLevel.equals(VerboseLevel.ALL))
		{
			log(message);
		}
	}
	
	public void changeVerboseLevel(VerboseLevel vl)
	{
		verboseLevel = vl;
	}
	
	public VerboseLevel getVerboseLevel()
	{
		return verboseLevel;
	}
	
	public String getFullLog()
	{
		return logArea.getText();
	}
	
}

enum LogLevel
{
	DEBG,INFO,WARN,CRIT
}

enum VerboseLevel
{
	OFF,CRITICAL,NORMAL,ALL
}
