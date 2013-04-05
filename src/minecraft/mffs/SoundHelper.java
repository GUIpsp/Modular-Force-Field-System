package mffs;

import java.net.URL;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class SoundHelper
{
	public static final SoundHelper INSTANCE = new SoundHelper();

	public static final String[] SOUND_FILES = { "field1.ogg", "field2.ogg", "field3.ogg", "field4.ogg", "field5.ogg" };

	@ForgeSubscribe
	public void loadSoundEvents(SoundLoadEvent event)
	{
		for (int i = 0; i < SOUND_FILES.length; i++)
		{
			URL url = this.getClass().getResource("/mffs/" + SOUND_FILES[i]);

			event.manager.soundPoolSounds.addSound("mffs/" + SOUND_FILES[i], url);

			if (url == null)
			{
				System.out.println("Invalid sound file: " + SOUND_FILES[i]);
			}
		}
	}
}