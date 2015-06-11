package gameplay_items;

public abstract class Item {

	int x;

	int y;
	int radius;
	int speed;
	double angle;

	public Item(int x, int y, int r, int s, double a) {
		this.x = x;
		this.y = y;
		this.radius = r;
		this.speed = s;
		this.angle = a;
	}

	public boolean contains(int x, int y) {
		int xs = (this.x - x) * (this.x - x);
		int ys = (this.y - y) * (this.y - y);

		if (xs + ys <= this.radius * this.radius) {
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getRadius() {
		return radius;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}
}
