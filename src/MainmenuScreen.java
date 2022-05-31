import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainmenuScreen extends Screen {
		
	Button StartGame;
	Button Settings;
	Button Quit;

	MainmenuScreen(Engine engine)
	{
		this.engine = engine;
		screen = new BufferedImage((int)Engine.getScreenManager().getResolution().getWidth(),(int)Engine.getScreenManager().getResolution().getHeight(),BufferedImage.TYPE_INT_ARGB);
		g3 = screen.getGraphics();
		
		StartGame = new Button(100,100,200,75, new InteractiveComponentInterface()
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{
				onClickNot(ic);
				Engine.getScreenManager().setGamestate(GameState.INGAME);
			}
		});
		
		Settings = new Button(100,200,200,75, new InteractiveComponentInterface()
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{
				onClickNot(ic);
				Engine.getScreenManager().setGamestate(GameState.SETTINGS);
			}
		});
		
		Quit = new Button(100,300,200,75, new InteractiveComponentInterface()
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{
				onClickNot(ic);
				System.exit(0);
			}
		});
		
		StartGame.setTitleText("Start Game");
		Settings.setTitleText("Settings");
		Quit.setTitleText("Quit Game");
		
		intCompCore.add(StartGame);
		intCompCore.add(Settings);
		intCompCore.add(Quit);
		intComp = new ArrayList<InteractiveComponent>(intCompCore);
		
		genScreen();
		addGUI();
	}
	
	public void genScreen()
	{
		StartGame.setCenterLocation(2*getScreenSize().width/6,getScreenSize().height/2);
		Settings.setCenterLocation(3*getScreenSize().width/6,getScreenSize().height/2);
		Quit.setCenterLocation(4*getScreenSize().width/6,getScreenSize().height/2);
		
		setBackground(Color.black);
	}
}
