package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * present treasure
 */
class Treasure extends GameObject {
    private final GameObjectCollection gameObjects;
    private final int value;
    private final Counter counter;

    /**
     * the constructor of single treasure.
     * @param topLeftCorner topLeftCorner of the img on screen
     * @param dimensions dimensions of the treasure
     * @param renderable renderable obj for present the treasure
     * @param gameObjects game objects collection
     * @param value the value of this treasure
     * @param counter counter of all treasures that was eaten until now.
     */
    public Treasure(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                    GameObjectCollection gameObjects, int value, Counter counter)
    {
        super(topLeftCorner, dimensions, renderable);
        this.gameObjects=gameObjects;
        this.value=value;
        this.counter =counter;
    }

    /**
     * onCollisionEnter method. increase counter and remove the treasure.
     * @param other other - the avatar
     * @param collision collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        counter.increaseBy(value);
        gameObjects.removeGameObject(this);
    }

    /**
     * shouldCollideWith only the avatar.
     * @param other other
     * @return true for avatar,false otherwise
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Avatar;
    }

}
