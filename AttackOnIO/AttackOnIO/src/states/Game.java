package states;

import java.util.ArrayList;

import main.ImageManager;
import misc.Map;
import misc.LatoFont;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;

import entities.Monster;
import towers.ShamanTower;
import towers.SoldierTower;
import towers.HunterTower;
import towers.ShootingTower;
import towers.SnowTower;
import towers.Tower;

/**
 * Show Status
 * 
 */
public class Game extends BasicGameState {
	
	/* Graphic */
	Image sidebar_background, info;
	
	/* Buttons */
	MouseOverArea button_upgrade,button_sell, button_startWave,
	button_hunterTower, button_soldierTower, button_shamanTower, button_snowTower,
	button_quitGame, button_cancel;
	
	/* Pause */
	private boolean pause;
	
	private Map map;
	private ArrayList<Monster> monsterList;
	private ArrayList<Tower> towerList;
	
	/* Remove Tower */
	private ArrayList<Monster> monsterRemovalList;
	private ArrayList<Tower> towerRemovalList;
	
	private Tower buyTower, selectedTower;
	private AStarPathFinder buyTowerPathfinder;
	
	private Color selectedFill, selectedRing;
	
	private ArrayList<Monster> spawnList;
	
	private boolean lost;
	
	private int wave;
	private int spawnInterval = 1000, lastSpawn;
	
	private int gold;
	private int baseHealth, currentHealth;

	private boolean gotMoneyForWave = true;
	
	/**
	 * Create new status
	 * @param map's selected
	 */
	public Game(Map map) {
		/* Load Background */
		sidebar_background = ImageManager.getImage(ImageManager.GAME_SIDEBAR_BG);
		info = ImageManager.getImage(ImageManager.GAME_INFO);
		
		map.setGame(this);
		
		buyTowerPathfinder = new AStarPathFinder(map,200,false);
		
		this.map = map;
		this.monsterList = new ArrayList<Monster>();
		this.monsterRemovalList = new ArrayList<Monster>();
		this.towerList = new ArrayList<Tower>();
		this.towerRemovalList = new ArrayList<Tower>();
		this.spawnList = new ArrayList<Monster>();
		this.gold = map.getStartingMoney();
		this.baseHealth = 20;
		this.currentHealth = baseHealth;
		
		this.selectedFill =  new Color(41,136,255,40);
		this.selectedRing =  new Color(41,136,255,180);
	}

	public Map getMap() {
		return map;
	}
	
	/**
	 * Remove Enemies was dead
	 * @return Enemies list
	 */
	public ArrayList<Monster> getMonsterList() {
		if(!monsterRemovalList.isEmpty()) {
			for(Monster e : monsterRemovalList) {
				monsterList.remove(e);
			}
		}
		return monsterList;
	}

	public ArrayList<Tower> getTowerList() {
		return towerList;
	}

