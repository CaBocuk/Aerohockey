package settings;

public class Settings {
	public int getGameType() {
		return gameType;
	}
	public void setGameType(int gameType) {
		this.gameType = gameType;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}
	public Hero getFirstHero() {
		return firstHero;
	}
	public void setFirstHero(Hero firstHero) {
		this.firstHero = firstHero;
	}
	public Hero getSecondHero() {
		return secondHero;
	}
	public void setSecondHero(Hero secondHero) {
		this.secondHero = secondHero;
	}
	public Ball getBall() {
		return ball;
	}
	public void setBall(Ball ball) {
		this.ball = ball;
	}
	int gameType;
	Field field;
	Hero firstHero;
	Hero secondHero;
	Ball ball;
}