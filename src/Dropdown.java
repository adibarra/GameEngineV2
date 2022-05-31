import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class Dropdown extends InteractiveComponent {
	
	ArrayList<String> options;
	boolean expanded;
	int selectedComponent;
	ArrayList<InteractiveComponent> generatedComponents;
	DropdownOptionsInterface doi;
	
	Dropdown()
	{
		generatedComponents = new ArrayList<InteractiveComponent>();
		options = new ArrayList<String>();
		selectedComponent = -1;
		expanded = false;
	}
	
	Dropdown(double x, double y, double w, double h, InteractiveComponentInterface ici, DropdownOptionsInterface doi)
	{
		xpos = x;
		ypos = y;
		width = w;
		height = h;
		this.ici = ici;
		this.doi = doi;
		
		generatedComponents = new ArrayList<InteractiveComponent>();
		options = new ArrayList<String>();
		selectedComponent = -1;
		expanded = false;
	}
	
	public void setOptions(String[] strings)
	{
		options.addAll(Arrays.asList(strings));
		
		if(options.size() > 0)
			selectedComponent = 0;
	}
	
	public void generateOptionComponents()
	{
		Dropdown dd = this;
		generatedComponents.clear();
		
		for(int k = 0; k < options.size(); k++)
		{
			int compNum = k;
			Button optionButton = new Button((int)xpos,(int)(ypos+height*k),(int)width,(int)height,new InteractiveComponentInterface()
			{	
				public void onClickNotAction(InteractiveComponent ic)
				{
					expanded = false;
					selectedComponent = compNum;
					doi.selectionMade(dd);
					subText = options.get(selectedComponent);
					Engine.getScreenManager().getCurrentScreen().setInteractables(new ArrayList<InteractiveComponent>(Engine.getScreenManager().getCurrentScreen().getInteractablesCore()));
				}
				
				public void onMouse(InteractiveComponent ic) {
					ic.mousedOver = true;
				}
				
			});
			
			optionButton.altColor = new Color(200,128,128);
			optionButton.setTitleText(options.get(k));
			generatedComponents.add(optionButton);
		}
	}
	
	public ArrayList<InteractiveComponent> getOptionComponents()
	{
		return generatedComponents;
	}
	
	public void draw(Graphics g3)
	{	
		if(!expanded)
		{
			if(mousedOver)
				g3.setColor(altColor);
			else
				g3.setColor(color);
			
			g3.fillRect((int)(xpos),(int)(ypos),(int)(width),(int)(height));
			g3.setColor(Color.black);
			g3.drawRect((int)(xpos),(int)(ypos),(int)(width)-(int)(height/2),(int)(height));
			g3.drawRect((int)(xpos)+(int)(width)-(int)(height/2),(int)(ypos),(int)(height/2),(int)(height));
			
			drawCenteredString(g3,"V",new Rectangle2D.Double(xpos+width-height/2,ypos,height/2,height),font);
			
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
		else
		{
			for(int k = 0; k < options.size(); k++)
			{
				g3.setColor(color);
				g3.fillRect((int)(xpos),(int)(ypos+height*k),(int)(width),(int)(height));
				g3.setColor(Color.black);
				g3.drawRect((int)(xpos),(int)(ypos+height*k),(int)(width),(int)(height));
				drawCenteredString(g3,options.get(k),new Rectangle2D.Double(xpos,ypos+height*k,width,height),font);
			}
		}
	}
}

interface DropdownOptionsInterface
{
	public default void selectionMade(Dropdown dropdown) {
		doSelection(dropdown);
		Engine.getScreenManager().setIdleMode(false);
	}
	
	public default void doSelection(Dropdown dropdown)
	{
		
	}
}
