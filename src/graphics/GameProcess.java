/**
 * A lot of different things are here :)
 * Moving the controllers, speed-control, etc.
 */
package graphics;

import gameplay_items.Ball;
import gameplay_items.Controller;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class GameProcess extends JFrame implements ActionListener {

	private static final long serialVersionUID = -4104528357686357214L;
	private JPanel contentPane;
	private final GameProcess frame = this;
	private final KeyListener keyListener;
	StartWindow parent;

	Image fieldImg;
	Image ballImg;
	Image firstImg;
	Image secondImg;

	boolean isUpPressed = false;
	boolean isDownPressed = false;
	boolean isLeftPressed = false;
	boolean isRightPressed = false;
	boolean isWPressed = false;
	boolean isSPressed = false;
	boolean isAPressed = false;
	boolean isDPressed = false;
	int firstKeysPressed = 0;
	int secondKeysPressed = 0;

	private int counter = 0;

	static final double PI = Math.PI;

	double accumulatedSpeed;
	double frictionForce;
	int gameType;
	JLabel label;
	Timer mainTimer;
	Timer timer;
	Timer controllerTimer;
	Controller firstController;
	Controller secondController;
	Ball ball;
	GamePanel panel;
	int timerSpeed;
	int controllerTimerSpeed;
	Point score;
	boolean gameStopped;
	final double GOAL_WID;

	double speedCoeff = 0.15;
	private JMenuBar menuBar;
	private JMenuItem mntmSettings;
	private JMenuItem mntmIfABug;
	private JMenuItem mntmAbout;

	public GameProcess(StartWindow parent) {
		setTitle("Aerohockey");
		this.parent = parent;
		accumulatedSpeed = 0;
		frictionForce = parent.settings.getField().getCoeff();
		fieldImg = parent.settings.getField().getImg();
		ballImg = parent.settings.getBall().getImg();
		GOAL_WID = parent.settings.getField().getGoalWid();
		gameType = parent.settings.getGameType();
		firstImg = parent.settings.getFirstHero().getImg();
		secondImg = parent.settings.getSecondHero().getImg();
		timerSpeed = 5;
		controllerTimerSpeed = 5;
		mainTimer = new Timer(timerSpeed, this);
		ball = new Ball(-100, -100, parent.settings.getBall().getRadius(), parent.settings.getBall().getSpeed(), PI / 2);
		firstController = new Controller(-1, -1, parent.settings.getFirstHero().getRadius(), parent.settings.getFirstHero().getSpeed(), 0, (gameType < 2) ? true : false, parent.settings.getFirstHero().getLevel(), parent.settings.getFirstHero().getStrength());
		secondController = new Controller(-1, -1, parent.settings.getSecondHero().getRadius(), parent.settings.getSecondHero().getSpeed(), 0, (gameType == 0 || gameType == 2) ? true : false, parent.settings.getSecondHero().getLevel(), parent.settings.getSecondHero().getStrength());
		score = new Point(0, 0);
		gameStopped = true;
		keyListener = new MyKeyListener(frame);

		timer = new Timer(controllerTimerSpeed, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.repaint();
				if (!ClashVerifyer.clashedController(frame)) {
					if (ball.getY() < panel.winHei / 2 && !firstController.isPlayer()) {
						firstController.setY((int) (firstController.getY() + firstController.getSpeed() * 2 * speedCoeff));
						firstController.setAngle(3 * PI / 2);
					} else if (!secondController.isPlayer() && ball.getY() > panel.winHei / 2) {
						secondController.setY((int) (secondController.getY() - secondController.getSpeed() * 2 * speedCoeff));
						secondController.setAngle(PI / 2);
					}
				} else {
					mainTimer.start();
				}
			}
		});

		controllerTimer = new Timer(25, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int x = firstController.getX();
				int y = firstController.getY();
				firstKeysPressed = 0;
				secondKeysPressed = 0;
				double angle1 = 0;
				double angle2 = 0;
				firstController.setMoved(false);
				secondController.setMoved(false);
				if (firstController.isPlayer()) {
					if (isLeftPressed && x > 5 + firstController.getRadius()) {
						x -= firstController.getSpeed() * controllerTimerSpeed / 10;
						firstController.setMoved(true);
						firstKeysPressed++;
						angle1 += PI;
					}
					if (isRightPressed && x < panel.getWidth() - firstController.getRadius() - 5) {
						x += firstController.getSpeed() * controllerTimerSpeed / 10;
						firstController.setAngle(firstController.getAngle() + PI);
						firstController.setMoved(true);
						firstKeysPressed++;
						if (isDownPressed)
							angle1 += 2 * PI;

					}
					if (isUpPressed && y > 5 + firstController.getRadius()) {
						y -= firstController.getSpeed() * controllerTimerSpeed / 10;
						firstController.setMoved(true);
						firstKeysPressed++;
						angle1 += PI / 2;
					}
					if (isDownPressed && y < panel.getHeight() / 4 - firstController.getRadius() - 5) {
						y += firstController.getSpeed() * controllerTimerSpeed / 10;
						firstController.setMoved(true);
						firstKeysPressed++;
						angle1 += 3 * PI / 2;
					}
				}
				if (firstKeysPressed < 3 && firstKeysPressed > 0) {
					firstController.setAngle(angle1 / firstKeysPressed);
				} else if (firstKeysPressed == 3) {
					if (isLeftPressed && isRightPressed) {
						if (isUpPressed)
							firstController.setAngle(PI / 2);
						else if (isDownPressed)
							firstController.setAngle(3 * PI / 2);
						else
							firstController.setMoved(false);
					} else if (isDownPressed && isUpPressed) {
						if (isLeftPressed)
							firstController.setAngle(PI);
						else if (isRightPressed)
							firstController.setAngle(0);
						else
							firstController.setMoved(false);
					}
				} else {
					firstController.setMoved(false);
				}
				firstController.setX(x);
				firstController.setY(y);

				x = secondController.getX();
				y = secondController.getY();
				if (secondController.isPlayer()) {
					if (isAPressed && x > 5 + secondController.getRadius()) {
						x -= secondController.getSpeed() * controllerTimerSpeed / 10;
						secondController.setMoved(true);
						secondKeysPressed++;
						angle2 += PI;
					}
					if (isDPressed && x < panel.getWidth() - secondController.getRadius() - 5) {
						x += secondController.getSpeed() * controllerTimerSpeed / 10;
						secondController.setMoved(true);
						secondKeysPressed++;
						if (isSPressed)
							angle1 += 2 * PI;
					}
					if (isWPressed && y > 5 + secondController.getRadius() + panel.getHeight() * 3 / 4) {
						y -= secondController.getSpeed() * controllerTimerSpeed / 10;
						secondController.setMoved(true);
						secondKeysPressed++;
						angle2 += PI / 2;
					}
					if (isSPressed && y < panel.getHeight() - secondController.getRadius() - 5) {
						y += secondController.getSpeed() * controllerTimerSpeed / 10;
						secondController.setMoved(true);
						secondKeysPressed++;
						angle2 += 3 * PI / 2;
					}
				}
				if (secondKeysPressed < 3 && secondKeysPressed > 0) {
					secondController.setAngle(angle2 / secondKeysPressed);
				} else if (secondKeysPressed == 3) {
					if (isAPressed && isDPressed) {
						if (isWPressed)
							secondController.setAngle(PI / 2);
						else if (isSPressed)
							secondController.setAngle(3 * PI / 2);
						else
							secondController.setMoved(false);
					} else if (isSPressed && isWPressed) {
						if (isAPressed)
							secondController.setAngle(PI);
						else if (isDPressed)
							secondController.setAngle(0);
						else
							secondController.setMoved(false);
					}
				} else {
					secondController.setMoved(false);
				}
				secondController.setX(x);
				secondController.setY(y);
				ClashVerifyer.clashedController(frame);
				repaint();
			}

		});
		controllerTimer.start();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 500, 700);
		setResizable(false);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mntmSettings = new JMenuItem("Вернуться в меню");
		mntmSettings.setHorizontalAlignment(SwingConstants.CENTER);
		mntmSettings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				parent.setVisible(true);
				frame.setEnabled(false);
				frame.setVisible(false);
			}
		});
		menuBar.add(mntmSettings);
		
		mntmIfABug = new JMenuItem("Если вдруг случился баг");
		mntmIfABug.setBackground(Color.LIGHT_GRAY);
		mntmIfABug.setHorizontalAlignment(SwingConstants.CENTER);
		mntmIfABug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isMain = mainTimer.isRunning();
				boolean isController = controllerTimer.isRunning();
				mainTimer.stop();	
				controllerTimer.stop();
				JOptionPane.showMessageDialog(null, "Пожалуйста, нажмите на F5, чтобы все стало хорошо.");
				if(isMain) mainTimer.start();
				if(isController) controllerTimer.start();
			}
		});
		menuBar.add(mntmIfABug);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.setHorizontalAlignment(SwingConstants.CENTER);
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean isMain = mainTimer.isRunning();
				boolean isController = controllerTimer.isRunning();
				mainTimer.stop();	
				controllerTimer.stop();
				JOptionPane.showMessageDialog(null, "Савосик Артём, 2015г.");
				if(isMain) mainTimer.start();
				if(isController) controllerTimer.start();
			}
		});
		menuBar.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel = new GamePanel(this);
		contentPane.add(panel, BorderLayout.CENTER);

		label = new JLabel("<html><font color='red' size=10>СЧЕТ: 0 - 0</font></html>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(label, BorderLayout.NORTH);
		this.addKeyListener(keyListener);
		//mainTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		counter = (counter > 10) ? 0 : counter + 1;
		if (!ClashVerifyer.clashedWall(frame)) {
			if (ball.getSpeed() > ball.getInitialSpeed()) {
				accumulatedSpeed += frictionForce;
				ball.setSpeed((int) (ball.getSpeed() + 0.5 - accumulatedSpeed));
				if (accumulatedSpeed >= 0.5 + frictionForce)
					accumulatedSpeed = 0;
			} else {
				accumulatedSpeed = 0;
			}
			double x = Math.cos(ball.getAngle()) * ball.getSpeed();
			double y = Math.sin(ball.getAngle()) * ball.getSpeed();
			boolean isPi = Math.abs(ball.getAngle() % (PI / 2)) < 0.0001;
			x += isPi ? 0 : (x > 0) ? 1.5 : -1.5;
			y -= isPi ? 0 : (y > 0) ? -1.5 : 1.5;
			ball.setX((int) (ball.getX() + x));
			ball.setY((int) (ball.getY() - y));
		}
		moveControllers();
		ClashVerifyer.clashedController(frame);

		panel.repaint();
	}

	private void moveControllers() { //if the controller is not moved by a person
		
		/****************************** FIRST CONTROLLER*****************************/
		if (!firstController.isPlayer()) {
			int x = firstController.getX();
			int y = firstController.getY();
			int prevX = x;
			int prevY = y;
			int level = firstController.getLevel();
			if (level == 0) {
				Random r = new Random();
				if (counter % 12 == 0) {
					do {
						x = firstController.getX();
						y = firstController.getY();
						x += firstController.getSpeed() * Math.pow(-1, (r.nextInt() % 2)) * 5 * speedCoeff;
						y -= firstController.getSpeed() * Math.pow(-1, (r.nextInt() % 2)) * 5 * speedCoeff;
					} while (x <= 4 + firstController.getRadius() || x >= panel.winWid - 4 - firstController.getRadius() || y <= 4 + firstController.getRadius() || y >= panel.winHei / 4 - 4 - firstController.getRadius());
				}

				firstController.setX(x);
				firstController.setY(y);
			} else if (level == 2) {
				if (ball.getY() < panel.winHei / 2) {
					if (firstController.getX() <= panel.winWid * 0.8 && firstController.getX() >= panel.winWid * 0.2 + firstController.getSpeed() * speedCoeff) {
						x += firstController.getSpeed() * Math.signum((ball.getX() - firstController.getX())) * speedCoeff;
					}
					if (x >= panel.winWid * 0.8 - firstController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.79 - firstController.getSpeed() * speedCoeff);
					} else if (x <= panel.winWid * 0.2 + firstController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.21 + firstController.getSpeed() * speedCoeff);
					}
					int c = ball.getSpeed() / 3;
					c = (c > 0) ? c : 1;
					if (ball.getY() > firstController.getY() + firstController.getSpeed() && y < panel.winHei / 4 - firstController.getRadius() - firstController.getSpeed() * (speedCoeff) * c && ball.getY() < panel.winHei / 16 * 5) {
						y += firstController.getSpeed() * (speedCoeff) * c;
					} else if (ball.getY() <= firstController.getY() + firstController.getSpeed() && y > firstController.getRadius() + firstController.getSpeed() * (speedCoeff)) {
						y -= firstController.getSpeed() * speedCoeff;
					}

				} else if (ball.getY() > panel.winHei / 16 * 5) {
					if (firstController.getX() > panel.winWid / 2 + firstController.getSpeed() * speedCoeff) {
						x -= firstController.getSpeed() * speedCoeff;
					} else if (firstController.getX() < panel.winWid / 2 - firstController.getSpeed() * speedCoeff) {
						x += firstController.getSpeed() * speedCoeff;
					}
					if (firstController.getY() > firstController.getRadius() + firstController.getSpeed() * speedCoeff + 5) {
						y -= firstController.getSpeed() * speedCoeff;
					}
				}
				firstController.setX(x);
				firstController.setY(y);
			} else if (level == 1) {
				if (ball.getY() < panel.winHei / 2) {
					if (firstController.getX() < panel.winWid * 0.8 - firstController.getSpeed() * speedCoeff && firstController.getX() > panel.winWid * 0.2 + firstController.getSpeed() * speedCoeff) {
						x += firstController.getSpeed() * Math.signum((ball.getX() - firstController.getX())) * speedCoeff;
					}
					if (x >= panel.winWid * 0.8 - firstController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.79 - firstController.getSpeed() * speedCoeff);
					} else if (x <= panel.winWid * 0.2 + firstController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.21 + firstController.getSpeed() * speedCoeff);
					}
					int c = ball.getSpeed() / 3;
					c = (c > 0) ? c : 1;
					if (ball.getY() > firstController.getY() + firstController.getSpeed() && y < panel.winHei / 4 - firstController.getRadius() - firstController.getSpeed() * (speedCoeff) * c && ball.getY() < panel.winHei / 2) {
						y += firstController.getSpeed() * (speedCoeff) * c;
					} else if (ball.getY() <= firstController.getY() + firstController.getSpeed() && y > firstController.getRadius() + firstController.getSpeed() * (speedCoeff)) {
						y -= firstController.getSpeed() * speedCoeff;
					}

				} else if (!firstController.isPlayer() && ball.getY() > panel.winHei / 2) {
					if (firstController.getX() > panel.winWid / 2 + firstController.getSpeed() * speedCoeff) {
						x -= firstController.getSpeed() * speedCoeff;
					} else if (firstController.getX() < panel.winWid / 2 - firstController.getSpeed() * speedCoeff) {
						x += firstController.getSpeed() * speedCoeff;
					}
					if (firstController.getY() > firstController.getRadius() + firstController.getSpeed() * speedCoeff + 5) {
						y -= firstController.getSpeed() * speedCoeff;
					}
				}
				firstController.setX(x);
				firstController.setY(y);
			}

			//set the controller angle
			firstController.setAngle(0);
			firstController.setMoved(true);
			if (y > prevY) {
				if (x > prevX) {
					firstController.setAngle(7 * PI / 4);
				} else if (x < prevX) {
					firstController.setAngle(5 * PI / 4);
				} else {
					firstController.setAngle(3 * PI / 2);
				}
			} else if (y < prevY) {
				if (x > prevX) {
					firstController.setAngle(PI / 4);
				} else if (x < prevX) {
					firstController.setAngle(3 * PI / 4);
				} else {
					firstController.setAngle(PI / 2);
				}
			} else {
				if (x > prevX) {
					firstController.setAngle(0);
				} else if (x < prevX) {
					firstController.setAngle(PI);
				} else {
					firstController.setMoved(false);
				}
			}
		}

		
		
		/************************************SECOND CONTROLLER*****************************/
		if (!secondController.isPlayer()) {
			int x = secondController.getX();
			int y = secondController.getY();
			int prevX = x;
			int prevY = y;
			int level = secondController.getLevel();
			if (level == 0) {
				Random r = new Random();
				if (counter % 12 == 0) {
					do {
						x = secondController.getX();
						y = secondController.getY();
						x += secondController.getSpeed() * Math.pow(-1, (r.nextInt() % 2)) * 5 * speedCoeff;
						y -= secondController.getSpeed() * Math.pow(-1, (r.nextInt() % 2)) * 5 * speedCoeff;
					} while (x < 5 + secondController.getRadius() || x > panel.winWid - 5 - secondController.getRadius() || y > panel.winHei - 5 - secondController.getRadius() || y < panel.winHei * 3 / 4 + 5 + secondController.getRadius());
				}

				secondController.setX(x);
				secondController.setY(y);
			} else if (level == 2) {
				if (ball.getY() > panel.winHei / 2) {
					if (secondController.getX() <= panel.winWid * 0.8 - secondController.getSpeed() * speedCoeff && secondController.getX() >= panel.winWid * 0.2 + secondController.getSpeed() * speedCoeff) {
						x += secondController.getSpeed() * Math.signum((ball.getX() - secondController.getX())) * speedCoeff;
					}
					if (x >= panel.winWid * 0.8 - secondController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.79 - secondController.getSpeed() * speedCoeff);
					} else if (x <= panel.winWid * 0.2 + secondController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.21 + secondController.getSpeed() * speedCoeff);
					}
					int c = ball.getSpeed() / 3;
					c = (c > 0) ? c : 1;
					if (ball.getY() < secondController.getY() - secondController.getSpeed() && y > panel.winHei * 3 / 4 + secondController.getRadius() + secondController.getSpeed() * (speedCoeff) * c && ball.getY() < panel.winHei / 16 * 11) {
						y -= secondController.getSpeed() * (speedCoeff) * c;
					} else if (ball.getY() >= secondController.getY() - secondController.getSpeed() && y < panel.winHei - secondController.getRadius() - secondController.getSpeed() * (speedCoeff)) {
						y += secondController.getSpeed() * speedCoeff;
					}

				} else if (ball.getY() < panel.winHei / 16 * 11) {
					if (secondController.getX() > panel.winWid / 2 + secondController.getSpeed() * speedCoeff) {
						x -= secondController.getSpeed() * speedCoeff;
					} else if (secondController.getX() < panel.winWid / 2 - secondController.getSpeed() * speedCoeff) {
						x += secondController.getSpeed() * speedCoeff;
					}
					if (secondController.getY() < panel.winHei - secondController.getRadius() - secondController.getSpeed() * speedCoeff - 5) {
						y += secondController.getSpeed() * speedCoeff;
					}
				}
				secondController.setX(x);
				secondController.setY(y);
			} else if (level == 1) {
				if (ball.getY() > panel.winHei / 2) {
					if (secondController.getX() <= panel.winWid * 0.8 - secondController.getSpeed() * speedCoeff && secondController.getX() >= panel.winWid * 0.2 + secondController.getSpeed() * speedCoeff) {
						x += secondController.getSpeed() * Math.signum((ball.getX() - secondController.getX())) * speedCoeff;
					}
					if (x >= panel.winWid * 0.8 - secondController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.79 - secondController.getSpeed() * speedCoeff);
					} else if (x <= panel.winWid * 0.2 + secondController.getSpeed() * speedCoeff) {
						x = (int) (panel.winWid * 0.21 + secondController.getSpeed() * speedCoeff);
					}
					int c = ball.getSpeed() / 3;
					c = (c > 0) ? c : 1;
					if (ball.getY() < secondController.getY() - secondController.getSpeed() && y > panel.winHei * 3 / 4 + secondController.getRadius() + secondController.getSpeed() * (speedCoeff) * c && ball.getY() > panel.winHei / 2) {
						y -= secondController.getSpeed() * (speedCoeff) * c;
					} else if (ball.getY() >= secondController.getY() - secondController.getSpeed() && y < panel.winHei - secondController.getRadius() - secondController.getSpeed() * (speedCoeff)) {
						y += secondController.getSpeed() * speedCoeff;
					}

				} else if (ball.getY() < panel.winHei / 2) {
					if (secondController.getX() > panel.winWid / 2 + secondController.getSpeed() * speedCoeff) {
						x -= secondController.getSpeed() * speedCoeff;
					} else if (secondController.getX() < panel.winWid / 2 - secondController.getSpeed() * speedCoeff) {
						x += secondController.getSpeed() * speedCoeff;
					}
					if (secondController.getY() < panel.winHei - secondController.getRadius() - secondController.getSpeed() * speedCoeff - 5) {
						y += secondController.getSpeed() * speedCoeff;
					}
				}
				secondController.setX(x);
				secondController.setY(y);
			}

			//set the controller angle
			secondController.setAngle(0);
			secondController.setMoved(true);
			if (y > prevY) {
				if (x > prevX) {
					secondController.setAngle(7 * PI / 4);
				} else if (x < prevX) {
					secondController.setAngle(5 * PI / 4);
				} else {
					secondController.setAngle(3 * PI / 2);
				}
			} else if (y < prevY) {
				if (x > prevX) {
					secondController.setAngle(PI / 4);
				} else if (x < prevX) {
					secondController.setAngle(3 * PI / 4);
				} else {
					secondController.setAngle(PI / 2);
				}
			} else {
				if (x > prevX) {
					secondController.setAngle(0);
				} else if (x < prevX) {
					secondController.setAngle(PI);
				} else {
					secondController.setMoved(false);
				}
			}
			ClashVerifyer.clashedController(frame);
			panel.repaint();
		}

	}

	public void restart() {
		timer.stop();
		mainTimer.stop();
		accumulatedSpeed = 0;
		ball = new Ball(-100, -100, parent.settings.getBall().getRadius(), parent.settings.getBall().getSpeed(), PI / 2);
		firstController = new Controller(-1, -1, parent.settings.getFirstHero().getRadius(), parent.settings.getFirstHero().getSpeed(), 0, (gameType < 2) ? true : false, parent.settings.getFirstHero().getLevel(), parent.settings.getFirstHero().getStrength());
		secondController = new Controller(-1, -1, parent.settings.getSecondHero().getRadius(), parent.settings.getSecondHero().getSpeed(), 0, (gameType == 0 || gameType == 2) ? true : false, parent.settings.getSecondHero().getLevel(), parent.settings.getSecondHero().getStrength());
		score = new Point(0, 0);
		releaseAll();
		gameStopped = true;
		label.setText("<html><font color='red' size=10>СЧЕТ: " + score.x + " - " + score.y + "</font></html>");
	}

	public void releaseAll() {
		isUpPressed = false;
		isDownPressed = false;
		isLeftPressed = false;
		isRightPressed = false;
		isWPressed = false;
		isSPressed = false;
		isAPressed = false;
		isDPressed = false;
		firstKeysPressed = 0;
		secondKeysPressed = 0;
	}
}

