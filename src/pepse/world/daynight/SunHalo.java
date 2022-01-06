package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo{
    private final static String TAG ="sun halo";
    private static final float SUN__HALO_SIZE =2F ;
    /**
     This function creates a halo around a given object that represents the sun. The halo will be tied to the given sun, and will always move with it.
     Parameters:
     gameObjects - The collection of all participating game objects.
     layer - The number of the layer to which the created halo should be added.
     sun - A game object representing the sun (it will be followed by the created game object).
     color - The color of the halo.
     Returns: A new game object representing the sun's halo.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        GameObject sunHalo = new GameObject(Vector2.ZERO,sun.getDimensions().mult(SUN__HALO_SIZE),new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(TAG);
        gameObjects.addGameObject(sunHalo,layer);
        sunHalo.addComponent((delta) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }

}
