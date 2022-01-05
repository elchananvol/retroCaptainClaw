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

    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final Terrain terrain;
    private final Random random;
    private final int seed;
    private final HashMap<Integer,Block> map = new HashMap<Integer,Block>();

    public Tree(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions,int seed, Terrain terrain){
        this.gameObjects =gameObjects;
        this.layer =layer;
        this.windowDimensions =windowDimensions;
        this.terrain =terrain;
        random = new Random(seed);
        this.seed = seed;
    }




    private void createTree(int locationX, int height){
        float groundHeight = terrain.groundHeightAt(locationX);
        Vector2 treeSize =new Vector2(Block.SIZE, height*Block.SIZE);
        Vector2 treeTopLeftCorner =new Vector2(locationX, groundHeight - height*Block.SIZE);
        Block tree = new Block(treeTopLeftCorner, treeSize, new RectangleRenderable(TREE_COLOR));
        map.put(locationX,tree);
        gameObjects.addGameObject(tree,layer);
        int square = (height / 3)*Block.SIZE;
        for (float y = groundHeight - height*Block.SIZE-square; y <= groundHeight - height*Block.SIZE + square; y+=Block.SIZE) {
            for (int x = -square; x <= square; x+=Block.SIZE) {
                if (x != 0 && random.nextDouble()<0.5) {
                    Leaf leaf = new Leaf(new Vector2(x+locationX, y),terrain);
                    gameObjects.addGameObject(leaf, Math.abs(Objects.hash(locationX, seed)));
                }
            }
        }
//        new leaves(gameObjects,Math.abs(Objects.hash(locationX, seed)),locationX,height,terrain.groundHeightAt(locationX),terrain,seed);

    }

    /**
     * This method creates trees in a given range of x-values.
     * Parameters:
     * @param minX - The lower bound of the given range (will be rounded to a multiple of Block.SIZE).
     * @param maxX - The upper bound of the given range (will be rounded to a multiple of Block.SIZE).
     */
    public void createInRange(int minX, int maxX) {
        assert maxX >= minX;
        minX =  Block.round(minX);
        maxX = Block.round(maxX);


        for(int x= minX;x<= maxX;x+=   Block.SIZE){
            random.setSeed(Objects.hash(x, seed));
            if(random.nextDouble() <0.1){
                createTree(x,random.nextInt(30) +3);
            }
        }

    }
    public void deleteInRange(int minX, int maxX){
        minX =  Block.round(minX);
        maxX = Block.round(maxX);
        for(int x= minX;x<= maxX;x+=   Block.SIZE){
            if(map.containsKey(x)){
                gameObjects.removeGameObject(map.get(x),layer);
                int leaf_layer = Math.abs(Objects.hash(x, seed));
                if (!gameObjects.isLayerEmpty(leaf_layer)) {
                    gameObjects.objectsInLayer(leaf_layer).forEach((leaf) -> gameObjects.removeGameObject(leaf, leaf_layer));
                }
                map.remove(x);
            }

        }


    }

}
