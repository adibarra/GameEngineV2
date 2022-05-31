import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Button extends InteractiveComponent {
	
	Button()
	{
		
	}
	
	Button(double x, double y, double w, double h, InteractiveComponentInterface ici)
	{
		xpos = x;
		ypos = y;
		width = w;
		height = h;
		this.ici = ici;
	}
	
	public void draw(Graphics g3)
	{		
		if(mousedOver)
			g3.setColor(altColor);
		else
			g3.setColor(color);
		
		g3.fillRect((int)xpos,(int)ypos,(int)width,(int)height);
		g3.setColor(Color.black);
		g3.drawRect((int)xpos,(int)ypos,(int)width,(int)height);
		
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
}
