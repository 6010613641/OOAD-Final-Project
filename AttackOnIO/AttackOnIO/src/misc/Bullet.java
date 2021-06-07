package misc;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

import entities.Monster;

/**
 * show bullet movement
 *
 */
public class Bullet {
	private Vector2f position, direction;
	private Monster target;
	private Image texture;
	private int radius;
	
	int lifeTime;
	
	/**
	 * Reload bullet
	 * @param direction bullet to enemies
	 * @param lock target
	 * @param texture
	 */
	public Bullet(Vector2f position, Vector2f direction, Monster target, Image texture) {
		this.position = position;
		this.direction = direction;
		this.target = target;
		this.texture = texture;
		this.radius = 12;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Monster getTarget() {
		return target;
	}
	
	public void setTarget(Monster target) {
		this.target = target;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	/**
	 * Calculate Projectile bullet
	 * @param movement speed
	 */
	public void update(GameContainer gc, int delta) {
		lifeTime += delta;
		if (target != null)
			direction = new Vector2f(target.getPosition().x - position.x,target.getPosition().y - position.y).normalise();
		position.add(direction.copy().scale((delta/1000f)*500));
	}
	
	/**
	 * Render game
	 */
	public void render(GameContainer gc, Graphics g) {
		texture.setRotation(180 + (float)direction.getTheta());	
		texture.drawCentered(position.x,position.y);
	}

	/**
	 * check target bullet
	 * @return Boolean bullet to target or not
	 */
	public boolean hitsTarget() {
		if(target != null) {
			Vector2f entityPos = target.getPosition();
			double distance = Math.sqrt(Math.pow(Math.abs(entityPos.x-position.getX()), 2)+Math.pow(Math.abs(entityPos.y-position.getY()), 2));
			int targetRadius = target.getRadius();
			if (distance < (targetRadius + radius)) {
				return true;
			}
		}
		return false;
	}
}