	/**
	 * Buy Tower
	 * @return Tower
	 */
	public Tower getBuyTower() {
		return buyTower;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public int getWave() {
		return wave;
	}

	/**
	 * Upgrade Tower or Sell
	 * Button
	 */
	@Override
	public void init(GameContainer gc, final StateBasedGame sbg) throws SlickException {	
		button_upgrade = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_UPGRADE),1100,332);
		button_sell =  new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_SELL),1100,378);
		button_startWave =  new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_STARTWAVE),1100,755);
		
		button_hunterTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.HUNTER_TOWER_1),1104,48);
		button_soldierTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.SOLDIER_TOWER_1),1154,48);
		button_shamanTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.SHAMAN_TOWER_1),1104,96);
		button_snowTower = new MouseOverArea(gc,ImageManager.getImage(ImageManager.SNOW_TOWER_1),1154,96);
		
		button_quitGame = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_QUITGAME),550,287);
		button_cancel = new MouseOverArea(gc,ImageManager.getImage(ImageManager.GAME_BUTTON_CANCEL),550,345);
		
		/* Tower Button */
		button_hunterTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new HunterTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_soldierTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new SoldierTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_shamanTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new ShamanTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		button_snowTower.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				buyTower = new SnowTower(new Point(-1000,-1000), Game.this);
				selectedTower = buyTower;
			}		
		});
		
		/* Start Wave Button */
		button_startWave.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				if(spawnList.isEmpty() && wave < map.getWaveList().size()) {
					wave++;
					spawnList = map.getMonsterList(wave);
					
					if(!gotMoneyForWave) {
						gold += map.getWaveMoney();
					} else {
						gotMoneyForWave = false;
					}
				}
			}		
		});
		
		/* Upgrade Button */
		button_upgrade.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				int upgradeLevel = selectedTower.getUpgradeLevel();
				int cost = selectedTower.getUpgradeCost();
				if(upgradeLevel < 2 && gold >= cost) {
					selectedTower.setUpgradeLevel(upgradeLevel+1);
					gold -= cost;
				}
			}		
		});
		
		button_sell.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				int reward = 0;
				for(int i = 0; i <= selectedTower.getUpgradeLevel();i++) {
					reward += selectedTower.getCost(i)/2;
				}
				
				gold += reward;
				
				Game.this.removeTower(selectedTower);
				Point position = selectedTower.getTilePosition();
				map.setTower((int)position.getX(), (int)position.getY(), false);
				
				selectedTower = null;
			}		
		});
		
		/* Pause Button */
		button_quitGame.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				map.resetTowerList();
				sbg.enterState(1,new FadeOutTransition(), new FadeInTransition());
			}		
		});
		
		button_cancel.addListener(new ComponentListener() {
			@Override
			public void componentActivated(AbstractComponent arg0) {
				pause = false;
			}		
		});
	}

	/**
	 * GameEngine
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		/* Back to Main */
		if(lost) {
			Summary summary = new Summary(this,false);
			summary.init(gc, sbg);
			sbg.addState(summary);
			sbg.enterState(4,new FadeOutTransition(), new FadeInTransition());
		}
		
		Input input = gc.getInput();
		if(!pause) {
			lastSpawn += delta;
			
			/* Enemies spawn */
			if(!spawnList.isEmpty() && lastSpawn > spawnInterval) {
				/* Spawn enemies */
				monsterList.add(spawnList.get(0));
				spawnList.remove(0);
				lastSpawn = 0;
			/* finish wave */
			} else if(spawnList.isEmpty() && monsterList.isEmpty()) {
				
				if(!gotMoneyForWave) {
					gold += map.getWaveMoney();
					gotMoneyForWave = true;
				}
				
				if(wave == map.getWaveList().size()) {
					/* Win */
					Summary summary = new Summary(this,true);
					summary.init(gc, sbg);
					sbg.addState(summary);
					sbg.enterState(4,new FadeOutTransition(), new FadeInTransition());
				}
			}
			
			/* Restart */
			if(!monsterRemovalList.isEmpty()) {
				for(Monster e : monsterRemovalList) {
					monsterList.remove(e);
				}
			}
			if(!towerRemovalList.isEmpty()) {
				for(Tower t : towerRemovalList) {
					towerList.remove(t);
				}
			}
			
			/* Update status of tower and enemies */
			for(Tower tower : towerList) {
				tower.update(gc, delta);
			}
			for(Monster monster : monsterList) {
				monster.update(gc, delta);
			}
			
			int mouseX = Mouse.getX();
			int mouseY = 800-Mouse.getY();
			
			/* position tower setup */
			if(buyTower != null) {
				if(mouseX < 1056 && mouseY < 720) {
					int tileposx = (int)Math.floor((mouseX)/48);
					int tileposy = (int)Math.floor((mouseY)/48);
					
					/* select position */
					int towerPosX = tileposx*48+24;
					int towerPosY = tileposy*48+24;				
					buyTower.setPosition(new Point(towerPosX,towerPosY));
				}
			}
			
			/* Left-Click */
			if(input.isMousePressed(0)) {
				
				/* Point to position */
				if(mouseX < 1056 && mouseY < 720) {
					
					int tileposx = (int)Math.floor((mouseX)/48);
					int tileposy = (int)Math.floor((mouseY)/48);
					
					/* Selected */
					if(buyTower != null) {

						/* isEmpty position */
						if(!map.isTower(tileposx, tileposy)) {
							
							/* Check money */			
							if(buyTower.getCost() <= gold) {
								
								/* Checking that tower is on the main path or not */
								boolean blocking = false;
								Point base = map.getBase();
								for(Point spawn : map.getSpawnList()) {
									if(buyTowerPathfinder.findPath(null,(int)spawn.getX(),(int)spawn.getY(),(int)base.getX(),(int)base.getY()) == null) {
										blocking = true;
										break;
									} else if(tileposx == spawn.getX() && tileposy == spawn.getY()) {
										blocking = true;
									}
								}
								
								/* Path checking */
								if(!blocking) {
									/* Buy Tower */
									gold -= buyTower.getCost();
									towerList.add(buyTower);
									/* add Tower */
									map.setTower(tileposx, tileposy, true);
									
									/* Cancel Tower */
									selectedTower = null;
									
									/* Build Tower */
									if(input.isKeyDown(Input.KEY_LSHIFT)) {
										if(buyTower instanceof HunterTower) {			
											buyTower = new HunterTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof SoldierTower) {
											buyTower = new SoldierTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof ShamanTower) {
											buyTower = new ShamanTower(new Point(-1000,-1000), Game.this);
											
										} else if(buyTower instanceof SnowTower) {
											buyTower = new SnowTower(new Point(-1000,-1000), Game.this);
											
										}
										
										/* Select new towers */
										selectedTower = buyTower;
									} else {
										buyTower = null;
									}		
								}
							}
						}
						
					/* Click Tower */
					} else if(map.isTower(tileposx, tileposy)) {
						Tower clickedTower = null;
						
						Point clickedTilePos = new Point(tileposx, tileposy);
						for(Tower t: towerList) {
							Point towerTilePos = t.getTilePosition();
							if(clickedTilePos.getX() == towerTilePos.getX() && clickedTilePos.getY() == towerTilePos.getY()) {
								clickedTower = t;
							}
						}
						
						/* Select tower */
						selectedTower = clickedTower;
						
					} else {
						/* Cancel select tower */
						selectedTower = null;
					}
				}
			}
		}
		
		/* ESC button */
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			if(buyTower != null) {
				buyTower = null;
				selectedTower = null;
			} else {
				pause = !pause;
			}
		}
	}

	/**
	 * GameEngine
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		/* Show Map */
		map.getMap().render(0,0);
		
		/* Show Option */
		sidebar_background.draw(0,0);
		
		/* Show Money */
		LatoFont.draw(86,763,gold+"",22);
		
		/* Show HP */
		LatoFont.draw(215,763,currentHealth+"/"+baseHealth,22);
		
		/* Show Wave */
		LatoFont.draw(960,763,"Wave "+wave+"/"+map.getWaveList().size(),22);
		
		/* NextWave Button */
		button_startWave.render(gc,g);
		
		/* Info next waves */	
		Integer[] nextWave = map.getWaveUnits(wave+1);
		if(nextWave != null) {
			LatoFont.draw(1105,660,"Next Wave:",14);
			LatoFont.draw(1105,680,"Ground: "+nextWave[0],14);
			LatoFont.draw(1105,700,"Air: "+nextWave[1],14);
			LatoFont.draw(1105,720,"Ancient: "+nextWave[2],14);
			LatoFont.draw(1105,740,"Boss: "+nextWave[3],14);
		} else {
			LatoFont.draw(1105,720,"Last Wave!",14);
		}
		
		/* Tower Buttons */
		button_hunterTower.render(gc,g);
		button_soldierTower.render(gc,g);
		button_shamanTower.render(gc,g);
		button_snowTower.render(gc,g);
		
		
		if(selectedTower != null) {
			/* Show Range Tower */
			Point position = selectedTower.getPosition();
			Circle rangeCircle = new Circle(position.getX(),position.getY(),selectedTower.getRange());
			
			g.setColor(selectedFill);
			g.fill(rangeCircle);
			
			g.setColor(selectedRing);
			g.draw(rangeCircle);
			
			
			/* Info Menu */
			info.draw(1100,180);

			if(selectedTower instanceof ShootingTower) {
				ShootingTower shTower = (ShootingTower) selectedTower;
//				LatoFont.draw(1105, 220, "Name: " + shTower.getName(), 14);
				LatoFont.draw(1105, 220, "Damage: " + shTower.getDamage(), 14);
				LatoFont.draw(1105, 240, "Range: " + shTower.getRange(), 14);
				LatoFont.draw(1105, 260, "Atk Speed: " + Math.round(1000f/shTower.getShootInterval()*100)/100f, 14);
				LatoFont.draw(1105, 280, "Kills: " + shTower.getKillCount(), 14);
				if(selectedTower == buyTower)
					LatoFont.draw(1105, 300, "Cost: " + buyTower.getCost(), 14);
			} else if(selectedTower instanceof SnowTower) {
				SnowTower SnowTower = (SnowTower) selectedTower;
				LatoFont.draw(1105, 220, "Range: "+SnowTower.getRange(), 14);
				LatoFont.draw(1105, 240, "Slow Value: "+(int)((1-SnowTower.getSlowValue())*100)+"%", 14);
				if(selectedTower == buyTower)
					LatoFont.draw(1105, 260, "Cost: " + buyTower.getCost(), 14);
			}
			
			if(selectedTower != buyTower && selectedTower != null) {
				if(selectedTower.getUpgradeLevel() < 2) {
					LatoFont.draw(1170, 339, selectedTower.getUpgradeCost()+"", 22);
					button_upgrade.render(gc, g);
				}
				button_sell.render(gc, g);		
			}
		}
		
		/* Show when player're buying tower */
		if(buyTower != null) {
			buyTower.render(gc, g);			
		}
		
		/* Render Entities */
		for(Tower tower : towerList) {
			tower.render(gc, g);
		}
		for(Monster monster : monsterList) {
			monster.render(gc, g);
		}
		
		/* Pause */
		if(pause) {
			g.setColor(new Color(0, 15, 35, 198));
			g.fillRect(0,0,1280,800);
			
			LatoFont.draw(455, 233, "Do you really want to quit?", 32);
			button_quitGame.render(gc, g);
			button_cancel.render(gc, g);
		}
	}
	
	@Override
	public int getID() {
		return 3;
	}
	
	public int getHealth() {
		return currentHealth;
	}

	/**
	 * Update Player HP
	 * End when Player Hp equal 0
	 * @param HP
	 */
	public void setHealth(int health) {
		if(health > 0) {
			currentHealth = health;
		} else {
			/* Damage Taken */
			lost = true;
		}
	}
	
	/**
	 * Mark remove tower
	 * @param tower remove
	 */
	public void removeTower(Tower tower) {
		towerRemovalList.add(tower);
	}
	
	/**
	 * Mark remove Tower
	 * @param remove monster
	 */
	public void removeMonster(Monster monster) {

		monsterRemovalList.add(monster);
		
		/* remove tower */
		for(Tower t : towerList) {
			if(t instanceof ShootingTower) {
				ShootingTower st = (ShootingTower) t;
				if(st.getTarget() == monster) {
					st.clearTarget();
				}
			}
		}
	}
}
