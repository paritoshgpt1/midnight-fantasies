package trafficsignal;

import java.util.ArrayList;
import java.util.Random;

public class Traffic {
	static volatile long monitoringTime = 100000000;
	static volatile int maxCarsInALane = 10;
	private int numberOfLanes = 4;
	static volatile int activeLane;
	private ArrayList<Lane> lanes = new ArrayList<>();

	public static void main(String[] args) {
		System.out.println("Press Enter key to showcase swag!");
		Traffic tms = new Traffic();
		tms.activateRandomSignal();
		tms.createLanes();
		tms.addCarsToAllLanes();
		tms.startTMS();
		tms.printStatistics();
	}

	private void activateRandomSignal() {
		Random r = new Random();
		activeLane = r.nextInt(numberOfLanes-1) + 1;
	}

	private void createLanes() {
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

	private void addCarsToAllLanes() {
		for (int i = 0; i < numberOfLanes; i++) {
			Company c = new Company(lanes.get(i));
			Thread t = new Thread(c);
			t.setName("Company"+(i+1));
			t.start();
		}
	}

	private void startTMS() {
		int counter = 0;
		Thread dev = makeWay();
		while (monitoringTime > 0) {
			if (Developer.isDeveloperApproaching) {
				try {
					dev.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Developer.isDeveloperApproaching = false ;
			}
			if (counter == Signal.greenTime) {
				//                lanes.get(activeLane-1).deactivateLane();
				//                lanes.get(activeLane).activateLane();

				counter = 0;
				// Increment lane number
				activeLane = (activeLane+1)%4 + 1;
				//                System.out.println("New activeLane: " + activeLane);
			}
			counter++;
			monitoringTime--;
		}
	}

	private Thread makeWay() {
		Developer developers = new Developer() ;
		Thread t = new Thread(developers);
		t.setName("Developer");
		t.start();
		return t;
	}

	private void printStatistics() {
		for (int i = 0; i < numberOfLanes; i++) {
			int carsPassed = lanes.get(i).carsPassed;
			System.out.println("Cars Passed in Lane " + (i+1) + ": " + carsPassed);
			System.out.println("Cars Remaining in Queue" + (i+1) + ": " + lanes.get(i).carQ.size());
		}
	}
}
