package entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

import states.Game;

/**
 * Unit Enemies
 * 
 */
public class Monster implements Mover {
	private Game game;
	
	private Vector2f position;
	private int angle;
	private int tileposx, tileposy;
	
	private int health, maxhp, radius;
	private float speed;
	private boolean isGhost, isBoss;
	private float slowValue;
	private Image texture;
	
	private AStarPathFinder pathfinder;
	private Point targetPoint;
	private float rotation;
	
	/**
	 * Build Enemies Armies
	 * @param Game reference object
	 * @param Start position 
	 * @param Player HP
	 * @param Movement speed
	 * @param Enemies type
	 * @param Boss wave
	 * @param Map
	 */
	public Monster(Game game, Vector2f position, int hp, float speed, boolean isGhost, boolean isBoss, Image texture) {
		this.game = game;
		
		this.position = position;
		this.tileposx = (int)Math.floor((position.x)/48);
		this.tileposy = (int)Math.floor((position.y)/48);
		
		this.health = hp;
		this.maxhp = hp;
		this.radius = 20;
		this.speed = speed;
		this.slowValue = 1;
		this.isGhost = isGhost;
		this.isBoss = isBoss;
		this.texture = texture;
		
		if(!isBoss) {
			texture.setCenterOfRotation(24, 24);
			radius = 10;
		}

		this.targetPoint = game.getMap().getBase();		
		this.pathfinder = new AStarPathFinder(game.getMap(), 200, false);
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMaxhp() {
		return maxhp;
	}
	
	public int getRadius() {
		return radius;
	}

	public Vector2f getPosition() {
		return position;
	}

	public boolean isGhost() {
		return isGhost;
	}

	public boolean isBoss() {
		return isBoss;
	}

	
	public void setSlowValue(float slowValue) {
		if(slowValue < this.slowValue)
			this.slowValue = slowValue;
	}
	
	/**
	 * @param Game window
	 */
	public void update(GameContainer gc, int delta) {	

		/* Check map for enemies movement */
		if(Math.abs(tileposx*48+24-position.x) >= 48) {
			tileposx = (int)Math.floor((position.x)/48);
		} else if(Math.abs(tileposy*48+24-position.y) >= 48) {
			tileposy = (int)Math.floor((position.y)/48);
		}
		
		//rotation = (delta/1000.0f)*200*slowValue;
		//texture.rotate(rotation);
		
		/* Path finder */
		Path p = pathfinder.findPath(this, tileposx, tileposy , (int)targetPoint.getX(), (int)targetPoint.getY());
		if(p != null) {
			/* enemies move */
			if(p.getLength() > 0) {
				/* path movement */
				Step step = p.getStep(1);
				Vector2f nextPoint = new Vector2f(step.getX() - tileposx, step.getY() - tileposy);			
				nextPoint = nextPoint.scale((delta/1000.f)*48*speed*slowValue);
				position.add(nextPoint);
				
				/* finish movement */
				slowValue = 1;
			}
		}
		/* Enemies damage to castle */
		else {
			Point base = game.getMap().getBase();
			if(base.getX() == tileposx && base.getY() == tileposy) {
				/* destroy enemies and decrease player hp */
				game.removeMonster(this);
				game.setHealth(game.getHealth() - ((isBoss)? 5 : 1));
			}
		}
	}
	

	public void render(GameContainer gc, Graphics g) {
		if(isBoss) {
			texture.drawCentered(position.x, position.y);
		} else {
			texture.draw(position.x-24, position.y-24, 48, 48);
		}
		
		/* show hp */
		g.setColor(Color.red);
		g.fillRect(position.x-18, position.y-24, 36, 2);
		
		g.setColor(Color.green);
		float width = 36*((float)health/(float)maxhp);
		if(width > 0) {	
			g.fillRect(position.x-18, position.y-24, (int)width, 2);	
		}
	}
}



