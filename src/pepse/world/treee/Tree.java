package pepse.world.treee;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Tree {
    private static final Color TREE_COLOR =new Color(100, 50, 20);

    private final GameObjectCollection gameObjects;
    private final int layer;
    private final Vector2 windowDimensions;
    private final Terrain terrain;
    Random random =new Random();

    public Tree(GameObjectCollection gameObjects, int layer, Vector2 windowDimensions, Terrain terrain){
        this.gameObjects =gameObjects;
        this.layer =layer;
        this.windowDimensions =windowDimensions;
        this.terrain =terrain;
    }




    private void createTree(int locationX, int height){

        for(float i= terrain.groundHeightAt(locationX) - Block.SIZE; i>=  terrain.groundHeightAt(locationX) - height*Block.SIZE;i-=Block.SIZE){
            Block block = new Block(new Vector2(locationX, i), new RectangleRenderable(TREE_COLOR));
            gameObjects.addGameObject(block,layer);
        }
//        Block block = new Block(new Vector2(locationX, (int)windowDimensions.y() - terrain.groundHeightAt(locationX)), new RectangleRenderable(Color.cyan));
//        gameObjects.addGameObject(block,layer+1);
        new leaves(gameObjects,layer,locationX,height,terrain.groundHeightAt(locationX),terrain);

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
            if(random.nextDouble() <0.15){
                createTree(x,random.nextInt(30) +3);
            }

        }

    }

}
