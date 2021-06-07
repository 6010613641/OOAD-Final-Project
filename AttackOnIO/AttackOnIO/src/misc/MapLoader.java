package misc;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.tiled.TiledMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Load Map from res/maps
 *
 */
public class MapLoader {
	
	/**
	 * Load Map from "res/maps"
	 * @return Map List
	 */
	public static ArrayList<Map> loadMaps() {
		ArrayList<Map> mapList = new ArrayList<Map>();
		
		File[] files = new File("res/maps").listFiles();
		
		for(File file : files) {
			if(file.isDirectory()) {
				File xml = new File(file.getPath()+"/properties.xml");
				File mapFile = new File(file.getPath()+"/map.tmx");
				File preview = new File(file.getPath()+"/preview.png");
				
				if(xml.exists() && mapFile.exists() && preview.exists()) {
					Image previewImage = null;
					TiledMap tiledMap = null;
					try {
						previewImage = new Image(preview.getPath());
						tiledMap = new TiledMap(mapFile.getPath());
					} catch (SlickException se) {
						se.printStackTrace();
					}
					
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					try {
						DocumentBuilder db = dbf.newDocumentBuilder();
						Document doc = db.parse(xml);
						Element root = doc.getDocumentElement();
						
						/* Attribute */
						String mapName = root.getElementsByTagName("Name").item(0).getTextContent();
						int startMoney = Integer.parseInt(root.getElementsByTagName("StartMoney").item(0).getTextContent());
						int killMoney = Integer.parseInt(root.getElementsByTagName("MoneyPerKill").item(0).getTextContent());
						int waveMoney = Integer.parseInt(root.getElementsByTagName("MoneyPerWave").item(0).getTextContent());
						int startHpHuman = Integer.parseInt(root.getElementsByTagName("BaseHealthHuman").item(0).getTextContent());
						int startHpGhost = Integer.parseInt(root.getElementsByTagName("BaseHealthGhost").item(0).getTextContent());
						int startHpAncient = Integer.parseInt(root.getElementsByTagName("BaseHealthAncient").item(0).getTextContent());
						int startHpBoss = Integer.parseInt(root.getElementsByTagName("BaseHealthBoss").item(0).getTextContent());
						int waveHpMultiplier = Integer.parseInt(root.getElementsByTagName("HealthMultiplier").item(0).getTextContent());
						
						/* Spawn Point */
						ArrayList<Point> spawnList = new ArrayList<Point>();
						NodeList spawnNodes = root.getElementsByTagName("Spawn");
						for(int i = 0; i < spawnNodes.getLength(); i++) {
							Element spawn = (Element)spawnNodes.item(i);
							int x = Integer.parseInt(spawn.getElementsByTagName("PosX").item(0).getTextContent());
							int y = Integer.parseInt(spawn.getElementsByTagName("PosY").item(0).getTextContent());
							spawnList.add(new Point(x,y));
						}
						
						/* Position */
						Element baseNode = (Element)root.getElementsByTagName("Base").item(0);
						int baseX = Integer.parseInt(baseNode.getElementsByTagName("PosX").item(0).getTextContent());
						int baseY = Integer.parseInt(baseNode.getElementsByTagName("PosY").item(0).getTextContent());
						Point base = new Point(baseX,baseY);
						
						/* Waves */
						ArrayList<Integer[]> waveList = new ArrayList<Integer[]>();
						NodeList waveNodes = root.getElementsByTagName("Wave");
						for(int i = 0; i < waveNodes.getLength(); i++) {
							Element wave = (Element)waveNodes.item(i);
							int humanUnits = Integer.parseInt(wave.getElementsByTagName("Human").item(0).getTextContent());
							int ghostUnits = Integer.parseInt(wave.getElementsByTagName("Ghost").item(0).getTextContent());
							int ancientUnits = Integer.parseInt(wave.getElementsByTagName("Ancient").item(0).getTextContent());
							int bossUnits = Integer.parseInt(wave.getElementsByTagName("Boss").item(0).getTextContent());
							waveList.add(new Integer[]{humanUnits,ghostUnits,ancientUnits,bossUnits});
						}
						
						System.out.println("Added map "+mapName);
						mapList.add(new Map(tiledMap, previewImage, mapName, startMoney, killMoney, waveMoney, startHpHuman, startHpGhost, startHpAncient, startHpBoss, waveHpMultiplier, spawnList, base, waveList));				
					} catch(Exception e) {
						e.printStackTrace();
						System.out.println("Could not read map '"+file.getName()+"'");
						continue;
					}
				} else {
					System.out.println("Invalid map folder '"+file.getName()+"'");
					continue;
				}
			}
		}
		return mapList;
	}
}
