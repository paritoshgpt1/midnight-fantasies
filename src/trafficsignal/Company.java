package trafficsignal;

public class Company implements Runnable{
	Lane lane;
	int carsAdded;

	public Company(Lane lane) {
		this.lane = lane;
	}

	@Override
	public void run() {
		while (Traffic.monitoringTime > 0 && lane.carQ.size() <= Traffic.maxCarsInALane) {
			synchronized (lane.carQ) {
				lane.carQ.offer(new Car());
				carsAdded++;
				//                System.out.println(Thread.currentThread().getName() + " Car Added: " + lane.carQ.size());
			}
			try {
				Thread.sleep(lane.waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}