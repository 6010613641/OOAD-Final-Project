package towers;

import java.util.ArrayList;
import java.util.Iterator;

import misc.Bullet;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

import states.Game;
import entities.Monster;

/**
 * Shooting Tower Type
 */
public abstract class ShootingTower extends Tower {
	protected Image projectile;
	
	protected int[] damage, shootInterval;
	protected int lastShot;
	protected int angle;
	
	protected Monster target;
	protected ArrayList<Bullet> bullets;
	
	protected int killCount;
	
	/**
	 * Build Shooting Tower
	 * @param position 
	 * @param damage 
	 * @param shootInterval
	 * @param range
	 * @param cost
	 * @param textures
	 * @param projectile
	 * @param game
	 */
	protected ShootingTower(Point position, int[] damage, int[] shootInterval,int[] range, int[] cost, Image[] textures, Image projectile, Game game) {
		super(position, range, cost, textures, game);
		this.projectile = projectile;
		this.damage = damage;
		this.shootInterval = shootInterval;		
		this.bullets = new ArrayList<Bullet>();
	}
	
	/**
	 * find lowest enemies hp
	 * @return lowest enemy in range
	 */
	public Monster lookForTarget() {
		ArrayList<Monster> entitiesInRange = getMonstersInRange();
		/* Enemy in Range */
		if(!entitiesInRange.isEmpty()) {	
			
			/* No finding */
			int index = -1;
			
			/* find lowest enemy hp */
			for(int i = 0; i < entitiesInRange.size();i++) {
				if(canTarget(entitiesInRange.get(i))) {
					
					/* update enemy hp */
					if(index == -1 || entitiesInRange.get(i).getHealth() < entitiesInRange.get(index).getHealth()) {
						index = i;
					}
				}
			}
			
			/* if find enemy const null */
			if(index != -1) {
				return entitiesInRange.get(index);
			}
		}
		return null;
	}
	
	public Monster getTarget() {
		return target;
	}
	
	/**
	 * Clear enemy target
	 */
	public void clearTarget() {
		target = null;
		for(Bullet b : bullets) {
			b.setTarget(null);
		}
	}
	
	public int getDamage() {
		return damage[upgradeLevel];
	}
	
	public int getShootInterval() {
		return shootInterval[upgradeLevel];
	}

	public int getKillCount() {
		return killCount;
	}

	/**
	 * Update Tower
	 * Update Shop
	 * find enemies in range and attack
	 */
	@Override
	public void update(GameContainer gc, int delta) {
		/* time last shot */
		lastShot += delta;
		
		/* Tower target */
		if(target != null) {
			
			/* lock target enemy */
			angle = (int)new Vector2f(target.getPosition().getX()-position.getX(),target.getPosition().getY() - position.getY()).getTheta();
			
			/* Interval */
			if(lastShot >= shootInterval[upgradeLevel]) {
				bullets.add(new Bullet(new Vector2f(position.getX(),position.getY()),new Vector2f(angle), target,projectile.copy()));
				lastShot = 0;
			}
			
			/* Enemies out of range */
			double distanceToTarget = Math.sqrt(Math.pow(Math.abs(target.getPosition().getX()-position.getX()), 
					2)+Math.pow(Math.abs(target.getPosition().getY()-position.getY()), 2));
			if(distanceToTarget > getRange()) {
				target = null;
			}
		} else {
			target = lookForTarget();
		}
		
		/* update bullet to enemies */
		Iterator<Bullet> iterator = bullets.iterator();
		while(iterator.hasNext()){
           	Bullet b = iterator.next();
           	
            b.update(gc, delta);
            
            if(b.getLifeTime() > 10000) {
				iterator.remove();
			}
            
            /* Bullet hit enemies */
            if(b.hitsTarget()) {
            	
            	/* Damage */
            	Monster t = b.getTarget();
            	t.setHealth(t.getHealth()-damage[upgradeLevel]);
            	
            	/* Enemies dead */
            	if(t.getHealth() <= 0) {
            		game.removeMonster(t);
            		game.setGold(game.getGold()+game.getMap().getKillMoney());
            		killCount++;
            	}
            	
            	iterator.remove();
            }
        }
	}
	
	/**
	 * Spin Tower
	 */
	@Override
	public void render(GameContainer gc, Graphics g) {
		Image tower = this.textures[upgradeLevel];
		tower.setRotation(angle);
		tower.drawCentered(position.getX(), position.getY());

		for(Bullet b : bullets) {
			b.render(gc, g);
		}
	}
}


