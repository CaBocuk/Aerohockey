/**
 * Ball and Controller physics are here
 */

package graphics;

import gameplay_items.Controller;
import graphics.GameProcess;

import java.awt.Point;

import javax.swing.JOptionPane;

public final class ClashVerifyer {

	private static final double PI = Math.PI;

	public static boolean clashedController(GameProcess h) {

		if (h.ball.getY() < h.panel.winWid / 2) {
			double xs = (h.ball.getX() - h.firstController.getX()) * (h.ball.getX() - h.firstController.getX());
			double ys = (h.ball.getY() - h.firstController.getY()) * (h.ball.getY() - h.firstController.getY());
			double rads = (h.ball.getRadius() + h.firstController.getRadius()) * (h.ball.getRadius() + h.firstController.getRadius());

			if (xs + ys <= rads) {
				if (h.gameStopped) {
					h.gameStopped = false;
					h.mainTimer.start();
					h.panel.repaint();
				}
				// h.ball.getAngle() = PI + h.ball.getAngle();
				boolean is1 = h.ball.getX() > h.firstController.getX() && h.ball.getY() < h.firstController.getY();
				boolean is2 = h.ball.getX() < h.firstController.getX() && h.ball.getY() < h.firstController.getY();
				boolean is3 = h.ball.getX() < h.firstController.getX() && h.ball.getY() > h.firstController.getY();
				boolean is4 = h.ball.getX() > h.firstController.getX() && h.ball.getY() > h.firstController.getY();
				double hypotenuze = Math.sqrt(xs + ys);
				double alpha = Math.asin((double) (h.firstController.getY() - h.ball.getY()) / hypotenuze);
				alpha = is1 ? alpha : is2 ? PI - alpha : is3 ? -PI - alpha : is4 ? alpha : (PI + h.ball.getAngle() % (2 * PI)) % (2 * PI);
				h.ball.setAngle(alpha % (2 * PI));

				if (h.firstController.isMoved()) {
					int speed1 = h.firstController.getSpeed() * h.controllerTimerSpeed / 10;
					int speed2 = h.ball.getSpeed();
					double angle1 = h.firstController.getAngle() % (2 * PI);
					double angle2 = h.ball.getAngle() % (2 * PI);
					angle1 = (angle1 < 0) ? 2 * PI + angle1 % (2 * PI) : angle1 % (2 * PI);
					angle2 = (angle2 < 0) ? 2 * PI + angle2 % (2 * PI) : angle2 % (2 * PI);

					int speed3 = (int) Math.sqrt(speed1 * speed1 + speed2 * speed2 - 2 * speed1 * speed2 * Math.cos(PI - angle1 + angle2));
					if (Math.abs(angle1 - angle2) > PI / 2) {
						speed3 = (int) (-Math.sqrt(speed3) * 7 / (Math.abs(speed1 - speed2) + 1) * h.firstController.getStrength());
						speed3 = (speed3 < -h.ball.getInitialSpeed() + 1) ? -h.ball.getInitialSpeed() + 1 : speed3;
					} else {
						speed3 = (int) (Math.sqrt(speed3) * 7 / (Math.abs(speed1 - speed2) + 1) * h.firstController.getStrength());
						speed3 = (speed3 > h.ball.getInitialSpeed()) ? h.ball.getInitialSpeed() : speed3;
					}
					h.ball.setSpeed(h.ball.getSpeed() + speed3);
					if (h.ball.getSpeed() < 1) {
						h.ball.setSpeed(1);
					} else if (h.ball.getSpeed() > h.ball.getInitialSpeed() * 2) {
						h.ball.setSpeed(h.ball.getInitialSpeed() * 2);
					}
				}

				double dif = h.ball.getRadius() + h.firstController.getRadius() - Math.sqrt(xs + ys);
				h.ball.setX((int) (h.ball.getX()+dif * Math.cos(h.ball.getAngle())));
				h.ball.setY((int) (h.ball.getY()-dif * Math.sin(h.ball.getAngle())));
				return true;
			}
		} else {
			double xs = (h.ball.getX() - h.secondController.getX()) * (h.ball.getX() - h.secondController.getX());
			double ys = (h.ball.getY() - h.secondController.getY()) * (h.ball.getY() - h.secondController.getY());
			double rads = (h.ball.getRadius() + h.secondController.getRadius()) * (h.ball.getRadius() + h.secondController.getRadius());

			if (xs + ys <= rads) {
				if (h.gameStopped) {
					h.gameStopped = false;
					h.mainTimer.start();
					h.panel.repaint();
				}
				// h.ball.getAngle() = PI + h.ball.getAngle();
				boolean is1 = h.ball.getX() > h.secondController.getX() && h.ball.getY() < h.secondController.getY();
				boolean is2 = h.ball.getX() < h.secondController.getX() && h.ball.getY() < h.secondController.getY();
				boolean is3 = h.ball.getX() < h.secondController.getX() && h.ball.getY() > h.secondController.getY();
				boolean is4 = h.ball.getX() > h.secondController.getX() && h.ball.getY() > h.secondController.getY();
				double hypotenuze = Math.sqrt(xs + ys);
				double alpha = Math.asin((double) (h.secondController.getY() - h.ball.getY()) / hypotenuze);
				alpha = is1 ? alpha : is2 ? PI - alpha : is3 ? -PI - alpha : is4 ? alpha : PI + h.ball.getAngle();
				h.ball.setAngle(alpha);

				if (h.secondController.isMoved()) {
					int speed1 = h.secondController.getSpeed() * h.controllerTimerSpeed / 10;
					int speed2 = h.ball.getSpeed();
					double angle1 = h.secondController.getAngle() % (2 * PI);
					double angle2 = h.ball.getAngle() % (2 * PI);
					int speed3 = (int) Math.sqrt(speed1 * speed1 + speed2 * speed2 - 2 * speed1 * speed2 * Math.cos(PI - angle1 + angle2));
					if (Math.abs(angle1 - angle2) > PI / 2) {
						speed3 = (int) (-Math.sqrt(speed3) * 7 / (Math.abs(speed1 - speed2) + 1) * h.secondController.getStrength());
						speed3 = (speed3 < -h.ball.getInitialSpeed() + 1) ? -h.ball.getInitialSpeed() + 1 : speed3;
					} else {
						speed3 = (int) (Math.sqrt(speed3) * 7 / (Math.abs(speed1 - speed2) + 1) * h.secondController.getStrength());
						speed3 = (speed3 > h.ball.getInitialSpeed()) ? h.ball.getInitialSpeed() : speed3;
					}
					h.ball.setSpeed(h.ball.getSpeed() + speed3);
					if (h.ball.getSpeed() < 1) {
						h.ball.setSpeed(1);
					} else if (h.ball.getSpeed() > h.ball.getInitialSpeed() * 2) {
						h.ball.setSpeed(h.ball.getInitialSpeed() * 2);
					}
				}

				double dif = h.ball.getRadius() + h.secondController.getRadius() - Math.sqrt(xs + ys);
				h.ball.setX((int) (h.ball.getX()+dif * Math.cos(h.ball.getAngle())));
				h.ball.setY((int) (h.ball.getY()-dif * Math.sin(h.ball.getAngle())));
				return true;
			}
		}

		return false;
	}

