/**
 * All in-game painting happens here
 */
package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

import javax.swing.JPanel;

class GamePanel extends JPanel {

	private static final long serialVersionUID = -6841890010502073761L;

	int winWid;
	int winHei;
	GameProcess p;

	Point topLeft, topRight, bottomLeft, bottomRight;

	public GamePanel(GameProcess a) {
		super();
		this.p = a;
		winWid = 0;
		winHei = 0;
	}

	public void paint(Graphics g) {
		if(p.timer.isRunning() && p.mainTimer.isRunning()){
			p.timer.stop();
		}
		
		winWid = this.getWidth();
		winHei = this.getHeight();
		topLeft = new Point((int) (winWid * p.GOAL_WID), 5);
		topRight = new Point((int) (winWid * (1 - p.GOAL_WID)), 5);
		bottomLeft = new Point((int) (winWid * p.GOAL_WID), winHei - 5);
		bottomRight = new Point((int) (winWid * (1 - p.GOAL_WID)), winHei - 5);

		/*** INITIALIZE POINS IF NEEDED ***/
		if (p.ball.getX() == -100) {
			// Initialize ball coordinates if needed
			boolean top = (new Random()).nextBoolean();
			p.ball.setX(winWid / 2);
			p.ball.setY(top ? winHei / 4 : winHei * 3 / 4);

			// Initialize firstController coordinates if needed
			p.firstController.setX(winWid / 2);
			p.firstController.setY(5 + p.firstController.getRadius());

			// Initialize secondController coordinates if needed
			p.secondController.setX(winWid / 2);
			p.secondController.setY(winHei - p.secondController.getRadius() - 5);
		}

		g.clearRect(0, 0, winWid, winHei);

		//Draw border
		paintField(g);

		paintBall(g);
		paintControllers(g);
		if(p.gameStopped && ((p.ball.getY() < winHei / 2 && !p.firstController.isPlayer()) || (p.ball.getY() > winHei/2 && !p.secondController.isPlayer()))){
			if(p.GOAL_WID == 0.3) g.setColor(Color.RED);
			else g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans MS", 20, 20));
			g.drawString("Нажмите пробел для продолжения игры", winWid/2-190, p.ball.getY() < winHei/2 ? winHei*11/16 : winHei * 5 / 16);
		}
		if(!p.mainTimer.isRunning() && !p.controllerTimer.isRunning()){
			if(p.GOAL_WID == 0.3) g.setColor(Color.RED);
			else g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans MS", 20, 20));
			g.drawString("Нажмите escape для продолжения игры", winWid/2-190, p.ball.getY() < winHei/2 ? winHei*11/16 : winHei * 5 / 16);
		}
	}

	private void paintField(Graphics g) {
		if(p.fieldImg != null) g.drawImage(p.fieldImg, 0, 0, winWid, winHei, 0, 0, p.fieldImg.getWidth(null), p.fieldImg.getHeight(null), null);
		if(p.GOAL_WID == 0.3) g.setColor(Color.RED);
		else g.setColor(Color.WHITE);
		((Graphics2D) g).setStroke(new BasicStroke(5));
		int radius = (int) (winWid * 0.15);
		g.drawOval(winWid / 2 - radius, winHei / 2 - radius, radius * 2, radius * 2);
		((Graphics2D) g).setStroke(new BasicStroke(3));
		g.drawLine(0, winHei / 2, winWid, winHei / 2);
		((Graphics2D) g).setStroke(new BasicStroke(1));
		g.drawLine(0, winHei / 4, winWid, winHei / 4);
		g.drawLine(0, winHei * 3 / 4, winWid, winHei * 3 / 4);

		((Graphics2D) g).setStroke(new BasicStroke(10));
		if(p.GOAL_WID != 0.2) g.setColor(Color.GREEN);
		else g.setColor(Color.RED);
		g.drawLine(0, 0, 0, winHei);
		g.drawLine(winWid, 0, winWid, winHei);

		g.drawLine(0, 0, (int) (winWid * p.GOAL_WID), 0);
		g.drawLine((int) (winWid * (1 - p.GOAL_WID)), 0, winWid, 0);
		g.drawLine(0, winHei, (int) (winWid * p.GOAL_WID), winHei);
		g.drawLine((int) (winWid * (1 - p.GOAL_WID)), winHei, winWid, winHei);
	} 

	private void paintBall(Graphics g) {
		Image img = p.ballImg;
		g.drawImage(img, p.ball.getX() - p.ball.getRadius(), p.ball.getY() - p.ball.getRadius(), p.ball.getX() + p.ball.getRadius(), p.ball.getY() + p.ball.getRadius(), 0, 0, img.getWidth(null), img.getHeight(null), null);
	}

	private void paintControllers(Graphics g) {
		((Graphics2D) g).setStroke(new BasicStroke(2));
		g.setColor(Color.BLUE);
		g.drawImage(p.firstImg, p.firstController.getX() - p.firstController.getRadius(), p.firstController.getY() - p.firstController.getRadius(),p.firstController.getX() + p.firstController.getRadius(), p.firstController.getY() + p.firstController.getRadius(), 0, 0, p.firstImg.getWidth(null), p.firstImg.getHeight(null), null);
		g.drawOval(p.firstController.getX() - p.firstController.getRadius(), p.firstController.getY() - p.firstController.getRadius(), p.firstController.getRadius()*2, p.firstController.getRadius()*2);
		
		g.drawImage(p.secondImg, p.secondController.getX() - p.secondController.getRadius(), p.secondController.getY() - p.secondController.getRadius(),p.secondController.getX() + p.secondController.getRadius(), p.secondController.getY() + p.secondController.getRadius(), 0, 0, p.secondImg.getWidth(null), p.secondImg.getHeight(null), null);
		g.drawOval(p.secondController.getX() - p.secondController.getRadius(), p.secondController.getY() - p.secondController.getRadius(), p.secondController.getRadius()*2, p.secondController.getRadius()*2);

	}

}