package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.HashMap;

public class Terrain {
    private final GameObjectCollection gameObjects;
    public static final int GROUND_SIZE = Block.SIZE*3;
    private final int groundLayer;
    private final NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final Vector2 windowDimensions;
    private final HashMap<Integer,Block> map = new HashMap<Integer,Block>();
    private final HashMap<Integer,Float> mapGroundHeight = new HashMap<Integer,Float>();



    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed);

    }

    public float groundHeightAt(float x) {
        int z= round(x);
        if (!mapGroundHeight.containsKey(z))
        {
            mapGroundHeight.put(z,windowDimensions.y()-  Block.round((float) noiseGenerator.noise(z) * Block.SIZE * 10 + windowDimensions.y() / 4));
        }
        return mapGroundHeight.get(z);
    }

    public void createInRange(int minX, int maxX) {
        assert maxX >= minX;
        minX = round(minX);
        maxX = round(maxX);
        for (int i = minX; i <= maxX; i += GROUND_SIZE ) {
            Block block = new Block(new Vector2(i,  groundHeightAt(i)), new Vector2(GROUND_SIZE, windowDimensions.y()*2f -groundHeightAt(i)), new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
            map.put(i,block);
            gameObjects.addGameObject(block, groundLayer);
        }


    }
    public void deleteInRange(int minX, int maxX){
        minX = round(minX);
        maxX = round(maxX);
        for(int x= minX;x<= maxX;x+=   Block.SIZE){
            if(map.containsKey(x)){
                gameObjects.removeGameObject(map.get(x),groundLayer);
                map.remove(x);
            }

        }
    }
    public static int round(float x){
        return (int) Math.floor((double) x / GROUND_SIZE) *GROUND_SIZE;
    }


}
