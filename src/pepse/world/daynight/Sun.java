package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Sun {
    private final static String TAG ="sun";
    private static final Color BASIC_SUN_COLOR = Color.yellow;
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject sun = new GameObject(Vector2.ZERO,windowDimensions.mult(0.1f),new OvalRenderable(BASIC_SUN_COLOR));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);
        gameObjects.addGameObject(sun,layer);
        Vector2 center = windowDimensions.mult(0.5f);
//        float radius = windowDimensions.y() / 2;
        Function<Float,Float> radius = (engel) -> ((float)(1f-Math.abs(Math.sin(engel)))* windowDimensions.y()/2 + (float)Math.abs(Math.sin(engel))*windowDimensions.x()/2);
        Consumer<Float> consumer = (engel) -> sun.setCenter(new Vector2((float) Math.cos(engel),(float)Math.sin(engel)).multX(windowDimensions.x()/2).multY(windowDimensions.y()/2).add(center)); ;
        new Transition<Float>(
                sun, // the game object being changed
                consumer, // the method to call
                (float)Math.PI, // initial transition value
                (float)Math.PI *2, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_ONCE,
                null); // nothing further to execute upon reaching final value
        return sun;
    }


}
