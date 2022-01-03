package pepse.util;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.util.Vector2;
import pepse.world.treee.leaf;

import java.util.Random;

public class nested implements Runnable {
    private final GameObject obj;
    private final  Random random = new Random();

    public nested(GameObject obj){
        this.obj =obj;
    }
    public void consumer(float angle) {
        obj.renderer().setRenderableAngle(angle);
//                            leaf.setDimensions();
        obj.setCenter(obj.getCenter().add(new Vector2(random.nextFloat(-0.2f,0.2f),random.nextFloat(-0.2f,0.2f))));
        obj.setDimensions(obj.getDimensions().add(new Vector2(random.nextFloat(-0.2f,0.2f),0)));
    }
    @Override
    public void run() {
        new Transition<Float>(
                obj, // the game object being changed
                this::consumer, // the method to call
                random.nextFloat(-20f,20f), // initial transition value
                random.nextFloat(-20f,20f), // final transition value
                Transition.LINEAR_INTERPOLATOR_FLOAT, // use a cubic interpolator
                10, // transtion fully over half a day
                Transition.TransitionType.TRANSITION_LOOP,
                null);
    }
}
