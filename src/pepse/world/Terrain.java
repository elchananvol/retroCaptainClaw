package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final NoiseGenerator noiseGenerator;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private final Vector2 windowDimensions;

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        this.noiseGenerator = new NoiseGenerator(seed);

    }

    public float groundHeightAt(float x) {
//        x =Block.round(x);
        x= (int) Math.floor( x / (Block.SIZE*3)) * Block.SIZE*3;
        return windowDimensions.y()-  Block.round((float) noiseGenerator.noise((double) x) * Block.SIZE * 5 + windowDimensions.y() / 4);
    }

    public void createInRange(int minX, int maxX) {
        assert maxX >= minX;
        minX =  Block.round(minX);
        maxX = Block.round(maxX);
        for (int i = minX; i <= maxX; i += Block.SIZE ) {
//            for (int j = i; j <= i + Block.SIZE * 2; j += Block.SIZE) {
                for (int hight = (int)windowDimensions.y() - Block.SIZE; hight >= (int)groundHeightAt(i); hight -= Block.SIZE) {
                    Vector2 location = new Vector2(i,  hight);
                    GameObject block = new Block(location, new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                    gameObjects.addGameObject(block, groundLayer);
                }
//            }


        }


    }


}
