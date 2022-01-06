package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class EnemyFactory {
    private final HashMap<Integer, enemy> map = new HashMap<Integer, enemy>();
    private final static Renderable bearsailor = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/bearsailor.gif"));
    private final static Renderable crazyhook = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/crazyhook.gif"));
    private final static Renderable cutthroat = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/cutthroat.gif"));
    private final static Renderable mercat = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/mercat.gif"));
    private final static Renderable officer = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/officer.gif"));
    private final static Renderable pegleg = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/pegleg.gif"));
    private final static Renderable punkrat = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/punkrat.gif"));
    private final static Renderable redtailpirate = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/redtailpirate.gif"));
    private final static Renderable robberthief = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/robberthief.gif"));
    private final static Renderable siren = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/siren.gif"));
    private final static Renderable tiger= new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/tiger.gif"));
    private final static Renderable tigerwhite = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/tigerwhite.gif"));
    private final static Renderable townguard1 = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/townguard1.gif"));
    private final static Renderable townguard2 = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("src/pepse/assets/enemy/townguard2.gif"));
    private final static Renderable[] ENEMIES = {bearsailor,crazyhook,cutthroat,mercat,officer,pegleg,punkrat,redtailpirate,robberthief,siren,tiger,tigerwhite,townguard1,townguard2};
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final int seed;
    Terrain terrain;
    Random random = new Random();

    public EnemyFactory(
            GameObjectCollection gameObjects,
            int layer, Terrain terrain,int seed) {
        this.terrain = terrain;
        this.gameObjects = gameObjects;
        this.layer =layer;
        this.seed =seed;
    }


    public void createInRange(int minX, int maxX) {
        assert maxX >= minX;
        minX = Terrain.round(minX);
        maxX = Terrain.round(maxX);
        for (int i = minX; i <= maxX; i += Terrain.GROUND_SIZE) {
            random.setSeed(Objects.hash(i, seed));
            if(random.nextDouble() <0.1) {
                if(!map.containsKey(i)) {

                    enemy enemy = new enemy(new Vector2(i, 0), Vector2.ONES.mult(Avatar.IMAGE_SIZE), ENEMIES[random.nextInt(ENEMIES.length)], terrain, seed);

                    map.put(i, enemy);
                    gameObjects.addGameObject(enemy, layer);
                }
            }
        }


    }

    public void deleteInRange(int minX, int maxX) {
        minX = Terrain.round(minX);
        maxX = Terrain.round(maxX);
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            if (map.containsKey(x)) {
                if(map.get(x).getTopLeftCorner().x()>minX && map.get(x).getTopLeftCorner().x()<maxX)
                if(gameObjects.removeGameObject(map.get(x), layer)) {

                    map.remove(x);
                }
            }

        }
    }
}
