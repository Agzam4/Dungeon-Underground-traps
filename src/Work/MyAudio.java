package Work;

import java.io.File;
import java.io.FileNotFoundException;

import javax.sound.sampled.*;

public class MyAudio {
	
	private Clip clip;
	private boolean isPlaying = false;
	
	public MyAudio(String s) {
		try {
			AudioInputStream ais;
			if(MyFile.pack == null) {
				ais = AudioSystem.getAudioInputStream(getClass().getResource/*AsStream*/(s));
			}else {
				try {
					ais = AudioSystem.getAudioInputStream(new File(
							System.getProperty("user.dir") + "\\data\\packs\\" + MyFile.pack + s));
				} catch (FileNotFoundException e) {
					ais = AudioSystem.getAudioInputStream(getClass().getResource/*AsStream*/(s));
				}
			}
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais =
				AudioSystem.getAudioInputStream(
					decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void play(int loops) {
		try {
			if(clip == null) return;
			stop();
			clip.setFramePosition(0);
			clip.loop(loops);
			clip.start();
			isPlaying = true;
		} catch (Exception e) {
		}
	}
	
	public void stop() {
		isPlaying = false;
		try {
			if(clip.isRunning()) clip.stop();
		} catch (Exception e) {
		}
	}
	
	public void close() {
		isPlaying = false;
		try {
		stop();
		clip.close();
		} catch (Exception e) {
		}
	}
	
	public void setVolume(float volume) {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);        
	    gainControl.setValue(20f * (float) Math.log10(volume));
	}
//	public void setVol(float v) {
//		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//		gainControl.setValue(v);
//	}
	
	public boolean isPlaying() {
		return isPlaying;
	}
}














