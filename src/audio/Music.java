// Class based on code from https://www.geeksforgeeks.org/play-audio-file-using-java/

package audio;
//Java program to play an Audio 
//file using Clip Object 

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner; 

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;

import gui.ClueGame; 

public class Music  
{ 

	// to store current position 
	Long currentFrame; 
	Clip clip; 
	String[] songs = {"/Song1.wav", "/Song2.wav","/Song3.wav","/Song4.wav", "/Song5.wav"};
	int songNumber;
	static boolean paused = false;
	AudioInputStream audioInputStream; 
	String filePath; 

	// constructor to initialize streams and clip 
	public Music(){} 

	public void initialize() {

		// create AudioInputStream object 
		try {
			URL url = this.getClass().getResource(filePath);
			
			audioInputStream = AudioSystem.getAudioInputStream(url);

			// create clip reference 
			clip = AudioSystem.getClip(); 

			// open audioInputStream to the clip 
			clip.open(audioInputStream); 

			clip.loop(Clip.LOOP_CONTINUOUSLY); 
		} 
		catch (UnsupportedAudioFileException e) {System.out.println("Unsupported file:" + filePath);} 
		catch (IOException e) {e.printStackTrace();}
		catch (LineUnavailableException e) {e.printStackTrace();} 
	}

	public void pickRandomSong() {
		songNumber = (int)(Math.random() * (songs.length-1));
		filePath = songs[songNumber];
		ClueGame.getInstance().getGameControlPanel().setSong();
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
		this.currentFrame = this.clip.getMicrosecondPosition(); 
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
			URL url = this.getClass().getResource(filePath);
			audioInputStream = AudioSystem.getAudioInputStream(url);
			clip.open(audioInputStream); 
			clip.loop(Clip.LOOP_CONTINUOUSLY); 
			if (paused) {
				pause();
			}
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

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public void nextSong() {
		songNumber++;
		songNumber %= (songs.length);
		filePath = songs[songNumber];
		ClueGame.getInstance().getGameControlPanel().setSong();
		restart();
	} 


} 