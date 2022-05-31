import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class FileLoader {
	
	private String workingDirectory;
	private String OS = (System.getProperty("os.name")).toUpperCase();
	
	public FileLoader prepare()
	{
		if (OS.contains("WIN"))
		{
			workingDirectory = System.getenv("AppData");
		}
		else
		{
			workingDirectory = System.getProperty("user.home");
			workingDirectory += "/Library/Application Support";
		}
		return this;
	}
	
	/**
	 * Loads file from outside jar file, parses its content
	 */
	public ArrayList<String> load(String fileName)
	{
		String gameData = "";
		String line = "";
		ArrayList<String> saveGame = new ArrayList<String>();
		
		File jarFile;
		try
		{
			jarFile = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
			File saveFile = new File(jarFile.getParentFile()+"/"+fileName+".dat");
		
			if(saveFile.exists())
			{
				BufferedReader br = new BufferedReader(new FileReader(saveFile));
			
				while((line = br.readLine()) != null)
				{
					if(!line.contains("#"))
					{
						gameData += line;
					}
				}
				br.close();
				
				for(int k = 0; k < gameData.split(";").length; k++)
				{
					saveGame.add(gameData.split(";")[k]);
				}
			}
			else
			{
				System.out.println("Failed to load file: "+fileName);
			}
					
		} catch (URISyntaxException | IOException e) {}	
		
		return saveGame;
	}
	
	/**
	 * Loads file from jar's internal res folder, parses its content
	 */
	public ArrayList<String> loadRes(String fileName)
	{
		String gameData = "";
		String line = "";
		ArrayList<String> saveGame = new ArrayList<String>();
		
		try
		{
			InputStream saveFile = getClass().getResourceAsStream(fileName+".dat");
					
			if(saveFile != null)
			{
				BufferedReader br = new BufferedReader(new InputStreamReader(saveFile,"UTF-8"));
			
				while((line = br.readLine()) != null)
				{
					if(!line.contains("#"))
					{
						gameData += line;
					}
				}
				br.close();
				
				for(int k = 0; k < gameData.split(";").length; k++)
				{
					saveGame.add(gameData.split(";")[k]);
				}
			}
			else
			{
				System.out.println("Failed to load file: "+fileName);
			}
					
		} catch (IOException e) {}	
		
		return saveGame;
	}
	
	public void saveLog(String content)
	{		
		try
		{
			LocalDateTime now = LocalDateTime.now();
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH_mm_ss");
			
			File saveFile = new File(getSaveDir()+"/"+Engine.getProjectName()+"/Logs/"+dtf.format(now)+".log");
			if(!saveFile.exists())
			{
				new File(getSaveDir()+"/"+Engine.getProjectName()+"/Logs/").mkdirs();
				saveFile.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			
			bw.write(content);
			bw.close();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get save directory
	 */
	public String getSaveDir()
	{
		return workingDirectory;
	}

}
