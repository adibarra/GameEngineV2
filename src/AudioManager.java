import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioManager {
	
	private float volume = 1f;
	private ArrayList<SoundClip> clips = new ArrayList<SoundClip>();
	
	public AudioManager()
	{
		
	}
	
	public void loadAudio()
	{
		clips.clear();
		//clips.add(new SoundClip("Clip1"));
		//clips.add(new SoundClip("Clip2"));
	}
	
	public void play(String clipName, boolean restartFromBeginning)
	{
		Clip clip = getAudio(clipName);
		
		((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f*(float)Math.log10(getVolume()));
		
		if(clip.isRunning())
			clip.stop();
		if(restartFromBeginning)
			clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop(String clipName)
	{
		Clip clip = getAudio(clipName);
		
		((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f*(float)Math.log10(getVolume()));
		
		if(clip.isRunning())
		{
			clip.stop();
			clip.setFramePosition(0);
		}
	}
	
	private Clip getAudio(String clipName)
	{
		for(int k = 0; k < clips.size(); k++)
		{
			if(clipName.equalsIgnoreCase(clips.get(k).getClipName()))
				return clips.get(k).getAudioClip();
		}
		return null;
	}
	
	public void cleanUp()
	{
		for(int k = 0; k < clips.size(); k++)
			clips.get(k).getAudioClip().close();
	}
	
	public void setVolume(float volume) 
	{
		if(this.volume != volume)
		{
			this.volume = volume;
			
			for(int k = 0; k < clips.size(); k++)
				if(clips.get(k).getAudioClip().isRunning())
					play(clips.get(k).getClipName(),false);
		}
	}
	

	public float getVolume() 
	{
		return volume;
	}
}
	
class SoundClip 
{
	private Clip audioClip;
	private String clipName;
	
	public SoundClip(String clipName)
	{
		try
		{
			setClipName(clipName);
			setAudioClip(AudioSystem.getClip());
			getAudioClip().open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(clipName+".au")));
			
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | IllegalArgumentException e)
		{
			System.out.println("Failed to load: "+clipName+".au");
		}
	}
	
	public SoundClip(String clipName, String fileName)
	{
		try
		{
			setClipName(clipName);
			setAudioClip(AudioSystem.getClip());
			getAudioClip().open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(fileName+".au")));
			
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | IllegalArgumentException e)
		{
			System.out.println("Failed to load: "+fileName+".au");
		}
	}

	public Clip getAudioClip() {
		return audioClip;
	}

	public void setAudioClip(Clip audioClip) {
		this.audioClip = audioClip;
	}

	public String getClipName() {
		return clipName;
	}

	public void setClipName(String clipName) {
		this.clipName = clipName;
	}
}