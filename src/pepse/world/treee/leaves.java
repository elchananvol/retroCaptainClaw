package pepse.world.treee;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

public class leaves {

    private final GameObjectCollection gameObjects;
    private final int layer;
    //    private final Vector2 windowDimensions;
    private final Terrain terrain;
    private final Random random = new Random();

    public leaves(GameObjectCollection gameObjects, int layer, int locationX, int height, float groundHeight,Terrain terrain) {
        this.gameObjects = gameObjects;
        this.layer = layer;
        this.terrain =terrain;
        create(locationX, height, groundHeight);
    }

    private void create(int locationX, int height, float groundHeight) {
        int square = (height / 3)*Block.SIZE;
        for (float y = groundHeight - height*Block.SIZE-square; y <= groundHeight - height*Block.SIZE + square; y+=Block.SIZE) {
            for (int x = -square; x <= square; x+=Block.SIZE) {
                if (x != 0 && random.nextDouble()<0.7) {
                    Block leaf = new leaf(new Vector2(x+locationX, y),terrain);
                    leaf.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                    gameObjects.addGameObject(leaf, layer);
//                Consumer<Float> consumer = (angle) -> (leaf.renderer().setRenderableAngle(angle) leaf.setDimensions(leaf.getDimensions().mult((float)Math.sin((angle)))));



 // nothing further to execute upon reaching final value
//                leaf.renderer().setRenderableAngle(angle);
//                leaf.setDimensions(leaf.getDimensions().mult(Math.sin(angle)));


                }
            }
        }


    }


}
