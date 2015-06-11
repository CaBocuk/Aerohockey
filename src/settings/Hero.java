package settings;

import java.awt.Image;

public class Hero {

	private int level;
	private int speed;
	private int radius;
	private double strength;
	private Image img;
	private String msg;

	public Hero(int l, int s, int r, double st, Image i, String m) {
		level = l;
		speed = s;
		radius = r;
		msg = m;
		strength = st;
		img = i;
	}

	public int getLevel() {
		return level;
	}

	public int getSpeed() {
		return speed;
	}

	public int getRadius() {
		return radius;
	}

	public double getStrength() {
		return strength;
	}

	public Image getImg() {
		return img;
	}

	public String getMsg() {
		return msg;
	}
}
