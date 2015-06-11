/**
 * Execution of the game and all manipulations with pre-game menu are held here.
 */

package graphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import settings.Ball;
import settings.Field;
import settings.Hero;
import settings.Settings;

public class StartWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	Settings settings = new Settings();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow frame = new StartWindow();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public StartWindow() {
		setTitle("Aerohockey");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		setBackground(Color.PINK);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		MenuPanel panel = new MenuPanel(this);
		panel.addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				if (panel.isMenu) {
					panel.setButtonFocus(e.getX(), e.getY());
				} else if (panel.isHeroChooser) {
					panel.setHeroFocuses(e.getX(), e.getY());
				} else if (panel.isLastSettings) {
					panel.setLastFocuses(e.getX(), e.getY());
				}
				repaint();
			}

		});
		panel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (panel.isMenu && panel.isMenuButtonInFocus && e.getButton() == MouseEvent.BUTTON1) { //go to the next page
					panel.isMenu = false;
					panel.isMenuButtonInFocus = false;
					panel.isHeroChooser = true;
				} else if (panel.isHeroChooser && e.getButton() == MouseEvent.BUTTON1) {
					panel.setHeroChooses(e.getX(), e.getY());
				} else if (panel.isLastSettings && e.getButton() == MouseEvent.BUTTON1) {
					panel.setLastSelection(e.getX(), e.getY());
				}
				repaint();
			}
		});
		contentPane.add(panel, BorderLayout.CENTER);
	}
}

