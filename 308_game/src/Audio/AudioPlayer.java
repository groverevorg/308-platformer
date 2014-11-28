package Audio;

import javax.sound.sampled.*;

public class AudioPlayer {
	
	
	private Clip clip;
	
	public AudioPlayer(String s) {
		
		//open file as stream
		try {
			
			AudioInputStream ais =
				AudioSystem.getAudioInputStream(
					getClass().getResourceAsStream(
						s
					)
				);
			//get file format
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
	
	//play file from beginning
	public void play() {
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	//stop playing file
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	//close file
	public void close() {
		stop();
		clip.close();
	}
	
	//play file in a loop starting from beginning
	public void loop() {
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
}














