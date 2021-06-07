package towers;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import entities.Monster;
import main.ImageManager;
import states.Game;

public class SnowTower extends Tower {
private float[] snowValues;
private final static int[] range = {100,100,100};
private final static int[] cost = {150,200,300};
private final static float[] slow = {0.7f,0.6f,0.5f};
	
	/**
	 * Build Snow Tower
	 * @param Position
	 */
	public SnowTower(Point position, Game game) {
		super(position,
				range,
				cost,
				new Image[]{
					ImageManager.getImage(ImageManager.SNOW_TOWER_1),
					ImageManager.getImage(ImageManager.SNOW_TOWER_2),
					ImageManager.getImage(ImageManager.SNOW_TOWER_3)		
				}, game);
		snowValues = slow;
	}
	
	public float getSlowValue() {
		return snowValues[upgradeLevel];
	}

	/**
	 * Update Slow Towers
	 */
	@Override
	public void update(GameContainer gc, int time) {
		for(Monster entity : this.getMonstersInRange()) {
			if(canTarget(entity))
				entity.setSlowValue(snowValues[upgradeLevel]);
		}
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		Image tower = this.textures[upgradeLevel];
		tower.drawCentered(position.getX(), position.getY());
	}

	@Override
	public boolean canTarget(Monster entity) {
		if(!entity.isGhost()) {
			return true;
		}
		return false;
	}
}
