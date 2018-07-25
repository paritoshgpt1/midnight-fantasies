package traffcsignal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


import javax.sound.sampled.*;

public class Traffic {
    static volatile long monitoringTime = 1000000;
    static volatile int maxCarsInALane = 10;
    private int numberOfLanes = 4;
    static volatile int activeLane;
    private ArrayList<Lane> lanes = new ArrayList<>();

    public static void main(String[] args) {
        Traffic tms = new Traffic();
        tms.activateRandomSignal();
        tms.createLanes();
        tms.addCarsToAllLanes();
        tms.startTMS();
        tms.printStatistics();
        tms.makeWay();
    }

    private void activateRandomSignal(){
        Random r = new Random();
        activeLane = r.nextInt(numberOfLanes-1) + 1;
    }

    private void createLanes(){
        for (int i = 0; i < numberOfLanes; i++) {
            String signalColor = "red";
            if(activeLane == i+1) signalColor = "green";
            Lane l = new Lane(i+1, signalColor);
            Thread t = new Thread(l);
            t.setName("Lane" + (i+1));
            lanes.add(l);
            t.start();
        }
    }

    private void addCarsToAllLanes(){
        for (int i = 0; i < numberOfLanes; i++) {
            Company c = new Company(lanes.get(i));
            Thread t = new Thread(c);
            t.setName("Company"+(i+1));
            t.start();
        }
    }

    private void startTMS(){
        int counter = 0;
        while (monitoringTime > 0) {
            if (counter == Signal.greenTime) {
//                lanes.get(activeLane-1).deactivateLane();
//                lanes.get(activeLane).activateLane();

                counter = 0;
                // Increment lane number
                activeLane = (activeLane+1)%4+1;
//                System.out.println("New activeLane: " + activeLane);
            }
            counter++;
            monitoringTime--;
        }
    }

    private void printStatistics(){
        for (int i = 0; i < numberOfLanes; i++) {
            int carsPassed = lanes.get(i).carsPassed;
            System.out.println("Cars Passed in Lane " + (i+1) + ": " + carsPassed);
            System.out.println("Cars Remaining in Queue" + (i+1) + ": " + lanes.get(i).carQ.size());
        }
    }

    void makeWay(){
        File soundFile = new File("cello.wav");
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(soundFile);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Clip clip = null;
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

        clip.start();  // play once
//// Loop()
//        clip.loop(0);  // repeat none (play once), can be used in place of start().
//        clip.loop(5);  // repeat 5 times (play 6 times)
//        clip.loop(Clip.LOOP_CONTINUOUSLY);
//        if (clip.isRunning()) clip.stop();


    }


}
