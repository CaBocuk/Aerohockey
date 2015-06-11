package gameplay_items;

public class Controller extends Item {

	boolean isPlayer;
	boolean isMoved;
	int level;
	double strength;

	public Controller(int x, int y, int r, int s, double d, boolean p, int l, double st) {
		super(x, y, r, s, d);
		level = l;
		isPlayer = p;
		isMoved = false;
		strength = s;
	}

	public boolean isPlayer() {
		return isPlayer;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}

	public int getLevel() {
		return level;
	}

	public double getStrength() {
		return strength;
	}
}
