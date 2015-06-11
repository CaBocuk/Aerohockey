package settings;

import java.awt.Image;

public class Ball {
	private int radius;
	private int speed;
	private Image img;
	private String msg;

	public Ball(int radius, int speed, Image img, String m) {
		super();
		this.radius = radius;
		this.speed = speed;
		this.img = img;
		this.msg = m;
	}

	public int getRadius() {
		return radius;
	}

	public int getSpeed() {
		return speed;
	}

	public Image getImg() {
		return img;
	}

	public String getMsg() {
		return msg;
	}
}
