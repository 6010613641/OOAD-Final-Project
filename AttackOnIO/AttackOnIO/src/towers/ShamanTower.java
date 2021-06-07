package towers;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

import entities.Monster;
import main.ImageManager;
import states.Game;

public class ShamanTower extends ShootingTower{
	/**
	 * Build Shaman Tower
	 * @param Position
	 */
	private final static int[] damage = {12,14,20};
	private final static int[] atkSpeed = {600,500,400};
	private final static int[] range = {150,200,250};
	private final static int[] cost = {150,200,300};
	
	public ShamanTower(Point position, Game game) {
		super(position,
				damage,
				atkSpeed,
				range,
				cost,
				new Image[]{
					ImageManager.getImage(ImageManager.SHAMAN_TOWER_1),
					ImageManager.getImage(ImageManager.SHAMAN_TOWER_2),
					ImageManager.getImage(ImageManager.SHAMAN_TOWER_3)		
				},
				ImageManager.getImage(ImageManager.PROJECTILE_CYAN), game);
	}

	@Override
	public boolean canTarget(Monster entity) {
		if(entity.isGhost()) {
			return true;
		}
		return false;
	}
}