	public static boolean clashedWall(GameProcess h) {

		if (h.panel.winWid != 0 && !clashedPost(h)) {
			// right and left clash
			if (h.ball.getX() - h.ball.getRadius() < 5 || h.ball.getX() + h.ball.getRadius() > h.panel.winWid - 5) {
				h.ball.setX((h.ball.getX() - h.ball.getRadius() < 5) ? 5 + h.ball.getRadius() : h.panel.winWid - 5 - h.ball.getRadius());
				h.ball.setAngle(PI - h.ball.getAngle());

				if (h.ball.getX() < h.panel.winWid / 2) {
					if (h.ball.getY() < h.panel.winHei / 2) {
						fixIfClashedWallAndController(PI / 2, 3 * PI / 2, h, h.firstController);
					} else {
						fixIfClashedWallAndController(PI / 2, 3 * PI / 2, h, h.secondController);
					}
				} else {
					if (h.ball.getY() < h.panel.winHei / 2) {
						fixIfClashedWallAndController(3 * PI / 2, PI / 2, h, h.firstController);
					} else {
						fixIfClashedWallAndController(3 * PI / 2, PI / 2, h, h.secondController);
					}
				}

				return true;
			}

			// up and down clash, clear goals
			if ((h.ball.getY() - h.ball.getRadius() < 5 || h.ball.getY() + h.ball.getRadius() > h.panel.winHei - 5) && !(h.ball.getX() - h.ball.getRadius() > h.panel.winWid * h.GOAL_WID && h.ball.getX() + h.ball.getRadius() < h.panel.winWid * (1 - h.GOAL_WID))) { // up
				// clash
				h.ball.setY((h.ball.getY() - h.ball.getRadius() < 5) ? 5 + h.ball.getRadius() : h.panel.winHei - 5 - h.ball.getRadius());
				h.ball.setAngle(-h.ball.getAngle());

				if (h.ball.getY() < h.panel.winHei / 2) {
					fixIfClashedWallAndController(0, PI, h, h.firstController);
				} else {
					fixIfClashedWallAndController(PI, 2 * PI, h, h.secondController);
				}
				return true;
			} else if ((h.ball.getX() - h.ball.getRadius() > h.panel.winWid * h.GOAL_WID && h.ball.getX() + h.ball.getRadius() < h.panel.winWid * (1 - h.GOAL_WID)) && h.ball.getY() < -h.ball.getRadius()) { // top
																																												// goal
				h.mainTimer.stop();
				h.gameStopped = true;
				h.ball.setX( h.panel.winWid / 2);
				h.ball.setY(h.panel.winHei / 4);
				h.firstController.setX(h.panel.winWid / 2);
				h.firstController.setY(5 + h.firstController.getRadius());
				h.secondController.setX(h.panel.winWid / 2);
				h.secondController.setY(h.panel.winHei - h.secondController.getRadius() - 5);
				h.score.y++;
				h.ball.setSpeed( h.ball.getInitialSpeed() / 3);
				h.ball.setAngle(-PI / 2);
				h.label.setText("<html><font color='red' size=10>СЧЕТ: " + h.score.x + " - " + h.score.y + "</font></html>");
				if(h.score.y == 7){
					int dialogResult = JOptionPane.showConfirmDialog(h, "Игрок 2 выиграл! Начать игру заново?", "Игра окончена!", JOptionPane.YES_NO_OPTION);
					if(dialogResult == 0){
						h.restart();
					}else{
						h.parent.setVisible(true);
						h.setEnabled(false);
						h.setVisible(false);
					}
				}
				return true;
			} else if ((h.ball.getX() - h.ball.getRadius() > h.panel.winWid * h.GOAL_WID && h.ball.getX() + h.ball.getRadius() < h.panel.winWid * (1 - h.GOAL_WID)) && h.ball.getY() > h.panel.winHei + h.ball.getRadius()) { // down
																																																// goal
				h.mainTimer.stop();
				h.gameStopped = true;
				h.ball.setX(h.panel.winWid / 2);
				h.ball.setY( h.panel.winHei / 4 * 3);
				h.firstController.setX(h.panel.winWid / 2);
				h.firstController.setY(5 + h.firstController.getRadius());
				h.secondController.setX(h.panel.winWid / 2);
				h.secondController.setY(h.panel.winHei - h.secondController.getRadius() - 5);
				h.score.x++;
				h.ball.setSpeed(h.ball.getInitialSpeed() / 3);
				h.ball.setAngle(PI / 2);
				h.label.setText("<html><font color='red' size=10>СЧЕТ: " + h.score.x + " - " + h.score.y + "</font></html>");
				if(h.score.x == 7){
					int dialogResult = JOptionPane.showConfirmDialog(h, "Игрок 1 выиграл! Начать игру заново?", "Игра окончена!", JOptionPane.YES_NO_OPTION);
					if(dialogResult == 0){
						h.restart();
					}else{
						h.parent.setVisible(true);
						h.setEnabled(false);
						h.setVisible(false);
					}
				}
				return true;
			}
		}
		return false;
	}

