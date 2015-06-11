package settings;

import java.awt.Image;

public class Field {

	private Image img;
	private double goalWid;
	private double coeff;
	private String msg;

	public Field(double goalWid, double coeff, Image img, String m) {
		this.img = img;
		this.goalWid = goalWid;
		this.coeff = coeff;
		msg = m;
	}

	public Image getImg() {
		return img;
	}

	public double getGoalWid() {
		return goalWid;
	}

	public double getCoeff() {
		return coeff;
	}

	public String getMsg() {
		return msg;
	}

}
