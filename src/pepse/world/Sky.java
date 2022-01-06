package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sky {
    private static final Color BASIC_SKY_COLOR = Color.decode("#7398a1");

    /**
     public static danogl.GameObject create​(danogl.collisions.GameObjectCollection gameObjects, int layer, danogl.util.Vector2 windowDimensions, float cycleLength)
     This function creates a yellow circle that moves in the sky in an elliptical path (in camera coordinates).
     Parameters:
     gameObjects - The collection of all participating game objects.
     layer - The number of the layer to which the created sun should be added.
     windowDimensions - The dimensions of the windows.
     cycleLength - The amount of seconds it should take the created game object to complete a full cycle.
     Returns:
     A new game object representing the sun.
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions, int skyLayer){
        GameObject sky = new GameObject(
                Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sky.setTag("sky");
        gameObjects.addGameObject(sky,skyLayer);
        return sky;



    }
}