	private static void fixIfClashedWallAndController(double min, double max, GameProcess h, Controller controller) {
		double xs = (h.ball.getX() - controller.getX()) * (h.ball.getX() - controller.getX());
		double ys = (h.ball.getY() - controller.getY()) * (h.ball.getY() - controller.getY());
		double rads = (h.ball.getRadius() + controller.getRadius()) * (h.ball.getRadius() + controller.getRadius());

		if (xs + ys <= rads) {
			boolean is1 = h.ball.getX() > controller.getX() && h.ball.getY() < controller.getY();
			boolean is2 = h.ball.getX() < controller.getX() && h.ball.getY() < controller.getY();
			boolean is3 = h.ball.getX() < controller.getX() && h.ball.getY() > controller.getY();
			boolean is4 = h.ball.getX() > controller.getX() && h.ball.getY() > controller.getY();
			double hypotenuze = Math.sqrt(xs + ys);
			double alpha = Math.asin((double) (controller.getY() - h.ball.getY()) / hypotenuze);
			alpha = is1 ? alpha : is2 ? PI - alpha : is3 ? -PI - alpha : is4 ? alpha : (PI + h.ball.getAngle() % (2 * PI)) % (2 * PI);
			alpha = (alpha < 0) ? 2 * PI + alpha % (2 * PI) : alpha % (2 * PI);
			min = (min == 3*PI/2) ? -PI/2 : min;
			if (alpha > min && alpha < max) {
				alpha = PI + alpha;
				double dif = h.ball.getRadius() + controller.getRadius() - Math.sqrt(xs + ys);
				controller.setX((int) (controller.getX() + dif*Math.cos(alpha)));
				controller.setY((int) (controller.getY()-dif * Math.sin(alpha)));
			}
		}
	}

