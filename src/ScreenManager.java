import java.awt.Dimension;
import java.util.ArrayList;

public class ScreenManager {

	private GameState gamestate = GameState.NONE;
	private Screen currentScreen;
	private Engine engine;
	
	private LoadingScreen loading;
	private MainmenuScreen mainmenu;
	private Screen settings;
	private Screen ingame;
	
	private boolean idleMode;
	private boolean maximized;
	private Dimension resolution;
	
	ScreenManager(Engine eng)
	{
		engine = eng;
		maximized = true;
		idleMode = false;
		resolution = new Dimension((int)Engine.getScreenSize().getWidth(),(int)Engine.getScreenSize().getHeight());
	}
	
	public Screen getCurrentScreen()
	{
		updateCurrentScreen();
		return currentScreen;
	}
	
	public void updateCurrentScreen()
	{
		if(gamestate != null)
		{
			switch (gamestate)
			{
				case INGAME:
					currentScreen = ingame;
					break;
				case MAINMENU:
					currentScreen = mainmenu;
					break;
				case SETTINGS:
					currentScreen = settings;
					break;
				case LOADING:
					currentScreen = loading;
					break;
				case NONE:
					setGamestate(GameState.LOADING);
					loading = new LoadingScreen(engine,this);
					currentScreen = loading;
					break;
				default:
					Engine.getLogger().log(LogLevel.CRIT,"GameState is... default? Reloading...");
					setGamestate(GameState.NONE);
					updateCurrentScreen();
					break;
			}
		}
		else
		{
			Engine.getLogger().log(LogLevel.CRIT,"GameState is null. Reloading...");
			setGamestate(GameState.NONE);
			updateCurrentScreen();
		}
	}
	
	public void checkInteractivesMoused(int mousex, int mousey)
	{
		if(currentScreen != null && currentScreen.getInteractables() != null)
		{
			ArrayList<InteractiveComponent> tempComp = new ArrayList<InteractiveComponent>(currentScreen.getInteractables());
			
			for(InteractiveComponent intComp : tempComp)
				intComp.ici.onMouseNot(intComp);
			
			for(InteractiveComponent intComp : tempComp)
				if(intComp.isMouseOver(mousex,mousey))
				{
					Engine.resetIdletimer();
					intComp.ici.onMouse(intComp);
					break;
				}
		}
	}
	
	public void checkInteractivesClicked(int mousex, int mousey)
	{	
		if(currentScreen != null && currentScreen.getInteractables() != null)
		{
			ArrayList<InteractiveComponent> tempComp = new ArrayList<InteractiveComponent>(currentScreen.getInteractables());
			for(InteractiveComponent intComp : tempComp)
				if(intComp.isMouseOver(mousex,mousey))
				{
					Engine.resetIdletimer();
					intComp.ici.onClick(intComp);
					break;
				}
		}
	}
	
	public void checkInteractivesReleased(int mousex, int mousey)
	{
		if(currentScreen != null && currentScreen.getInteractables() != null)
		{
			ArrayList<InteractiveComponent> tempComp = new ArrayList<InteractiveComponent>(currentScreen.getInteractables());
			for(InteractiveComponent intComp : tempComp)
				intComp.ici.onClickNot(intComp);
		}
	}

	public GameState getGamestate() {
		return gamestate;
	}
	
	public void setGamestate(GameState newGamestate) {
		if(!newGamestate.equals(gamestate))
		{
			if(newGamestate.equals(GameState.INGAME))
				Engine.getLogger().log(LogLevel.INFO,"Switching to INGAME screen.");
			else if(newGamestate.equals(GameState.MAINMENU))
				Engine.getLogger().log(LogLevel.INFO,"Switching to MAINMENU screen.");
			else if(newGamestate.equals(GameState.SETTINGS))
				Engine.getLogger().log(LogLevel.INFO,"Switching to SETTINGS screen.");
			else if(newGamestate.equals(GameState.LOADING))
				Engine.getLogger().log(LogLevel.INFO,"Switching to LOADING screen.");
			else if(newGamestate.equals(GameState.NONE))
				Engine.getLogger().log(LogLevel.WARN,"Switching to NONE screen.");
		}
		gamestate = newGamestate;
	}

	public Engine getEngine() {
		return engine;
	}

	public LoadingScreen getLoading() {
		return loading;
	}
	
	public void setLoading(LoadingScreen loading) {
		this.loading = loading;
	}

	public MainmenuScreen getMainmenu() {
		return mainmenu;
	}
	
	public void setMainmenu(MainmenuScreen mainmenu) {
		this.mainmenu = mainmenu;
	}

	public Screen getSettings() {
		return settings;
	}
	
	public void setSettings(Screen settings) {
		this.settings = settings;
	}

	public Screen getIngame() {
		return ingame;
	}
	
	public void setIngame(Screen ingame) {
		this.ingame = ingame;
	}

	public boolean isMaximized() {
		return maximized;
	}
	
	public void setResolution(Dimension newResolution) {
		resolution = newResolution;
	}

	public boolean isIdleMode() {
		return idleMode;
	}

	public void setIdleMode(boolean idleMode) {
		
		if(idleMode)
		{
			if(!this.idleMode)
				Engine.getLogger().log(LogLevel.DEBG,"Switching to idle rendering mode.");
		}
		else
		{
			if(this.idleMode)
				Engine.getLogger().log(LogLevel.DEBG,"Switching to active rendering mode.");
		}
		
		Engine.resetIdletimer();
		this.idleMode = idleMode;
	}
	
	public Dimension getResolution()
	{
		return resolution;
	}
}

enum GameState
{
	LOADING,MAINMENU,SETTINGS,INGAME,TYPING,NONE
}