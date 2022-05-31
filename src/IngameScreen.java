import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class IngameScreen extends Screen {
		
	Button backtomainmenu;

	IngameScreen(Engine engine)
	{
		this.engine = engine;
		screen = new BufferedImage((int)Engine.getScreenManager().getResolution().getWidth(),(int)Engine.getScreenManager().getResolution().getHeight(),BufferedImage.TYPE_INT_ARGB);
		g3 = screen.getGraphics();
		
		backtomainmenu = new Button(100,100,200,75, new InteractiveComponentInterface() 
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{
				Engine.getScreenManager().setGamestate(GameState.MAINMENU);
			}
		});
		
		backtomainmenu.setTitleText("Back to Main Menu");
		backtomainmenu.setSubText("currently in game");
		
		intCompCore.add(backtomainmenu);
		intComp = new ArrayList<InteractiveComponent>(intCompCore);
		
		genScreen();
		addGUI();
	}
	
	public void genScreen()
	{
		setBackground(Color.red);
	}
}