	public static boolean clashedPost(GameProcess h) {

		int type = 0;
		if (h.ball.getX() < h.panel.winWid / 2) {
			if (h.ball.getY() < h.panel.winHei / 2) {
				type = 0;
			} else {
				type = 2;
			}
		} else {
			if (h.ball.getY() < h.panel.winHei / 2) {
				type = 1;
			} else {
				type = 3;
			}
		}
		Point corn = (type == 0) ? h.panel.topLeft : (type == 3) ? h.panel.bottomRight : (type == 1) ? h.panel.topRight : h.panel.bottomLeft;

		if (type == 0) { // left top
			if (h.ball.contains(corn.x, corn.y) || (h.ball.getY() - Math.sin(h.ball.getAngle()) * h.ball.getSpeed() <= 5 && h.ball.getX() + Math.cos(h.ball.getAngle()) * h.ball.getSpeed() <= h.panel.winWid * h.GOAL_WID + h.ball.getRadius())) {

				int y = (int) (corn.y + h.ball.getX() - corn.x);
				if (h.ball.getY() > y) {
					return false;
				} else if (h.ball.getY() < y) {
					h.ball.setAngle(PI - h.ball.getAngle());
				} else {
					h.ball.setAngle(PI + h.ball.getAngle());
				}
				return true;
			} else {
				return false;
			}
		} else if (type == 1) { // right top
			if (h.ball.contains(corn.x, corn.y) || (h.ball.getY() - Math.sin(h.ball.getAngle()) * h.ball.getSpeed() <= 5 && h.ball.getX() + Math.cos(h.ball.getAngle()) * h.ball.getSpeed() >= h.panel.winWid * (1 - h.GOAL_WID) - h.ball.getRadius())) {

				int y = corn.y - h.ball.getX() + corn.x;
				if (h.ball.getY() > y) {
					return false;
				} else if (h.ball.getY() < y) {
					h.ball.setAngle(PI - h.ball.getAngle());
				} else {
					h.ball.setAngle(PI + h.ball.getAngle());
				}
				return true;
			} else {
				return false;
			}
		} else if (type == 2) { // bottom left
			if (h.ball.contains(corn.x, corn.y) || (h.ball.getY() - Math.sin(h.ball.getAngle()) * h.ball.getSpeed() >= h.panel.winHei - 5 && h.ball.getX() - h.ball.getRadius() + Math.cos(h.ball.getAngle()) * h.ball.getSpeed() <= h.panel.winWid * h.GOAL_WID)) {
				int y = corn.y - h.ball.getX() + corn.x;
				if (h.ball.getY() > y) {
					h.ball.setAngle(PI - h.ball.getAngle());
				} else if (h.ball.getY() < y) {
					return false;
				} else {
					h.ball.setAngle(PI + h.ball.getAngle());
				}
				return true;
			} else {
				return false;
			}
		} else if (type == 3) { // bottom right
			if (h.ball.contains(corn.x, corn.y) || (h.ball.getY() - Math.sin(h.ball.getAngle()) * h.ball.getSpeed() >= h.panel.winHei - 5 && h.ball.getX() + Math.cos(h.ball.getAngle()) * h.ball.getSpeed() >= h.panel.winWid * (1 - h.GOAL_WID) - h.ball.getRadius())) {

				int y = corn.y + h.ball.getX() - corn.x;
				if (h.ball.getY() > y) {
					h.ball.setAngle(PI - h.ball.getAngle());
				} else if (h.ball.getY() < y) {
					return false;
				} else {
					h.ball.setAngle(PI + h.ball.getAngle());
				}
				return true;
			} else {
				return false;
			}
		}

		return false;
	}
}
