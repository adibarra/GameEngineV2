import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class KeybindManager {

	static ArrayList<Keybind> keybinds = new ArrayList<Keybind>();

	public static ArrayList<Keybind> getKeybinds() {
		return keybinds;
	}
	
	public static boolean tryRunAction(int keycode, boolean held)
	{
		Keybind keybind = isRegistered(keycode);
		
		if(keybind == null)
		{
			if(held)
				Engine.getLogger().log(LogLevel.DEBG,"Button "+KeyEvent.getKeyText(keycode)+" ("+keycode+") is not bound.");
			return false;
		}
		else
		{
			keybind.runAction(held);
			return true;
		}
	}
	
	public static Keybind isRegistered(int keycode)
	{
		Keybind found = null;
		
		for(Keybind bind : keybinds)
			if(bind.getKeycode() == keycode)
			{
				found = bind;
				break;
			}
		
		return found;
	}
	
	public static boolean addKeybind(Keybind keybind)
	{
		if(isRegistered(keybind.getKeycode()) == null)
		{
			keybinds.add(keybind);
			return true;
		}
		
		return false;
	}
	
	public static boolean removeKeybind(Keybind keybind)
	{
		return keybinds.remove(keybind);
	}
}

class Keybind
{
	KeybindInterface keybindInterface = null;
	int keycode = -1;

	Keybind(int keycode, KeybindInterface keybindInterface)
	{
		this.keycode = keycode;
		this.keybindInterface = keybindInterface;
	}
	
	public int getKeycode() {
		return keycode;
	}

	public void setKeycode(int keycode) {
		this.keycode = keycode;
	}
	
	public void runAction(boolean held)
	{
		keybindInterface.action(held);
	}

}

interface KeybindInterface
{
	public void action(boolean held);
}
