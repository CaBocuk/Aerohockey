package gameplay_items;

public class Ball extends Item {

	int initialSpeed;

	public Ball(int x, int y, int r, int s, double d) {
		super(x, y, r, s, d);
		initialSpeed = s;
	}

	public int getInitialSpeed() {
		return initialSpeed;
	}

	public void setInitialSpeed(int initialSpeed) {
		this.initialSpeed = initialSpeed;
	}
}
