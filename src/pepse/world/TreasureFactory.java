package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import java.awt.*;
import java.util.Random;


/**
 * create random treasures
 */
public class TreasureFactory {
    private static final int MAX_TREASURE = 5;
    private static final int RANDOM_SIZE =80;
    /**
     * present struct of cpp
     */
    private static class Treasures{

        private final Renderable img;
        private final int value;
        public Treasures(Renderable img, int value){
            this.img=img;
            this.value=value;
        }

        public int getValue() {
            return value;
        }

        public Renderable getImg() {
            return img;
        }
    }

    private final static Renderable chalice = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/chalice.gif"));
    private final static Renderable coin = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/coin.gif"));
    private final static Renderable cross = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/cross.gif"));
    private final static Renderable crown = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/crown.gif"));
    private final static Renderable gecko = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/gecko.gif"));
    private final static Renderable goldbar = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/goldbar.gif"));
    private final static Renderable ring = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/ring.gif"));
    private final static Renderable scepter = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/scepter.gif"));
    private final static Renderable skull = new ImageRenderable(Toolkit.getDefaultToolkit().
            createImage("src/pepse/assets/Treasure/skull.gif"));
    private final static Treasures[] allTreasures ={new Treasures(chalice,30),new Treasures(chalice,30),
            new Treasures(chalice,2500),new Treasures(coin,100),new Treasures(cross,5000),
            new Treasures(crown,15000),new Treasures(gecko,10000),new Treasures(goldbar,500),
            new Treasures(ring,1500),new Treasures(scepter,7500),new Treasures(skull,25000)};
    private final static Random random = new Random();
    private final static Vector2 SIZE = new Vector2(40,40);


    /**
     * create random amount of treasure around of location.
     * @param gameObjects all game objects
     * @param location location to create around
     * @param counter counter of treasure to be added when the treasure will be "eaten"
     */
    public static void create(GameObjectCollection gameObjects, Vector2 location, Counter counter){
        int treasureNumbers = random.nextInt(MAX_TREASURE);
        for (int i=0; i<=treasureNumbers;i++){
            int treasure_num = random.nextInt(allTreasures.length);
            Vector2 locationOfTreasure = new Vector2(location.x() +random.nextInt(RANDOM_SIZE*2)-RANDOM_SIZE,
                    location.y()+random.nextInt(RANDOM_SIZE*3)-RANDOM_SIZE*2);
            Treasure treasure = new Treasure(locationOfTreasure,SIZE,allTreasures[treasure_num].getImg(),gameObjects,
                    allTreasures[treasure_num].getValue(),counter);
            gameObjects.addGameObject(treasure);
        }

    }
}

