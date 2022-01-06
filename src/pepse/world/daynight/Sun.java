package pepse.world.daynight;



import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Sun {
    private final static String TAG ="sun";
    private static final Color BASIC_SUN_COLOR =Color.decode("#f8e26b");
    private static final float SUN_SIZE =1F ;


    /**
     This function creates a yellow circle that moves in the sky in an elliptical path (in camera coordinates).
     Parameters:
     gameObjects - The collection of all participating game objects.
     layer - The number of the layer to which the created sun should be added.
     windowDimensions - The dimensions of the windows.
     cycleLength - The amount of seconds it should take the created game object to complete a full cycle.
     Returns: A new game object representing the sun.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject sun = new GameObject(Vector2.ZERO,windowDimensions.mult(SUN_SIZE),new OvalRenderable(BASIC_SUN_COLOR));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);
        gameObjects.addGameObject(sun,layer);
        makeMove(windowDimensions,sun,cycleLength);
        return sun;
    }

    /**
     * make to sun move over the sky
     * @param windowDimensions  windowDimensions
     * @param sun sun object
     * @param cycleLength day and night cycle length.
     */
    static void makeMove(Vector2 windowDimensions,GameObject sun,float cycleLength){
        Vector2 center = windowDimensions.mult(0.5f);
        Consumer<Float> calculateEllipsoid = (engel) -> sun.setCenter(new Vector2((float) Math.cos(engel),
                (float)Math.sin(engel)).multX(windowDimensions.x()/1.8f).multY(windowDimensions.y()/2).add(center));
        new Transition<Float>(
                sun, // the game object being changed
                calculateEllipsoid, // the method to call
                (float)Math.PI, // initial transition value
                (float)Math.PI *2, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength/2, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_ONCE,
                ()->sun.setCenter(Vector2.ONES.mult(-40))); // keep away from screen.
        new ScheduledTask(sun,
                cycleLength,
                false,
                ()->makeMove(windowDimensions,sun,cycleLength));

    }


}
