package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class EnemyFactory {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final HashMap<Integer, enemy> map = new HashMap<Integer, enemy>();
    private final HashMap<Integer, Float> mapGroundHeight = new HashMap<Integer, Float>();
    static Renderable bearsailor = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("pepse/assets/enemy/bearsailor.gif"));
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
                enemy block = new enemy(new Vector2(i, 0), Vector2.ONES.mult(Avatar.IMAGE_SIZE), bearsailor, terrain, random.nextInt());

                map.put(i, block);
                gameObjects.addGameObject(block, layer);
            }
        }


    }

    public void deleteInRange(int minX, int maxX) {
        minX = Terrain.round(minX);
        maxX = Terrain.round(maxX);
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            if (map.containsKey(x)) {
                gameObjects.removeGameObject(map.get(x), layer);
                map.remove(x);
            }

        }
    }
}
