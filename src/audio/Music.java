// Class based on code from https://www.geeksforgeeks.org/play-audio-file-using-java/

package audio;
//Java program to play an Audio 
//file using Clip Object 
import java.io.File; 
import java.io.IOException; 
import java.util.Scanner; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

public class Music  
{ 

	// to store current position 
	Long currentFrame; 
	Clip clip; 
	String[] songs = {"music/Song1.WAV", "music/Song2.WAV","music/Song3.WAV","music/Song4.WAV", "music/Song5.WAV"};
	int songNumber;

	AudioInputStream audioInputStream; 
	static String filePath; 

	// constructor to initialize streams and clip 
	public Music() 
	{ 
		pickRandomSong();
		// create AudioInputStream object 
		try {
			audioInputStream =  
					AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());

			// create clip reference 
			clip = AudioSystem.getClip(); 

			// open audioInputStream to the clip 
			clip.open(audioInputStream); 

			clip.loop(Clip.LOOP_CONTINUOUSLY); 
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	} 

	public void pickRandomSong() {
		songNumber = (int)(Math.random() * (songs.length-1));
		filePath = songs[songNumber];
	}

	public int getSongNumber() {
		return this.songNumber + 1;
	}

	// Method to play the audio 
	public void play()  
	{ 
		//start the clip 
		clip.start(); 
	} 

	// Method to pause the audio 
	public void pause()  
	{ 
		this.currentFrame =  
				this.clip.getMicrosecondPosition(); 
		clip.stop(); 
	} 

	// Method to resume the audio 
	public void resumeAudio() 
	{ 
		clip.close(); 

		resetAudioStream();

		clip.setMicrosecondPosition(currentFrame); 
		this.play(); 
	} 

	// Method to restart the audio 
	public void restart() 
	{ 
		clip.stop(); 
		clip.close(); 
		resetAudioStream(); 
		currentFrame = 0L; 
		clip.setMicrosecondPosition(0); 
		this.play(); 
	} 

	// Method to stop the audio 
	public void stop() 
	{ 
		currentFrame = 0L; 
		clip.stop(); 
		clip.close(); 
	} 


	// Method to reset audio stream 
	public void resetAudioStream() 
	{ 
		try {
			audioInputStream = AudioSystem.getAudioInputStream( 
					new File(filePath).getAbsoluteFile());
			clip.open(audioInputStream); 
			clip.loop(Clip.LOOP_CONTINUOUSLY); 
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	public void nextSong() {
		songNumber++;
		songNumber %= (songs.length);
		filePath = songs[songNumber];
		restart();
	} 


} 