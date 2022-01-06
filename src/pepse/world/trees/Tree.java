package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Tree {
    private static final Color TREE_COLOR =new Color(100, 50, 20);
    private static final float THE_PART_OF_TREE_WILL_BE_COVER_WITH_LEAVES = 1/3f;
    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Terrain terrain;
    private final Random random;
    private final int seed;
    private final HashMap<Integer,Block> map = new HashMap<Integer,Block>();

    /**
     *
     * @param gameObjects GameObjectCollection for edd trees
     * @param layer leyer that tree will add to.
     * @param seed seed for random so that all created world will be the same
     * @param terrain Terrain object to know the height of ground
     */

    public Tree(GameObjectCollection gameObjects, int layer,int seed, Terrain terrain){
        this.gameObjects =gameObjects;
        this.layer =layer;
        this.terrain =terrain;
        random = new Random(seed);
        this.seed = seed;
    }


    /**
     * this function create tree with leaves that root start at locationX
     * @param locationX location at x
     * @param height height of tree
     */
    private void createTree(int locationX, int height){
        float groundHeight = terrain.groundHeightAt(locationX);
        Vector2 treeSize =new Vector2(Block.SIZE, height*Block.SIZE);
        Vector2 treeTopLeftCorner =new Vector2(locationX, groundHeight - height*Block.SIZE);
        Block tree = new Block(treeTopLeftCorner, treeSize, new RectangleRenderable(TREE_COLOR));
        map.put(locationX,tree);
        gameObjects.addGameObject(tree,layer);

        int square = (int)(height*THE_PART_OF_TREE_WILL_BE_COVER_WITH_LEAVES)*Block.SIZE;
        for (float y = groundHeight - height*Block.SIZE-square; y <= groundHeight - height*Block.SIZE + square; y+=Block.SIZE) {
            for (int x = -square; x <= square; x+=Block.SIZE) {
                if (x != 0 && random.nextDouble()<0.5) {
                    Leaf leaf = new Leaf(new Vector2(x+locationX, y),terrain);
                    gameObjects.addGameObject(leaf, Math.abs(Objects.hash(locationX, seed)));
                }
            }
        }
    }

    /**
     * This method creates trees in a given range of x-values.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        minX =  Block.round(minX);
        maxX = Block.round(maxX);
        for(int x= minX;x<= maxX;x+=   Block.SIZE){
            random.setSeed(Objects.hash(x, seed));
            if(random.nextDouble() <0.1){
                createTree(x,random.nextInt(30) +3);
            }
        }
    }

    /**
     * This method delete trees in a given range of x-values.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void deleteInRange(int minX, int maxX){
        minX =  Block.round(minX);
        maxX = Block.round(maxX);
        for(int x= minX;x<= maxX;x+=   Block.SIZE){
            if(map.containsKey(x)){
                gameObjects.removeGameObject(map.get(x),layer);
                int leaf_layer = Math.abs(Objects.hash(x, seed));
                if (!gameObjects.isLayerEmpty(leaf_layer)) {
                    gameObjects.objectsInLayer(leaf_layer).forEach(
                            (leaf) -> gameObjects.removeGameObject(leaf, leaf_layer));
                }
                map.remove(x);
            }
        }
    }

}
