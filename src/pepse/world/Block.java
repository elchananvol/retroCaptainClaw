package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Block extends GameObject {
    public static final int SIZE = 30;

    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
    public static int round(float x){
        int z;
        if (x < 0) {
            z = (int) Math.floor((double) x / Block.SIZE) * Block.SIZE;
        } else {
            z = (int) Math.ceil((double) x / Block.SIZE) * Block.SIZE;
        }
        return z;
    }

}