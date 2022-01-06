package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

public class Night {
    private static final Color BASIC_DEY_NIGHT_COLOR = Color.black;
    private static final Float MIDNIGHT_OPACITY = 0.7f;
    private static final float INIT_VALUE = 0f;

    /**
     This function creates a black rectangular game object that covers the entire game window and changes its opaqueness in a cyclic manner, in order to resemble day-to-night transitions.
     Parameters:
     gameObjects - The collection of all participating game objects.
     layer - The number of the layer to which the created game object should be added.
     windowDimensions - The dimensions of the windows.
     cycleLength - The amount of seconds it should take the created game object to complete a full cycle.
     Returns:
     A new game object representing day-to-night transitions.
     */
    public static GameObject create(
            GameObjectCollection gameObjects,
            int layer,
            Vector2 windowDimensions,
            float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,windowDimensions,new RectangleRenderable(BASIC_DEY_NIGHT_COLOR));
        night.renderer().setOpaqueness(INIT_VALUE);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night,layer);
        new ScheduledTask(night,
                cycleLength*0.4f,
                false,
                ()->makeTransition(night,cycleLength));
        return night;


    }
    static void makeTransition(GameObject night,float cycleLength) {

        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                INIT_VALUE, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT, // use a cubic interpolator
                cycleLength / 2, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null); // nothing further to execute upon reaching final value
    }

    }
