import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class LoadingScreen extends Screen {
	
	private Long loadingTimer;
	private double loadingtime;
	private ScreenManager screenManager;
	private Thread loadingThread;
	
	private boolean finishedLoading = false;
	
	LoadingScreen(Engine eng, ScreenManager sm)
	{
		this.engine = eng;
		screen = new BufferedImage((int)Engine.getScreenManager().getResolution().getWidth(),(int)Engine.getScreenManager().getResolution().getHeight(),BufferedImage.TYPE_INT_ARGB);
		g3 = screen.getGraphics();
		screenManager = sm;

		startLoading();
		genScreen();
		addGUI();
	}
	
	public void genScreen()
	{
		double timeleft = (loadingTimer-System.currentTimeMillis());
		double screenWidth = getScreenSize().getWidth();
		double screenHeight = getScreenSize().getHeight();
		
		setBackground(Color.black);
		g3.setColor(Color.gray);
		drawCenteredString("LOADING",new Rectangle2D.Double(0,0,screenWidth,screenHeight),new Font("Ariel",Font.BOLD,30));
		
		if(timeleft <= 0)
		{
			if(finishedLoading)
				screenManager.setGamestate(GameState.MAINMENU);
			else
			{
				g3.setColor(new Color(128,128,128));
				g3.drawRect((int)(3*screenWidth/8),(int)(screenHeight/2+screenHeight/30),(int)(screenWidth/4),24);
				g3.setColor(new Color(20,100,20));
				g3.fillRect((int)(3*screenWidth/8)+1,(int)(screenHeight/2+screenHeight/30)+1,(int)(screenWidth/4)-1,23);
			}
		}
		else
		{			
			g3.setColor(new Color(128,128,128));
			g3.drawRect((int)(3*screenWidth/8),(int)(screenHeight/2+screenHeight/30),(int)(screenWidth/4),24);
			g3.setColor(Color.green);			
			g3.fillRect((int)(3*screenWidth/8)+1,(int)(screenHeight/2+screenHeight/30)+1,(int)(((loadingtime-timeleft)/loadingtime)*screenWidth/4)-2,23);
		}
	}
	
	public void startLoading()
	{
		loadingtime = 1000;
		loadingTimer = (long)(System.currentTimeMillis()+loadingtime);

		loadingThread = new Thread()
		{
			public void run()
			{
				Engine.getLogger().log(LogLevel.INFO,"Started Loading.");
				screenManager.setMainmenu(new MainmenuScreen(engine));
				screenManager.setSettings(new SettingsScreen(engine));
				screenManager.setIngame(new IngameScreen(engine));
				
				Engine.getAudioManager().loadAudio();
				
				Engine.getLogger().log(LogLevel.INFO,"All loading completed.");
				finishedLoading = true;
			}
		};
		
		loadingThread.start();
	}

}
