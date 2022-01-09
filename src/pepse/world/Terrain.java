package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.HashMap;

public class Terrain {
    private final GameObjectCollection gameObjects;
    /**
     * GROUND_SIZE is size of ground peace in horizontal orientation
     */
    public static final int GROUND_SIZE = Block.SIZE*5;
    private final int groundLayer;
    private final NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final Vector2 windowDimensions;
    private final HashMap<Integer,Block> map = new HashMap<Integer,Block>();
    private final HashMap<Integer,Float> mapGroundHeight = new HashMap<Integer,Float>();

    /**
     gameObjects - The collection of all participating game objects.
     groundLayer - The number of the layer to which the created ground objects should be added.
     windowDimensions - The dimensions of the windows.
     seed - A seed for a random number generator.
     */
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed);

    }

    /**
     This method return the ground height at a given location.
     Parameters:
     x - A number.
     Returns:The ground height at the given location (the pixel in y).
     */
    public float groundHeightAt(float x) {
        int z= round(x);
        if (!mapGroundHeight.containsKey(z))
        {
            mapGroundHeight.put(z,windowDimensions.y()-  Block.round((float) noiseGenerator.noise(z) * GROUND_SIZE*3 + windowDimensions.y() / 5));
        }
        return mapGroundHeight.get(z);
    }
    /**
     * This method creates land in a given range of x-values rounded to a multiple of GROUND_SIZE.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of GROUND_SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of GROUND_SIZE).
     */
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

    /**
     * This method delete land in a given range of x-values rounded to a multiple of GROUND_SIZE.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of  GROUND_SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of  GROUND_SIZE).
     */
    public void deleteInRange(int minX, int maxX){
        minX = round(minX);
        maxX = round(maxX);
        for(int x= minX;x<= maxX;x+=   GROUND_SIZE){
            if(map.containsKey(x)){
                gameObjects.removeGameObject(map.get(x),groundLayer);
                map.remove(x);
            }
        }
    }


    /**
     * x flout number is always between GROUND_SIZE *k <= x <=GROUND_SIZE *(k+1) for some k belong to N ( - integer).
     * @param x - flout
     * @return k
     */
    public static int round(float x){
        return (int) Math.floor((double) x / GROUND_SIZE) *GROUND_SIZE;
    }


}
