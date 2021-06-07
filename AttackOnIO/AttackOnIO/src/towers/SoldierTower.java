package towers;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import entities.Monster;
import main.ImageManager;
import states.Game;

public class SoldierTower extends ShootingTower{
	/**
	 * Build Soldier Tower
	 * @param Position
	 */
	private final static int[] damage = {16,18,24};
	private final static int[] atkSpeed = {1400,1300,1100};
	private final static int[] range = {200,250,300};
	private final static int[] cost = {150,200,300};
	
	public SoldierTower(Point position,Game game) {
		super(position,
				damage,
				atkSpeed,
				range,
				cost,
				new Image[]{
					ImageManager.getImage(ImageManager.SOLDIER_TOWER_1),
					ImageManager.getImage(ImageManager.SOLDIER_TOWER_2),
					ImageManager.getImage(ImageManager.SOLDIER_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_GREEN), game);
	}

	@Override
	public boolean canTarget(Monster entity) {
		if(!entity.isGhost()) {
			return true;
		}
		return false;
	}
}
