package trafficsignal;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Developer implements Runnable {
	static volatile boolean isDeveloperApproaching = false ;
	int developerPassTime = 8000 ;
	static Clip clip = null;

	static void startScanner() {
		Scanner in = new Scanner(System.in);
		if(in.hasNextLine()) isDeveloperApproaching = true ;
		in.close();
	}

	@Override
	public void run() {
		startScanner() ;
		if (isDeveloperApproaching) {
			System.out.println("All Lanes Down.. Developers approaching!!");
			File soundFile = new File("swag.wav");
			AudioInputStream audioIn = null;
			try {
				audioIn = AudioSystem.getAudioInputStream(soundFile);
			} catch (UnsupportedAudioFileException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				clip = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
			try {
				clip.open(audioIn);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			clip.start();
			try {
				Thread.sleep(developerPassTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clip.stop();
			System.out.println("Developers clear.. Resuming system..");
		}
	}
}
