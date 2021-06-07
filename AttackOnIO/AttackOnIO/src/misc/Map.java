package misc;

import java.util.ArrayList;

import states.Game;
import towers.Tower;
import main.ImageManager;

import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import entities.Monster;

/**
 * MAP
 *
 */
public class Map implements TileBasedMap {
	private String name;
	private Image preview;
	private TiledMap map;
	private int startingMoney, killMoney, waveMoney;
	private int waveHealthMultiplier;
	private int startHpHuman, startHpGhost, startHpBoss, startHpAncient;
	private ArrayList<Point> spawnList;
	private Point base;
	private ArrayList<Integer[]> waveList;
	
	private Game game;
	private int[][] towerList;
	
	
	/**
	 * Create Map
	 * @param import map files
	 * @param preview map
	 * @param map name
	 * @param Money per kill
	 * @param waveMoney 
	 * @param startHpHuman 
	 * @param startHpGhost
	 * @param startHpAncient
	 * @param startHpBoss
	 * @param waveHealthMultiplier 
	 * @param spawnList 
	 * @param base die
	 * @param waveList
	 */
	public Map(TiledMap map, Image preview, String name, int startingMoney, int killMoney, int waveMoney, int startHpHuman, int startHpGhost, int startHpAncient,int startHpBoss, int waveHealthMultiplier, 
				ArrayList<Point> spawnList, Point base, ArrayList<Integer[]> waveList) {
		this.preview = preview;
		this.map = map;
		this.name = name;
		this.startingMoney = startingMoney;
		this.killMoney = killMoney;
		this.waveMoney = waveMoney;
		this.startHpHuman = startHpHuman;
		this.startHpGhost = startHpGhost;
		this.startHpAncient = startHpAncient;
		this.startHpBoss = startHpBoss;
		this.waveHealthMultiplier = waveHealthMultiplier;
		this.spawnList = spawnList;
		this.base = base;
		this.waveList = waveList;
		
		this.towerList = new int[22][15];
	}
	
	
	/**
	 * Select tower to map
	 */
	public void resetTowerList() {
		this.towerList = new int[22][15];
	}
	
	public void setGame(Game game) {
		this.game = game;
	}

	public void setTower(int x, int y, boolean isTower) {
		towerList[x][y] = (isTower)? 1 : 0; 
	}
	
	/**
	 * check position tower
	 * @param check X-Coordinate
	 * @param check Y-Coordinate
	 * @return check tower
	 */
	public boolean isTower(int x, int y) {
		return (towerList[x][y] == 1)? true : false;
	}

	public String getName() {
		return name;
	}

	public Image getPreview() {
		return preview;
	}

	public int getStartingMoney() {
		return startingMoney;
	}

	public int getKillMoney() {
		return killMoney;
	}

	public int getWaveMoney() {
		return waveMoney;
	}

	public ArrayList<Point> getSpawnList() {
		return spawnList;
	}

	public Point getBase() {
		return base;
	}

	public ArrayList<Integer[]> getWaveList() {
		return waveList;
	}
	
	public Integer[] getWaveUnits(int wave) {
		return (wave <= waveList.size())? waveList.get(wave-1): null;
	}

	/**
	 * Path finder
	 */
	@Override
	public boolean blocked(PathFindingContext context, int x, int y) {
		if(map.getTileId(x, y, 1) == 0)
			return true;
		
		if(towerList[x][y] != 0)
			return true;
		
		/*	
		 * check block direction
		 */
		Mover mover = context.getMover();
		if(mover == null) {
			Tower buyTower = game.getBuyTower();
			if(buyTower != null) {
				Point towerPos = buyTower.getTilePosition();
				if(x == towerPos.getX() && y == towerPos.getY()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public float getCost(PathFindingContext context, int x, int y) {
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() {
		return map.getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return map.getWidth();
	}

	public TiledMap getMap() {
		return map;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

	/**
	 * Enemies's Armies
	 * @return Enemies's List
	 */
	public ArrayList<Monster> getMonsterList(int wave) {
		ArrayList<Monster> monsterList = new ArrayList<Monster>();
		
		/* check number of enemies unit */
		if(wave <= waveList.size() && wave > 0) {
			Integer[] numbers = waveList.get(wave-1);
			
			int spawn = 0;
			
			/* spawn ground units */
			for(int g = 0; g < numbers[0];g++) {
				/* frequency unit */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* enemies spawn point */
				Point spawnPoint = spawnList.get(spawn);
				
				/* add enemies */
				monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpHuman+(wave)*waveHealthMultiplier, 1.5f, false, false, ImageManager.getImage(ImageManager.HUMAN)));
			}
			
			/* spawn air unit */
			for(int g = 0; g < numbers[1];g++) {
				/* frequency unit */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* enemies spawn point */
				Point spawnPoint = spawnList.get(spawn);
				
				/* add enemies */
				monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpGhost+(wave)*waveHealthMultiplier, 2, true, false, ImageManager.getImage(ImageManager.GHOST)));
			}
			
			/* spawn ancient unit */
			for(int g = 0; g < numbers[2];g++) {
				/* frequency unit */
				if(spawn < spawnList.size()-1) {
					spawn++;
				} else {
					spawn = 0;
				}
				
				/* enemies spawn point */
				Point spawnPoint = spawnList.get(spawn);
				
				/* add enemies */
				monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
						startHpAncient+(wave)*waveHealthMultiplier, 1, false, false, ImageManager.getImage(ImageManager.ANCIENT)));
			}
			
			/* spawn boss unit */
			if(wave == 3) {
				for(int g = 0; g < numbers[3];g++) {
					/* frequency unit */
					if(spawn < spawnList.size()-1) {
						spawn++;
					} else {
						spawn = 0;
					}
					
					/* enemies spawn point */
					Point spawnPoint = spawnList.get(spawn);
					
					/* add enemies */
					monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
							startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.HUMAN_BOSS)));
				}
				return monsterList;
			}
			else if(wave == 5) {
				for(int g = 0; g < numbers[3];g++) {
					/* frequency unit */
					if(spawn < spawnList.size()-1) {
						spawn++;
					} else {
						spawn = 0;
					}
					
					/* enemies spawn point */
					Point spawnPoint = spawnList.get(spawn);
					
					/* add enemies */
					monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
							startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.GHOST_BOSS)));
				}
				return monsterList;
			}
			else if(wave == 7){
				for(int g = 0; g < numbers[3];g++) {
					/* frequency unit */
					if(spawn < spawnList.size()-1) {
						spawn++;
					} else {
						spawn = 0;
					}
					
					/* enemies spawn point */
					Point spawnPoint = spawnList.get(spawn);
					
					/* add enemies */
					monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
							startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.ANCIENT_BOSS)));
				}
				return monsterList;
			}
			else {
				for(int g = 0; g < numbers[3];g++) {
					/* frequency unit */
					if(spawn < spawnList.size()-1) {
						spawn++;
					} else {
						spawn = 0;
					}
					
					/* enemies spawn point */
					Point spawnPoint = spawnList.get(spawn);
					
					/* add enemies */
					if(g == 0 ) {
						monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
							startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.HUMAN_BOSS)));
					}
					else if(g == 1) {
						monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
								startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.ANCIENT_BOSS)));
					}
					else {
						monsterList.add(new Monster(game, new Vector2f(spawnPoint.getX()*48+24,spawnPoint.getY()*48+24), 
								startHpBoss+(wave)*waveHealthMultiplier, 1.5f, false, true, ImageManager.getImage(ImageManager.GHOST_BOSS)));
					}
				}
				return monsterList;
			}
			
		}
		return null;
	}

}