class MyKeyListener implements KeyListener {

	GameProcess p;

	public MyKeyListener(GameProcess h) {
		p = h;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			p.isLeftPressed = true;
		}

		if (key == KeyEvent.VK_RIGHT) {
			p.isRightPressed = true;
		}

		if (key == KeyEvent.VK_UP) {
			p.isUpPressed = true;
		}

		if (key == KeyEvent.VK_DOWN) {
			p.isDownPressed = true;
		}
		if (p.isLeftPressed && p.isRightPressed && p.isUpPressed && p.isDownPressed) {
			p.firstController.setMoved(false);
		}
		if (key == KeyEvent.VK_A) {
			p.isAPressed = true;
		}

		if (key == KeyEvent.VK_D) {
			p.isDPressed = true;
		}

		if (key == KeyEvent.VK_W) {
			p.isWPressed = true;
		}

		if (key == KeyEvent.VK_S) {
			p.isSPressed = true;
		}
		if (p.isAPressed && p.isDPressed && p.isWPressed && p.isSPressed) {
			p.secondController.setMoved(false);
		}
		ClashVerifyer.clashedController(p);
		p.panel.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			p.isLeftPressed = false;
		}
		if (key == KeyEvent.VK_RIGHT) {
			p.isRightPressed = false;
		}
		if (key == KeyEvent.VK_UP) {
			p.isUpPressed = false;
		}
		if (key == KeyEvent.VK_DOWN) {
			p.isDownPressed = false;
		}
		if (!p.isLeftPressed && !p.isRightPressed && !p.isUpPressed && !p.isDownPressed) {
			p.firstController.setMoved(false);
		}

		if (key == KeyEvent.VK_A) {
			p.isAPressed = false;
		}
		if (key == KeyEvent.VK_D) {
			p.isDPressed = false;
		}
		if (key == KeyEvent.VK_W) {
			p.isWPressed = false;
		}
		if (key == KeyEvent.VK_S) {
			p.isSPressed = false;
		}
		if (!p.isAPressed && !p.isDPressed && !p.isWPressed && !p.isSPressed) {
			p.secondController.setMoved(false);
		}
		if (key == KeyEvent.VK_SPACE) {
			if (!p.mainTimer.isRunning()) {
				p.timer.start();
			}
		}
		if (key == KeyEvent.VK_F5) {
			if (p.ball.getY() < p.panel.winHei / 2) {
				p.mainTimer.stop();
				p.ball.setSpeed(p.ball.getInitialSpeed() / 3);
				p.gameStopped = true;
				p.ball.setX(p.panel.winWid / 2);
				p.ball.setY(p.panel.winHei / 4);
				p.firstController.setX(p.panel.winWid / 2);
				p.firstController.setY(5 + p.firstController.getRadius());
				p.secondController.setX(p.panel.winWid / 2);
				p.secondController.setY(p.panel.winHei - p.secondController.getRadius() - 5);
				p.panel.repaint();
			} else {
				p.mainTimer.stop();
				p.ball.setSpeed(p.ball.getInitialSpeed() / 3);
				p.gameStopped = true;
				p.ball.setX(p.panel.winWid / 2);
				p.ball.setY(p.panel.winHei / 4 * 3);
				p.firstController.setX(p.panel.winWid / 2);
				p.firstController.setY(5 + p.firstController.getRadius());
				p.secondController.setX(p.panel.winWid / 2);
				p.secondController.setY(p.panel.winHei - p.secondController.getRadius() - 5);
				p.panel.repaint();
			}
		}
		if (key == KeyEvent.VK_ESCAPE) {
			if (p.mainTimer.isRunning() && p.controllerTimer.isRunning() && !p.gameStopped) {
				p.mainTimer.stop();
				p.controllerTimer.stop();
				p.panel.repaint();
			} else if (!p.gameStopped) {
				p.mainTimer.start();
				p.controllerTimer.start();
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
};
