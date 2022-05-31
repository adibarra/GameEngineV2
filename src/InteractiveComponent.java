import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class InteractiveComponent {
	
	double xpos = 0;
	double ypos = 0;
	double width = 0;
	double height = 0;
	
	String titleText = "";
	String subText = "";
	Color color = new Color(128,128,128);
	Color altColor = new Color(150,150,150);
	Font font = new Font("Ariel",Font.BOLD,12);
	
	boolean held = false;
	boolean mousedOver = false;
	InteractiveComponentInterface ici;
	
	InteractiveComponent()
	{
		
	}
	
	InteractiveComponent(double x, double y, double w, double h, InteractiveComponentInterface ici)
	{
		xpos = x;
		ypos = y;
		width = w;
		height = h;
		this.ici = ici;
	}
	
	public boolean isMouseOver(int mousex, int mousey)
	{
		return new Rectangle2D.Double(xpos,ypos,width,height).contains(mousex,mousey);
	}
	
	public void drawCenteredString(Graphics g3, String text, Rectangle2D.Double rect, Font font) 
	{
	    FontMetrics metrics = g3.getFontMetrics(font);
	    int x = (int)(rect.getX()+(rect.getWidth()-metrics.stringWidth(text))/2);
	    int y = (int)(rect.getY()+((rect.getHeight()-metrics.getHeight())/2)+metrics.getAscent());
	    g3.setFont(font);
	    g3.drawString(text,x,y);
	}
	
	public void draw(Graphics g3)
	{	
		if(mousedOver)
			g3.setColor(altColor);
		else
			g3.setColor(color);
		
		g3.fillRect((int)(xpos),(int)(ypos),(int)(width),(int)(height));
		g3.setColor(Color.black);
		g3.drawRect((int)(xpos),(int)(ypos),(int)(width),(int)(height));
		
		if(subText.equals(""))
		{
			drawCenteredString(g3,titleText,new Rectangle2D.Double(xpos,ypos,width,height),font);
		}
		else
		{
			drawCenteredString(g3,titleText,new Rectangle2D.Double(xpos,ypos,width,height-(font.getSize()+5)),font);
			drawCenteredString(g3,subText,new Rectangle2D.Double(xpos,ypos+(font.getSize()+5),width,height-(font.getSize()+5)),font);
		}
	}
	
	public void setTitleText(String newText)
	{
		titleText = newText;
	}
	
	public void setSubText(String newText)
	{
		subText = newText;
	}
	
	public void setFont(Font newFont)
	{
		font = newFont;
	}
	
	public void setLocation(double xpos, double ypos)
	{
		this.xpos = xpos;
		this.ypos = ypos;
	}
	
	public void setCenterLocation(double xpos, double ypos)
	{
		this.xpos = xpos-width/2;
		this.ypos = ypos-height/2;
	}
	
	public void setSize(double width, double height)
	{
		this.width = width;
		this.height = height;
	}
	
	public Dimension getSize()
	{
		return new Dimension((int)width,(int)height);
	}
}

interface InteractiveComponentInterface
{
	public default void onClickAction(InteractiveComponent ic) {}
	public default void onClickNotAction(InteractiveComponent ic) {}
	
	public default void onClick(InteractiveComponent ic) {
		if(!ic.held)
		{
			Engine.getLogger().log(LogLevel.DEBG,"\""+ic.titleText+"\" held.");
			ic.held = true;
			onClickAction(ic);
		}
		Engine.getScreenManager().setIdleMode(false);
	}

	public default void onClickNot(InteractiveComponent ic) {
		if(ic.held)
		{
			Engine.getLogger().log(LogLevel.DEBG,"\""+ic.titleText+"\" released.");
			ic.held = false;
			onClickNotAction(ic);
		}
		Engine.getScreenManager().setIdleMode(false);
	}
	
	public default void onMouse(InteractiveComponent ic) {
		ic.mousedOver = true;
		Engine.getScreenManager().setIdleMode(false);
	}

	public default void onMouseNot(InteractiveComponent ic) {
		ic.mousedOver = false;
	}
}
