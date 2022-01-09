package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Leaf extends Block {
    private static final Color LEAVE_COLOR = new Color(50, 200, 30);
    private static final float FADEOUT_TIME = 30;
    private static final float SHAKE_VALUE = 20f;
    private static final float GOOD_NUMBER_FOR_SHAKE = 0.2F;
    private final Random random;
    private final Vector2 initial;
    private final Terrain terrain;

    public Leaf(Vector2 topLeftCorner, Terrain terrain) {
        super(topLeftCorner, new RectangleRenderable(LEAVE_COLOR));
//        setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.initial = topLeftCorner;
        this.random = new Random();

        this.terrain = terrain;
        new ScheduledTask(this,
                random.nextFloat() * 10, false, this::windShaking);

        startCycle();

    }

    private void startCycle() {
        setTopLeftCorner(initial);
        renderer().setOpaqueness(1f);
        setVelocity(Vector2.ZERO);
        float life_time = random.nextFloat() * 200;
        new ScheduledTask(this,
                life_time,
                false,
                this::dropAndFade);
    }

    private void dropAndFade() {
        this.renderer().fadeOut(FADEOUT_TIME);
        new Transition<Float>(
                this, // the game object being changed
                this::drop, // the method to call
                -1f, // initial transition value
                1f, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                20, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        float death_time = random.nextFloat() * 10;
        new ScheduledTask(this,
                death_time + FADEOUT_TIME,
                false,
                this::startCycle);

    }

    public void drop(float angle) {
        if (getCenter().y() <= terrain.groundHeightAt(getCenter().x())) {
            setVelocity(new Vector2(angle * 30, 30));
        } else {
            setVelocity(Vector2.ZERO);
        }
    }


    public void windShaking() {
        new Transition<Float>(
                this, // the game object being changed
                this::shake, // the method to call
                random.nextFloat( )*SHAKE_VALUE*2 -SHAKE_VALUE, // initial transition value
                random.nextFloat( )*SHAKE_VALUE*2 -SHAKE_VALUE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                10, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }


    public void shake(float angle) {
        if (getCenter().y() <= terrain.groundHeightAt(getCenter().x())) {
            renderer().setRenderableAngle(angle);
//                            leaf.setDimensions();
            setCenter(getCenter().add(new Vector2(GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1), GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1))));
            setDimensions(new Vector2(Math.max(getDimensions().x() + GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1), 0.1f), getDimensions().y()));
        }
    }


}