class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	StartWindow p;

	// Start window - menu
	boolean isMenu = true; //if menu page should be displayed
	boolean isMenuButtonInFocus = false;
	int menuButWid = 100;
	int menuButHei = 50;

	// Second window - type & level (hero) choosers
	boolean isHeroChooser = false; //if gametype and hero chooser page should be displayed

	int typeFocusIndex = -1; //index of focused type
	int typeSelectedIndex = -1; //index of selected type
	String typeTexts[] = { "PvP", "PvC", "CvP", "CvC" };

	Hero heroes[] = new Hero[4]; // different heroes

	int firstHeroIndex = -1;
	int secondHeroIndex = -1;
	int heroFocusIndex = -1;
	String heroTexts[] = { "Копатыч", "Бараш", "Лосяш", "Кроль" };

	String firstText = "Выберите первого персонажа";
	String secondText = "Теперь выберите второго персонажа";
	String thirdText = "Персонажи выбраны. Начинайте игру";
	String typeText = "Выберите тип игры";

	int buttonFocusIndex = -1;
	String buttonTexts[] = { "НАЗАД", "ДАЛЕЕ", "СБРОС" };

	// Third window - goal width and ball speed
	boolean isLastSettings = false; //if field and goal selector page should be displayed

	int fieldSelectedIndex = -1;
	int ballSelectedIndex = -1;
	int fieldFocusedIndex = -1;
	int ballFocusedIndex = -1;

	Ball balls[] = new Ball[3];
	Field fields[] = new Field[3];

	// GENERAL
	int wid = -1;
	int hei = -1;

	public MenuPanel(StartWindow p) {
		this.p = p;
		try {
			balls[0] = new Ball(20, 2, ImageIO.read(getClass().getResource("/images/balls/football.png")), "Размер: средний, Скорость: средняя");
			balls[1] = new Ball(25, 1, ImageIO.read(getClass().getResource("/images/balls/basketball.png")), "Размер: большой, Скорость: низкая");
			balls[2] = new Ball(15, 4, ImageIO.read(getClass().getResource("/images/balls/hockey.png")), "Размер: маленький, Скорость: большая");
			fields[0] = new Field(0.25, 0.0025, ImageIO.read(getClass().getResource("/images/fields/parket.jpg")), "Ворота: средние, Трение: среднее");
			fields[1] = new Field(0.3, 0.005, ImageIO.read(getClass().getResource("/images/fields/ice.jpg")), "Ворота: маленькие, Трение: низкое");
			fields[2] = new Field(0.2, 0.01, ImageIO.read(getClass().getResource("/images/fields/grass.jpg")), "Ворота: широкие, Трение: высокое");
			heroes[0] = new Hero(0, 7, 40, 4.5, ImageIO.read(getClass().getResource("/images/heroes/fat_hero.png")), "Медленный, но большой и сильный");
			heroes[1] = new Hero(1, 10, 35, 2.5, ImageIO.read(getClass().getResource("/images/heroes/barash_hero.png")), "Все среднее: и скорость, и сила, и размер");
			heroes[2] = new Hero(1, 15, 30, 1.4, ImageIO.read(getClass().getResource("/images/heroes/losyash_hero.png")), "Быстрый, но маленький и слабенький");
			heroes[3] = new Hero(2, 20, 25, 0.8, ImageIO.read(getClass().getResource("/images/heroes/krol_hero.png")), "Очень быстрый, но очень маленький и слабый");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Ошибка в поиске изображений для игры.");
		}
	}

	public void paint(Graphics g) {
		g.clearRect(0, 0, wid, hei);
		if (wid != -1) {
			if (isMenu) {
				drawMenu(g);
			} else if (isHeroChooser) {
				drawHeroChooser(g);
			} else if (isLastSettings) {
				drawLast(g);
			}
		} else { //if wid and hei are not initialized
			wid = getWidth();
			hei = getHeight();
		}
	}

	/************************** MENU **************************/
	private void drawMenu(Graphics g) {
		g.setColor(Color.GRAY);
		g.drawImage(fields[1].getImg(), 0, 0, wid, hei,0,0,fields[1].getImg().getWidth(null),fields[1].getImg().getHeight(null),null);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(4));

		if (isMenuButtonInFocus) {
			g.setColor(Color.GREEN);
		} else {
			g.setColor(Color.CYAN);
		}

		g.fillRoundRect(wid / 2 - menuButWid, hei / 2 - menuButHei, menuButWid * 2, menuButHei * 2, 25, 25);
		g.setFont(new Font("Comic Sans MS", 20, 20));
		g.setColor(Color.BLACK);
		g.drawString("НОВАЯ ИГРА", wid / 2 - 60, hei / 2 + 5);
	}

	public void setButtonFocus(int x, int y) {
		if (x > wid / 2 - menuButWid && x < wid / 2 + menuButWid && y > hei / 2 - menuButHei && y < hei / 2 + menuButHei) {
			isMenuButtonInFocus = true;
		} else {
			isMenuButtonInFocus = false;
		}
	}

	/***************************** SECOND WINDOW - HERO CHOOSER **********************************/

	private void drawHeroChooser(Graphics g) {

		//fill the background
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, wid, hei);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));

		int radius = wid / 10; //radius of the circles to draw

		//draw the text and separator
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", 20, 20));
		g.drawString(typeText, wid / 2 - 100, 25);
		g.drawLine(0, (int) (radius * 4), wid, (int) (radius * 4));
		if (firstHeroIndex == -1) {
			g.drawString(firstText, wid / 2 - 140, (int) (radius * 4.5));
		} else if (secondHeroIndex == -1 && firstHeroIndex != -1) {
			g.drawString(secondText, wid / 2 - 170, (int) (radius * 4.5));
		} else {
			g.drawString(thirdText, wid / 2 - 170, (int) (radius * 4.5));
		}

		int x = (int) (radius / 1.5); //the coordinate of first circle
		for (int i = 0; i < 4; i++) {
			//draw type circles
			if (i == typeSelectedIndex) {
				g.setColor(Color.green);
			} else if (i == typeFocusIndex) {
				g.setColor(Color.cyan);
			} else {
				g.setColor(Color.red);
			}
			g.drawOval(x, radius, radius * 2, radius * 2);
			g.setFont(new Font("Comic Sans MS", 30, 30));
			g.drawString(typeTexts[i], x + radius / 2, radius * 2 + 8);

			// draw hero circles
			if (i == heroFocusIndex && secondHeroIndex == -1) {
				g.setColor(Color.white);
				g.setFont(new Font("Comic Sans MS", 13, 13));
				g.drawString(heroes[i].getMsg(), wid / 2 - heroes[i].getMsg().length() * 3, radius * 15 / 2);
				g.setColor(Color.cyan);
			} else if (secondHeroIndex == -1) {
				g.setColor(Color.red);
			} else {
				g.setColor(Color.WHITE);
			}
			g.drawImage(heroes[i].getImg(), x, radius * 5, x + radius * 2, radius * 7, 0, 0, heroes[i].getImg().getWidth(null), heroes[i].getImg().getHeight(null), null);
			g.drawOval(x, radius * 5, radius * 2, radius * 2);

			x += radius * 2 + 10;

		}

		//if a hero is selected - draw his miniature near the buttons
		if (firstHeroIndex != -1) {
			g.setColor(Color.GREEN);
			int smallRadius = (int) (radius / 1.5);
			g.drawOval(5, hei - smallRadius * 2 - 5, smallRadius * 2, smallRadius * 2);
			g.setFont(new Font("Comic Sans MS", 50 / heroTexts[firstHeroIndex].length(), 80 / heroTexts[firstHeroIndex].length()));
			g.drawString(heroTexts[firstHeroIndex], smallRadius / 4 + 5, hei - smallRadius + 30 / heroTexts[firstHeroIndex].length() - 5);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans MS", 18, 18));
			g.drawString("Игрок 1:", 3, hei - 2 * smallRadius - 10);
		}
		if (secondHeroIndex != -1) {
			g.setColor(Color.GREEN);
			int smallRadius = (int) (radius / 1.5);
			g.drawOval(wid - smallRadius * 2 - 5, hei - smallRadius * 2 - 5, smallRadius * 2, smallRadius * 2);
			g.setFont(new Font("Comic Sans MS", 50 / heroTexts[secondHeroIndex].length(), 80 / heroTexts[secondHeroIndex].length()));
			g.drawString(heroTexts[secondHeroIndex], wid - smallRadius * 7 / 4 - 5, hei - smallRadius + 30 / heroTexts[secondHeroIndex].length() - 5);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Comic Sans MS", 18, 18));
			g.drawString("Игрок 2:", wid - 2 * smallRadius - 10 - 2, hei - 2 * smallRadius - 10);
		}

		//draw the buttons
		g.setFont(new Font("Comic Sans MS", 20, 20));
		for (int i = 0; i < 3; i++) {
			if (i == buttonFocusIndex) {
				g.setColor(Color.CYAN);
			} else {
				g.setColor(Color.RED);
			}
			g.drawRoundRect((int) (radius / 1.5) * 2 + 15 + 110 * i, hei - 50, 100, 48, 25, 25);
			g.drawString(buttonTexts[i], (int) (radius / 1.5) * 2 + 30 + 113 * i, hei - 20);
		}

	}

	public void setHeroFocuses(int x, int y) {
		int radius = wid / 10;

		int centerX = (int) (radius / 1.5) + radius;
		int centerY = radius * 2;
		heroFocusIndex = -1;
		typeFocusIndex = -1;
		buttonFocusIndex = -1;

		for (int i = 0; i < 4; i++) {
			centerY = radius * 2;
			if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius && y <= hei / 3) {
				typeFocusIndex = i;
				return;
			}

			centerY = radius * 6;
			if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius && y >= hei / 3) {
				heroFocusIndex = i;
				return;
			}
			centerX += radius * 2 + 10;

			if (x >= (int) (radius / 1.5) * 2 + 15 + 110 * i && y >= hei - 50 && x <= (int) (radius / 1.5) * 2 + 115 + 110 * i && y <= hei - 1) {
				buttonFocusIndex = i;
				return;
			}

		}
	}

	public void setHeroChooses(int x, int y) {
		int radius = wid / 10;

		int centerX = (int) (radius / 1.5) + radius;
		int centerY = radius * 2;

		for (int i = 0; i < 4; i++) {
			//check the type selection
			centerY = radius * 2;
			if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius && y <= hei / 3) {
				typeSelectedIndex = i;
				p.settings.setGameType(i);
				return;
			}

			//check the hero selection
			centerY = radius * 6;
			if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius && y >= hei / 3 && secondHeroIndex == -1) {
				if (firstHeroIndex == -1) {
					p.settings.setFirstHero(heroes[i]);
					firstHeroIndex = i;
				} else {
					p.settings.setSecondHero(heroes[i]);
					secondHeroIndex = i;
				}
				return;
			}

			//check the button selection
			centerX += radius * 2 + 10;
			if (x >= (int) (radius / 1.5) * 2 + 15 + 110 * i && y >= hei - 50 && x <= (int) (radius / 1.5) * 2 + 115 + 110 * i && y <= hei - 1) {
				if (i == 0) { //НАЗАД
					isMenu = true;
					isHeroChooser = false;
					buttonFocusIndex = -1;
				} else if (i == 1) { //ДАЛЕЕ
					if (typeSelectedIndex != -1 && firstHeroIndex != -1 && secondHeroIndex != -1) {
						isHeroChooser = false;
						buttonFocusIndex = -1;
						isLastSettings = true;
					}
				} else if (i == 2) { //СБРОС
					firstHeroIndex = -1;
					secondHeroIndex = -1;
				}
				return;
			}
		}

	}

	/***************************** THIRD WINDOW - LAST SETTINGS ***********************************/

	private void drawLast(Graphics g) {
		//fill the background
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, wid, hei);

		//draw the text and separator
		g.setColor(Color.WHITE);
		g.setFont(new Font("Comic Sans MS", 20, 20));
		g.drawString("Выберите поле", wid / 2 - 70, 25);
		g.drawString("Выберите снаряд", wid / 2 - 75, 240);
		g.drawLine(0, 200, wid, 200);

		
		int x = wid / 10; // the coordinates of the circles
		for (int i = 0; i < 3; i++) {
			//draw fields
			if (i == fieldSelectedIndex) {
				g.setColor(Color.GREEN);
			} else if (i == fieldFocusedIndex) {
				g.setColor(Color.white);
				g.setFont(new Font("Comic Sans MS", 13, 13));
				g.drawString(fields[i].getMsg(), wid / 2 - fields[i].getMsg().length() * 3, 190);
				g.setColor(Color.CYAN);
			} else {
				g.setColor(Color.RED);
			}
			g.drawImage(fields[i].getImg(), (1 + 3 * i) * x, 50, (3 + 3 * i) * x, 175, 0, 0, fields[i].getImg().getWidth(null), fields[i].getImg().getHeight(null), null);
			((Graphics2D) g).setStroke(new BasicStroke(3));
			g.drawRect((1 + 3 * i) * x, 50, 2 * x, 125);
			
			
			//draw balls
			if (i == ballSelectedIndex) {
				g.setColor(Color.GREEN);
			} else if (i == ballFocusedIndex) {
				g.setColor(Color.white);
				g.setFont(new Font("Comic Sans MS", 13, 13));
				g.drawString(balls[i].getMsg(), wid / 2 - balls[i].getMsg().length() * 3, 280 + 2 * x);
				g.setColor(Color.CYAN);
			} else {
				g.setColor(Color.RED);
			}
			g.drawImage(balls[i].getImg(), (1 + 3 * i) * x, 260, (3 + 3 * i) * x, 260 + 2 * x, 0, 0, balls[i].getImg().getWidth(null), balls[i].getImg().getHeight(null), null);
			g.drawOval((1 + 3 * i) * x, 260, x * 2, x * 2);
		}

		
		//draw buttons
		((Graphics2D) g).setStroke(new BasicStroke(1));
		int radius = wid / 10;
		g.setFont(new Font("Comic Sans MS", 20, 20));
		for (int i = 0; i < 3; i++) {
			if (i == buttonFocusIndex) {
				g.setColor(Color.CYAN);
			} else {
				g.setColor(Color.RED);
			}
			g.drawRoundRect((int) (radius / 1.5) * 2 + 15 + 110 * i, hei - 50, 100, 48, 25, 25);
			g.drawString(buttonTexts[i], (int) (radius / 1.5) * 2 + 30 + 113 * i, hei - 20);
		}
	}

	public void setLastFocuses(int x, int y) {
		if (y < 200) { //if mouse is in field part
			for (int i = 0; i < 3; i++) {
				if (x >= (1 + 3 * i) * wid / 10 && y >= 50 && x <= (3 + 3 * i) * wid / 10 && y <= 175) {
					fieldFocusedIndex = i;
					return;
				} else {
					fieldFocusedIndex = -1;
				}
			}
		} else { //if mouse is in goal/buttons part
			for (int i = 0; i < 3; i++) {
				int radius = wid / 10;

				int centerX = (2 + 3 * i) * radius;
				int centerY = 260 + radius;
				if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius) {
					ballFocusedIndex = i;
					return;
				} else {
					ballFocusedIndex = -1;
				}

				if (x >= (int) (radius / 1.5) * 2 + 15 + 110 * i && y >= hei - 50 && x <= (int) (radius / 1.5) * 2 + 115 + 110 * i && y <= hei - 1) {
					buttonFocusIndex = i;
					return;
				} else {
					buttonFocusIndex = -1;
				}
			}

		}
	}

	public void setLastSelection(int x, int y) {
		if (y < 200) { //if mouse is in field part
			for (int i = 0; i < 3; i++) {
				if (x >= (1 + 3 * i) * wid / 10 && y >= 50 && x <= (3 + 3 * i) * wid / 10 && y <= 175) {
					fieldSelectedIndex = i;
					p.settings.setField(fields[i]);
					return;
				} else {
					fieldSelectedIndex = -1;
				}
			}
		} else { //if mouse is in goal/buttons part
			for (int i = 0; i < 3; i++) {
				int radius = wid / 10;

				int centerX = (2 + 3 * i) * radius;
				int centerY = 260 + radius;
				if (Math.sqrt((y - centerY) * (y - centerY) + (x - centerX) * (x - centerX)) <= radius) {
					ballSelectedIndex = i;
					p.settings.setBall(balls[i]);
					return;
				}

				if (x >= (int) (radius / 1.5) * 2 + 15 + 110 * i && y >= hei - 50 && x <= (int) (radius / 1.5) * 2 + 115 + 110 * i && y <= hei - 1) {
					if (i == 0) { //НАЗАД
						isHeroChooser = true;
						isLastSettings = false;
						buttonFocusIndex = -1;
					} else if (i == 1) { //ДАЛЕЕ
						if (fieldSelectedIndex != -1 && ballSelectedIndex != -1) {
							isHeroChooser = false;
							isMenu = true;
							isLastSettings = false;
							buttonFocusIndex = -1;
							//Launch the game
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										GameProcess frame = new GameProcess(p);
										frame.setVisible(true);
										p.setVisible(false);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
						}
					} else if (i == 2) {
						ballSelectedIndex = -1;
						fieldSelectedIndex = -1;
					}
					return;
				}
			}

		}
	}
}