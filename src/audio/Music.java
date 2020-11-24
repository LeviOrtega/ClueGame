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
	static Long currentFrame; 
	Clip clip; 
	String[] songs = {"/Song1.wav", "/Song2.wav","/Song3.wav","/Song4.wav", "/Song5.wav"};
	int songNumber;
	static boolean paused = false;
	AudioInputStream audioInputStream; 
	String filePath; 

	// constructor to initialize streams and clip 
	public Music(){} 

	public synchronized void initialize() {

		new Thread(new Runnable() {
			public void run() {
				try {
					// create AudioInputStream object
					URL url = this.getClass().getResource(filePath);

					audioInputStream = AudioSystem.getAudioInputStream(url);

					// create clip reference 
					clip = AudioSystem.getClip(); 

					// open audioInputStream to the clip 
					clip.open(audioInputStream); 

					clip.loop(Clip.LOOP_CONTINUOUSLY); 

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();
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
	public synchronized void play()  
	{ 
		new Thread(new Runnable() {
			public void run() {
				try {
					// start the clip
					clip.start();

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();

	} 

	// Method to pause the audio 
	public synchronized void pause()  
	{ 

		new Thread(new Runnable() {
			public void run() {
				try {
					currentFrame = clip.getMicrosecondPosition(); 
					clip.stop(); 

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();

	} 

	// Method to resume the audio 
	public synchronized void resumeAudio() 
	{ 
		new Thread(new Runnable() {
			public void run() {
				try {
					clip.stop();
					clip.setMicrosecondPosition(currentFrame); 
					clip.start();


				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();

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
	public synchronized void stop() 
	{ 

		new Thread(new Runnable() {
			public void run() {
				try {
					currentFrame = 0L; 
					clip.stop(); 
					clip.close(); 

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();


	} 


	// Method to reset audio stream 
	public synchronized void resetAudioStream() 
	{ 
		new Thread(new Runnable() {
			public void run() {
				try {
					URL url = this.getClass().getResource(filePath);
					audioInputStream = AudioSystem.getAudioInputStream(url);
					clip.open(audioInputStream); 
					clip.loop(Clip.LOOP_CONTINUOUSLY); 
					if (paused) {
						pause();
					}

				} 
				catch(IllegalStateException e) {
					// this is thrown if the player clicks on the next button too fast and it plays the same soung
					// do nothing with this exception
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();




	}

	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public synchronized void nextSong() {

		new Thread(new Runnable() {
			public void run() {
				try {

					songNumber++;
					songNumber %= (songs.length);
					filePath = songs[songNumber];
					ClueGame.getInstance().getGameControlPanel().setSong();
					restart();

				} catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
		}).start();

	} 


} 