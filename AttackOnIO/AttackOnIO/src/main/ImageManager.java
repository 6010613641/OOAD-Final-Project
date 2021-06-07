package main;

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class ImageManager {
	
	/* Tower */
	public static final int SOLDIER_TOWER_1 = 0;
	public static final int SOLDIER_TOWER_2 = 1;
	public static final int SOLDIER_TOWER_3 = 2;
	
	public static final int HUNTER_TOWER_1 = 3;
	public static final int HUNTER_TOWER_2 = 4;
	public static final int HUNTER_TOWER_3 = 5;
	
	public static final int SHAMAN_TOWER_1 = 6;
	public static final int SHAMAN_TOWER_2 = 7;
	public static final int SHAMAN_TOWER_3 = 8;
	
	public static final int SNOW_TOWER_1 = 9;
	public static final int SNOW_TOWER_2 = 10;
	public static final int SNOW_TOWER_3 = 11;
	
	/* Menu */
	public static final int MENU_BACKGROUND = 12;
	public static final int MENU_TEXT_ATTACKONIO = 13;
	public static final int MENU_TEXT_CHOOSEMAP = 14;
	
	public static final int MENU_BUTTON_PLAY = 15;
	public static final int MENU_BUTTON_CREDITS = 16;
	public static final int MENU_BUTTON_EXIT = 17;
	
	public static final int MENU_BUTTON_LARROW = 18;
	public static final int MENU_BUTTON_RARROW = 19;
	
	/* UI */
	public static final int GAME_SIDEBAR_BG = 20;
	public static final int GAME_INFO = 21;
	public static final int GAME_BUTTON_UPGRADE = 22;
	public static final int GAME_BUTTON_SELL = 23;
	public static final int GAME_BUTTON_STARTWAVE = 24;
	public static final int GAME_BUTTON_QUITGAME = 25;
	public static final int GAME_BUTTON_CANCEL = 26;
	public static final int GAME_BUTTON_RETRY = 27;
	
	/* Projectile */
	public static final int PROJECTILE_BLUE = 28;
	public static final int PROJECTILE_CYAN = 29;
	public static final int PROJECTILE_GREEN = 30;
	
	/* Enemy */
	public static final int GHOST = 31;
	public static final int GHOST_BOSS = 32;
	public static final int HUMAN = 33;
	public static final int HUMAN_BOSS = 34;
	public static final int ANCIENT = 35;
	public static final int ANCIENT_BOSS = 36;
	
	
	private static ArrayList<Image> imageList;
	
	static {
		imageList = new ArrayList<Image>();
		
		try {
			/* Tower */
			
			/* soldier */
			imageList.add(new Image("res/images/towers/soldierTower1.png"));
			imageList.add(new Image("res/images/towers/soldierTower2.png"));
			imageList.add(new Image("res/images/towers/soldierTower3.png"));
			
			/* ancient */
			imageList.add(new Image("res/images/towers/hunterTower1.png"));
			imageList.add(new Image("res/images/towers/hunterTower2.png"));
			imageList.add(new Image("res/images/towers/hunterTower3.png"));
			
			/* shaman */
			imageList.add(new Image("res/images/towers/shamanTower1.png"));
			imageList.add(new Image("res/images/towers/shamanTower2.png"));
			imageList.add(new Image("res/images/towers/shamanTower3.png"));
			
			imageList.add(new Image("res/images/towers/snowTower1.png"));
			imageList.add(new Image("res/images/towers/snowTower2.png"));
			imageList.add(new Image("res/images/towers/snowTower3.png"));
			
			/* Menu */
			/* Background */
			imageList.add(new Image("res/images/menus/N background.png"));
			
			/* Text */
			imageList.add(new Image("res/images/menus/N text-attackonio.png"));
			imageList.add(new Image("res/images/menus/N text-choose-map.png"));
			
			imageList.add(new Image("res/images/menus/N button-play.png"));
			imageList.add(new Image("res/images/menus/N button-credits.png"));
			imageList.add(new Image("res/images/menus/N button-exit.png"));
			
			imageList.add(new Image("res/images/menus/N button-arrow-left.png"));
			imageList.add(new Image("res/images/menus/N button-arrow-right.png"));
			
			/* UI */
			imageList.add(new Image("res/images/menus/N game-sidebarbg.png"));
			imageList.add(new Image("res/images/menus/N info.png"));
			imageList.add(new Image("res/images/menus/N button-upgrade.png"));
			imageList.add(new Image("res/images/menus/N button-sell.png"));
			imageList.add(new Image("res/images/menus/N button-startwave.png"));
			imageList.add(new Image("res/images/menus/N button-quitgame.png"));
			imageList.add(new Image("res/images/menus/N button-cancel.png"));
			imageList.add(new Image("res/images/menus/N button-retry.png"));
			
			/* Projectile */
			imageList.add(new Image("res/images/projectiles/projectile-hunter.png"));
			imageList.add(new Image("res/images/projectiles/projectile-shaman.png"));
			imageList.add(new Image("res/images/projectiles/bullet-soldier.png"));
			
			/* Enemy */
			
			/* ghost */
			imageList.add(new Image("res/images/enemies/ghost.png"));
			imageList.add(new Image("res/images/enemies/ghost_boss.png"));
			
			/* human */
			imageList.add(new Image("res/images/enemies/human.png"));
			imageList.add(new Image("res/images/enemies/human_boss.png"));
			
			/* ancient */
			imageList.add(new Image("res/images/enemies/ancient.png"));
			imageList.add(new Image("res/images/enemies/ancient_boss.png"));
			
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public static Image getImage(int id) {
		return imageList.get(id).copy();
	}
}
