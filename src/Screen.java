import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Screen {

	Graphics g3;
	Engine engine;
	BufferedImage screen;
	ArrayList<InteractiveComponent> intComp;
	ArrayList<InteractiveComponent> intCompCore;
	
	Screen()
	{
		intComp = new ArrayList<InteractiveComponent>();
		intCompCore = new ArrayList<InteractiveComponent>();
	}
	
	Screen(Engine engine)
	{
		this.engine = engine;
		screen = new BufferedImage((int)Engine.getScreenSize().getWidth(),(int)Engine.getScreenSize().getHeight(),BufferedImage.TYPE_INT_ARGB);
		g3 = screen.getGraphics();
		
		intComp = new ArrayList<InteractiveComponent>();
		intCompCore = new ArrayList<InteractiveComponent>();
		
		genScreen();
		addGUI();
	}
	
	public void genScreen()
	{
		
	}
	
	public void addGUI()
	{
		for(InteractiveComponent comp : intComp)
			comp.draw(g3);
	}
	
	public BufferedImage getScreen()
	{
		if(Engine.getIdletimer() > 10 && !Engine.getScreenManager().getGamestate().equals(GameState.LOADING))
		{
			if(!Engine.getScreenManager().isIdleMode())
				Engine.getScreenManager().setIdleMode(true);
		}
		else if(Engine.getScreenManager().isIdleMode())
			Engine.getScreenManager().setIdleMode(false);
		
		genScreen();
		addGUI();
		
		return screen;
	}
	
	public Dimension getScreenSize()
	{
		return new Dimension((int)Engine.getScreenSize().getWidth(),(int)Engine.getScreenSize().getHeight());
	}
	
	public ArrayList<InteractiveComponent> getInteractables()
	{
		return intComp;
	}
	
	public ArrayList<InteractiveComponent> getInteractablesCore()
	{
		return intCompCore;
	}
	
	public void setInteractables(ArrayList<InteractiveComponent> newIntComp)
	{
		intComp = newIntComp;
	}
	
	public void setBackground(Color color)
	{
		g3.setColor(color);
		g3.fillRect(0,0,screen.getWidth(),screen.getHeight());
	}
	
	public void drawCenteredString(String text, Rectangle2D.Double rect, Font font) 
	{
	    FontMetrics metrics = g3.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g3.setFont(font);
	    g3.drawString(text,x,y);
	}
}
