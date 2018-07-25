package trafficsignal;

public class Signal {
	String currentLight;
	static int redTime = 21;
	static int yelloTime = 2;
	static int greenTime = 500;

	public Signal(String currentLight) {
		this.currentLight = currentLight;
	}

	void changeLight(String currentLight) {
		this.currentLight = currentLight;
	}
}
