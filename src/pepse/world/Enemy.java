package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import java.util.Objects;
import java.util.Random;

/**
 * presnt en enemy.
 */
class Enemy extends GameObject {

    private static final int MAX_SPPED = 400;
    private static final int MIN_SPEED = 30;
    private static final int MIN_RANGE = 5;
    private final float initialX;
    private final int range;
    private final Terrain terrain;
    private final Vector2 dimensions;
    private boolean wait = false;
    private Vector2 savedVelocity =null;

    /**
     * create enemy obj
     * @param topLeftCorner topLeftCorner of the img on enemy
     * @param dimensions dimensions of the treasure
     * @param renderable renderable obj for present the enemy
     * @param terrain terrain obj (for knowing height of ground each step
     * @param seed seed for random.
     */
    public Enemy(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Terrain terrain, int seed) {
        super(topLeftCorner, dimensions, renderable);
        this.initialX = topLeftCorner.x();
        Random random = new Random(Objects.hash(initialX, seed));
        range = random.nextInt( MIN_RANGE*(MIN_RANGE-1) * Terrain.GROUND_SIZE)+MIN_RANGE * Terrain.GROUND_SIZE ;
        renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
        setVelocity(Vector2.RIGHT.mult(random.nextInt(MAX_SPPED+MIN_SPEED)-MIN_SPEED));
        this.dimensions=dimensions;
        this.terrain = terrain;

        if (random.nextBoolean()) {
            setVelocity(getVelocity().mult(-1));
            renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
        }
    }

    /**
     * make the enemy step inside a random range back and forth.
     * @param deltaTime deltaTime
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float locationX =getTopLeftCorner().x();
        if (Math.abs( locationX- initialX) >= range ) {
            if(!wait) {
                renderer().setIsFlippedHorizontally(!renderer().isFlippedHorizontally());
                setVelocity(getVelocity().multX(-1));
                wait = true;
            }
        }
        else {
            wait = false;
        }
        setTopLeftCorner(new Vector2(locationX,terrain.groundHeightAt(getCenter().x())-dimensions.y()));


    }

    /**
     * enemy collide only with the avatar
     * @param other - other game object
     * @return true if other is avatar
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Avatar;
    }

    /**
     * when Collision happen the enemy stop moving
     * @param other the avatar
     * @param collision -collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
            savedVelocity = new Vector2(getVelocity().x(),getVelocity().y());
            setVelocity(Vector2.ZERO);
    }

    /**
     * when Collision stop the enemy continue moving (if it didnt kill, see Avatar class)
     * @param other - the avatar
     */
    @Override
    public void onCollisionExit(GameObject other) {
        setVelocity(savedVelocity);
    }
}
