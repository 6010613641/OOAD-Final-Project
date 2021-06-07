package towers;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import entities.Monster;
import states.Game;

/**
 * Tower
 */
public abstract class Tower {
	protected Game game;
	
	protected Point position;
	
	protected int upgradeLevel;
	protected int[] range, cost;
	protected Image[] textures;
	
	/**
	 * Build Tower
	 * @param position
	 * @param range
	 * @param cost
	 * @param textures
	 * @param game
	 */
	protected Tower(Point position, int[] range, int[] cost, Image[] textures, Game game) {
		this.position = position;
		this.range = range;
		this.cost = cost;
		this.textures = textures;
		this.game = game;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public Point getTilePosition() {
		return new Point(
			(int)Math.floor(position.getX()/48),
			(int)Math.floor(position.getY()/48)
		);
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getUpgradeLevel() {
		return upgradeLevel;
	}
	
	public void setUpgradeLevel(int upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}

	public int getRange() {
		return range[upgradeLevel];
	}

	public int getCost() {
		return cost[upgradeLevel];
	}
	
	public int getCost(int i) {
		return cost[i];
	}
	
	public int getUpgradeCost() {
		return (upgradeLevel < 2)? cost[upgradeLevel+1] : 0;
	}
	
	/**
	 * Detect Enemies in range tower
	 * @return enemies in range
	 */
	public ArrayList<Monster> getMonstersInRange() {
		ArrayList<Monster> monsterList = game.getMonsterList();
		ArrayList<Monster> monsterInRange = new ArrayList<Monster>();
		for(Monster entity: monsterList) {
			// Position
			Vector2f entityPos = entity.getPosition();
			
			// Distance from tower to enemy
			double distance = Math.sqrt(Math.pow(Math.abs(entityPos.x-position.getX()), 2)+Math.pow(Math.abs(entityPos.y-position.getY()), 2));
			if(distance <= getRange()) {
				monsterInRange.add(entity);
			}
		}
		return monsterInRange;
	}

	/**
	 * Lock target
	 * @param check enemies type
	 * @return tower can attack enemies or not
	 */
	public abstract boolean canTarget(Monster entity);
	
	/**
	 * Update status
	 * @param Game Window
	 */
	public abstract void update(GameContainer gc, int delta);
	
	/**
	 * Render Game
	 * @param Game Window
	 */
	public abstract void render(GameContainer gc, Graphics g);
}




