import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class GameEngine
{
    public static void main(String[] args)
    {
    	String osName = System.getProperty("os.name").toLowerCase();
		if (osName.startsWith("mac os x"))
		{
			//Makes Command+Q activate the windowClosing windowEvent
			System.setProperty("apple.eawt.quitStrategy","CLOSE_ALL_WINDOWS");
		}
    	
		Engine engine = new Engine();
		
		engine.setSize(1000,750);
		engine.setExtendedState(engine.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		engine.setLocationRelativeTo(null);
		engine.setUndecorated(true);
		engine.setVisible(true);
		engine.setResizable(false);
		
		engine.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run()
			{
				Engine.getAudioManager().cleanUp();
				Engine.getLogger().log(LogLevel.INFO,"Cleaned up, now saving and exiting.");
				Engine.getFileLoader().saveLog(Engine.getLogger().getFullLog());
	        }
		});
    }
}



@SuppressWarnings("serial")
class Engine extends JFrame implements MouseListener,MouseMotionListener,KeyListener,MouseWheelListener
{

	private static Graphics2D g2;
	private static BufferedImage offscreen;
	
	private static final float TARGET_FRAME_TIME_120 = 8.3f;
	private static final float TARGET_FRAME_TIME_90 = 11.1f;
	private static final float TARGET_FRAME_TIME_60 = 16.7f;
	private static final float TARGET_FRAME_TIME_30 = 33.3f;
	private static final float TARGET_FRAME_TIME_15 = 66.7f;
	private static float TARGET_FRAME_TIME = TARGET_FRAME_TIME_60;
	private static double start = System.nanoTime();
	
	private static int mousex = 0;
	private static int mousey = 0;
	private static int clickx = 0;
	private static int clicky = 0;
	private static long idletimer = 0;
	
	private static final String PROJECT_NAME = "GameEngineV2";
	
	private static FileLoader fl;
	private static AudioManager am;
	private static ScreenManager sm;
	private static Logger logger;
	
	Engine()
	{
		super(PROJECT_NAME);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addKeyListener(this);
        requestFocusInWindow();
        
        Utils.setupKeybinds(this);
		setScreenManager(new ScreenManager(this));
		setAudioManager(new AudioManager());
		setFileLoader(new FileLoader().prepare());
		setLogger(new Logger());
		
		getLogger().log(LogLevel.INFO,"Started Game Engine.");
		
	}
	
	public void update(Graphics g)
	{
		if(!Engine.getScreenManager().isIdleMode())
		{
			BufferedImage screen = getScreenManager().getCurrentScreen().getScreen();
			offscreen = (BufferedImage)createImage((int)screen.getWidth(),screen.getHeight());
			g2 = offscreen.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
			
			g2.drawImage(screen,0,0,null);
			g.drawImage(offscreen,0,0,super.getWidth(),super.getHeight(),this);
			
			
			/*
			BufferedImage screen = getScreenManager().getCurrentScreen().getScreen();	
			
			offscreen = (BufferedImage)createImage((int)screen.getWidth(),screen.getHeight());
			g2 = offscreen.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
						
			g2.drawImage(screen,0,0,null);
			Image temp = offscreen.getScaledInstance((int)getScreenManager().getResolution().getWidth(),(int)getScreenManager().getResolution().getHeight(),Image.SCALE_SMOOTH);
			temp = temp.getScaledInstance(super.getWidth(),super.getHeight(),Image.SCALE_SMOOTH);
			g.drawImage(temp,0,0,this);
			*/
		}
		
		start = System.nanoTime();
		Utils.delay(Utils.numFix(TARGET_FRAME_TIME-((System.nanoTime()-start)/1000000000)));
		idletimer++;
		repaint();
	}

	public void mouseDragged(MouseEvent arg0) {
		clickx = mousex = arg0.getX();
		clicky = mousey = arg0.getY();
		getScreenManager().checkInteractivesClicked(arg0.getX(),arg0.getY());
	}
	
	public void mouseReleased(MouseEvent e) {
		clickx = mousex = e.getX();
		clicky = mousey = e.getY();
		getScreenManager().checkInteractivesReleased(e.getX(),e.getY());
	}

	public void mouseMoved(MouseEvent arg0) {
		mousex = arg0.getX();
		mousey = arg0.getY();
		getScreenManager().checkInteractivesMoused(arg0.getX(),arg0.getY());
	}
	
	public void keyPressed(KeyEvent arg0) {
		KeybindManager.tryRunAction(arg0.getKeyCode(),true);
	}

	public void keyReleased(KeyEvent arg0) {
		KeybindManager.tryRunAction(arg0.getKeyCode(),false);
	}
	
	public static Dimension getScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static String getProjectName() {
		return PROJECT_NAME;
	}

	public static FileLoader getFileLoader() {
		return fl;
	}

	public static void setFileLoader(FileLoader fl) {
		Engine.fl = fl;
	}

	public static AudioManager getAudioManager() {
		return am;
	}

	public static void setAudioManager(AudioManager am) {
		Engine.am = am;
	}

	public static ScreenManager getScreenManager() {
		return sm;
	}

	public static void setScreenManager(ScreenManager sm) {
		Engine.sm = sm;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		Engine.logger = logger;
	}

	public static long getIdletimer() {
		return idletimer;
	}

	public static void setIdletimer(long idletimer) {
		Engine.idletimer = idletimer;
	}
	
	public static void resetIdletimer() {
		Engine.idletimer = 0;
	}
	
	public static float getTargetFrameTime() {
		return TARGET_FRAME_TIME;
	}

	public void mousePressed(MouseEvent e) {
		mouseDragged(e);
	}
	
	public void paint(Graphics g) {
		update(g);
	}
	
	public void keyTyped(KeyEvent arg0) {}
	
	public void mouseClicked(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	public void mouseWheelMoved(MouseWheelEvent arg0) {}

}

class Utils
{
	public static void setupKeybinds(Engine engine)
	{
		KeybindManager.addKeybind(new Keybind(KeyEvent.VK_X, new KeybindInterface()
		{
			@Override
			public void action(boolean held) {
				System.exit(0);
			}
		}));

	}
	
	public static double numFix(double num)
	{	
		return (num < 0) ? 0 : num;
	}
	
	public static long getCurrentTime()
	{
		return (long)(System.nanoTime()/1000000000);
	}
	
	public static void delay(double nt)
	{
		try 
		{
			Thread.sleep((long)nt);
		} catch (InterruptedException e) {}
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
	}
	
	public static BufferedImage resizeImage(BufferedImage img, int width, int height)
	{
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
}