package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Objects;
import java.util.Random;

public class enemy extends GameObject {

    private static final int MAX_SPPED = 400;
    private static final int MIN_SPEED = 30;
    private final float initialX;
    private final int range;
    private final static int MOVEMENTS_SPEED = 400;
    private final Terrain terrain;
    private final Vector2 dimensions;
    private final Random random;

    private boolean wait = false;
    private Vector2 savedVelocity =null;


    public enemy(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Terrain terrain, int seed) {
        super(topLeftCorner, dimensions, renderable);
        this.initialX = topLeftCorner.x();
        this.random = new Random(Objects.hash(initialX, seed));
        range = random.nextInt(5 * Terrain.GROUND_SIZE, 50 * Terrain.GROUND_SIZE);
        renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
        setVelocity(Vector2.RIGHT.mult(random.nextInt(MIN_SPEED,MAX_SPPED)));
//        if (random.nextBoolean()) {
//            setVelocity(getVelocity().mult(-1));
//            renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
//        }
        this.dimensions=dimensions;
        this.terrain = terrain;

//        transform().setAccelerationY(1000);
//        physics().preventIntersectionsFromDirection(Vector2.DOWN);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float locationX =getTopLeftCorner().x();
        if (Math.abs( locationX- initialX) >= range && !wait) {
            renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
            //setTopLeftCorner(getTopLeftCorner().subtract(new Vector2(0,MOVEMENTS_SPEED)));
            setVelocity(getVelocity().multX(-1));
            wait = true;
        }
        if (Math.abs(locationX- initialX) <= range) {
            wait = false;
        }
//        System.out.println(getTopLeftCorner());
//        System.out.println(Terrain.round(initialX)+5);
//        System.out.println(Terrain.round(initialX )+Terrain.GROUND_SIZE-5 -Avatar.IMAGE_SIZE);
//        System.out.println("FXGFG");
//        if((Terrain.round(initialX)+5 >=getTopLeftCorner().x()))
//        {
//            renderer().setIsFlippedHorizontally(false);
//            setVelocity(Vector2.RIGHT.mult(MOVEMENTS_SPEED));
//        }
//        if(Terrain.round(initialX )+Terrain.GROUND_SIZE-5 -Avatar.IMAGE_SIZE <=getTopLeftCorner().x())
//        {
//            renderer().setIsFlippedHorizontally(true);
//            setVelocity(Vector2.LEFT.mult(MOVEMENTS_SPEED));
//
//        }
//        setTopLeftCorner(new Vector2(locationX,Math.min(terrain.groundHeightAt(locationX),terrain.groundHeightAt(locationX-dimensions.x()))-dimensions.y()));
        setTopLeftCorner(new Vector2(locationX,terrain.groundHeightAt(getCenter().x())-dimensions.y()));


//                || Terrain.round(initialX )+Terrain.GROUND_SIZE-5 -Avatar.IMAGE_SIZE <=getTopLeftCorner().x() ) && !wait){

            //setTopLeftCorner(getTopLeftCorner().subtract(new Vector2(0,MOVEMENTS_SPEED)));

//            setVelocity(getVelocity().multX(-1));
//            System.out.println(getTopLeftCorner());
//            System.out.println(getVelocity());
//            System.out.println("here");

//        }
//        if(Terrain.round(initialX) <getTopLeftCorner().x() || Terrain.round(initialX +Terrain.GROUND_SIZE) >getTopLeftCorner().x() -Avatar.IMAGE_SIZE ){
//            wait = false;
//        }
//        if (lastLocation == getTopLeftCorner().x()) {
//
//            int dir = getVelocity().x() < 0 ? -1 : 1;
//            setTopLeftCorner(new Vector2(getTopLeftCorner().x() + dir*Avatar.IMAGE_SIZE , terrain.groundHeightAt(getTopLeftCorner().x() + dir) -Avatar.IMAGE_SIZE) );
//            lastLocation=Float.MAX_VALUE;
//        }
//        else {
//            lastLocation = getTopLeftCorner().x();
//        }


        //  setTopLeftCorner(new Vector2(getTopLeftCorner().x(),terrain.groundHeightAt(getTopLeftCorner().y())-Avatar.IMAGE_SIZE));


    }

    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Avatar;
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
            savedVelocity = new Vector2(getVelocity().x(),getVelocity().y());
            setVelocity(Vector2.ZERO);
    }


    @Override
    public void onCollisionExit(GameObject other) {
        setVelocity(savedVelocity);
//        System.out.println(savedVelocity);
//        savedVelocity =null;
//        if (life_counter <= 0) {
//            gameObjects.removeGameObject(this);
//        }

    }
}
