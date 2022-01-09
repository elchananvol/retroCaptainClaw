package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block (larger objects can be created from blocks).
 */
public class Block extends GameObject {
    public static final int SIZE = 24;

    /**
     * block of fixed size
     topLeftCorner - The location of the top-left corner of the created block.
     renderable - A renderable to render as the block.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        setPhysics();

    }

    /**
     * x flout number is always between Block.SIZE *k <= x <=Block.SIZE *(k+1) for some k belong to N ( - integer).
     * @param x - flout
     * @return k
     */
    public static int round(float x){
            return (int) Math.floor((double) x / SIZE) * SIZE;
    }

    /**
     * block in unfixed size
     topLeftCorner - The location of the top-left corner of the created block.
     dimension - the size of block.
     renderable - A renderable to render as the block.
     */
    public Block(Vector2 topLeftCorner,Vector2 dimension, Renderable renderable) {
        super(topLeftCorner, dimension, renderable);
        setPhysics();

    }
    private void setPhysics(){
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }


}