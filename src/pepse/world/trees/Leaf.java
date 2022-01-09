package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

/**
 * present singel leaf
 */
public class Leaf extends Block {
    private static final Color LEAVE_COLOR = new Color(50, 200, 30);
    private static final float FADEOUT_TIME = 30;
    private static final float SHAKE_AT_WIND_VALUE = 20f;
    private static final float GOOD_NUMBER_FOR_SHAKE = 0.2F;
    private final Random random;
    private final Vector2 initial;
    private final Terrain terrain;

    /**
     * constructor
     * @param topLeftCorner  topLeftCorner
     * @param terrain the ground to prevent collision
     */
    public Leaf(Vector2 topLeftCorner, Terrain terrain) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier.approximateColor(LEAVE_COLOR)));
        this.initial = topLeftCorner;
        this.random = new Random();
        this.terrain = terrain;
        new ScheduledTask(this,
                random.nextFloat() * 10, false, this::windShaking);
        startCycle();
    }

    /**
     * to every leaf have unique cycle that after lifetime it starts to drop and fade
     */
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

    /**
     * make drop and fade.
     */
    private void dropAndFade() {
        this.renderer().fadeOut(FADEOUT_TIME);
        new Transition<Float>(
                this, // the game object being changed
                this::drop, // the method to call
                -2f, // initial transition value
                2f, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                40,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        float death_time = random.nextFloat() * 10;
        new ScheduledTask(this,
                death_time + FADEOUT_TIME,
                false,
                this::startCycle);

    }

    /**
     * make the drop movement right and left.
     * @param angle angle
     */
    public void drop(float angle) {
        if (getCenter().y() <= terrain.groundHeightAt(getCenter().x())) {
            setVelocity(new Vector2(angle * 30, 30));
        } else {
            setVelocity(Vector2.ZERO);
        }
    }

    /**
     * make the shake of leaf in the wind transition
     */
    private void windShaking() {
        new Transition<>(
                this, // the game object being changed
                this::shake, // the method to call
                random.nextFloat( )* SHAKE_AT_WIND_VALUE *2 - SHAKE_AT_WIND_VALUE, // initial transition value
                random.nextFloat( )* SHAKE_AT_WIND_VALUE *2 - SHAKE_AT_WIND_VALUE, // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                40,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }

    /**
     * do the specific shake for the leaf.
     * implementation: change the size and move a little according to angle
     * @param angle the angle from transition
     */
    private void shake(float angle) {
        if (getCenter().y() <= terrain.groundHeightAt(getCenter().x())) {
            renderer().setRenderableAngle(angle);
            setCenter(getCenter().add(new Vector2(GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1), GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1))));
            setDimensions(new Vector2(Math.max(getDimensions().x() + GOOD_NUMBER_FOR_SHAKE*(random.nextFloat( )*2 -1), GOOD_NUMBER_FOR_SHAKE/2), getDimensions().y()));
        }
    }


}
