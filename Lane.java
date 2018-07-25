package cmusignal;

import java.util.LinkedList;
import java.util.Queue;

public class Lane implements Runnable{
    volatile Queue<Car> carQ = new LinkedList<>();
    int carsPassed = 0;
    Signal signal;
    int waitTime;
    int id;
    public Lane(int id, String signalColor) {
        this.id = id;
        signal = new Signal(signalColor);
    }

    void activateLane(){
//        System.out.println("Activating signal for " + Thread.currentThread().getName());
        signal.changeLight("green");
    }

    void deactivateLane(){
//        System.out.println("Deactivating signal for " + Thread.currentThread().getName());
        signal.changeLight("red");
    }

    @Override
    public void run() {
        while(Traffic.monitoringTime > 0) {

            if(Traffic.activeLane == id) activateLane();
            else deactivateLane();

            if(signal.currentLight.equals("green")) {
                while (carQ.isEmpty()) { }
                Car c;
                synchronized (carQ) {
                    c = carQ.poll();
                    carsPassed++;
//                    System.out.println(Thread.currentThread().getName() + " carsPassed: " + carsPassed);
                }
                try {
                    Thread.sleep(c.passTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
