import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SettingsScreen extends Screen {
		
	Button backtomainmenu;
	Dropdown resolution;

	SettingsScreen(Engine engine)
	{
		this.engine = engine;
		screen = new BufferedImage((int)Engine.getScreenSize().getWidth(),(int)Engine.getScreenSize().getHeight(),BufferedImage.TYPE_INT_ARGB);
		g3 = screen.getGraphics();
		
		backtomainmenu = new Button(100,100,200,75, new InteractiveComponentInterface() 
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{
				Engine.getScreenManager().setGamestate(GameState.MAINMENU);
			}
		});
		
		resolution = new Dropdown(100,200,200,50,new InteractiveComponentInterface() 
		{
			@Override
			public void onClickAction(InteractiveComponent ic)
			{	
				if(!resolution.expanded)
				{
					resolution.expanded = true;
					resolution.generateOptionComponents();
					ArrayList<InteractiveComponent> tempComp = new ArrayList<InteractiveComponent>(resolution.getOptionComponents());
					tempComp.addAll(intCompCore);
					intComp = tempComp;
				}
			}
			
		},new DropdownOptionsInterface()
		{
			@Override
			public void doSelection(Dropdown dropdown)
			{
				if(dropdown.selectedComponent == 0)
					Engine.getScreenManager().setResolution(new Dimension(2560,1440));
				else if(dropdown.selectedComponent == 1)
					Engine.getScreenManager().setResolution(new Dimension(1920,1080));
				else if(dropdown.selectedComponent == 2)
					Engine.getScreenManager().setResolution(new Dimension(1000,1000));
			}
		});
		
		backtomainmenu.setTitleText("Back to Main Menu");
		backtomainmenu.setSubText("currently in settings");
		resolution.setTitleText("Window Resolution");
		resolution.setOptions(new String[] {"2560x1440","1920x1080","1000x1000"});
		
		intCompCore.add(backtomainmenu);
		intCompCore.add(resolution);
		intComp = new ArrayList<InteractiveComponent>(intCompCore);
		
		genScreen();
		addGUI();
	}
	
	public void genScreen()
	{
		setBackground(Color.orange);
	}
}
