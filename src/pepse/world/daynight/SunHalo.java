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
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);

    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            GameObject sun,
            Color color){
        GameObject sunHalo = new GameObject(Vector2.ZERO,sun.getDimensions().mult(2f),new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(TAG);
        gameObjects.addGameObject(sunHalo,layer);
        sunHalo.addComponent((delta) -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }

}
