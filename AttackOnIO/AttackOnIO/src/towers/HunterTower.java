package towers;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import entities.Monster;
import main.ImageManager;
import states.Game;

public class HunterTower extends ShootingTower {
	/**
	 * Build Hunter Tower
	 * @param Position
	 */
	private final static int[] damage = {8,9,10};
	private final static int[] atkSpeed = {800,650,500};
	private final static int[] range = {150,200,250};
	private final static int[] cost = {150,200,300};
	
	public HunterTower(Point position, Game game) {
		super(position,
				damage,
				atkSpeed,
				range,
				cost,
				new Image[]{
					ImageManager.getImage(ImageManager.HUNTER_TOWER_1),
					ImageManager.getImage(ImageManager.HUNTER_TOWER_2),
					ImageManager.getImage(ImageManager.HUNTER_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_BLUE), game);
	}

	@Override
	public boolean canTarget(Monster entity) {
		return true;
	}
}
