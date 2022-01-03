package pepse;

import com.sun.source.tree.Tree;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.*;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
//import pepse.world.treee.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 40);
    private static final String PATH_TO_NUSIC = "clow/clawmusic/Level_1.wav";

    PepseGameManager() {
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        Sound sound = soundReader.readSound(PATH_TO_NUSIC);
        sound.playLooped();
        GameObject sky = Sky.create(gameObjects(),new Vector2(windowController.getWindowDimensions().x(), windowController.getWindowDimensions().y()/5), Layer.BACKGROUND);
        GameObject night = Night.create(gameObjects(),Layer.FOREGROUND,windowController.getWindowDimensions(),20);
        GameObject sun = Sun.create(gameObjects(),Layer.FOREGROUND,windowController.getWindowDimensions(),5);
        GameObject sunHalo = SunHalo.create(gameObjects(),Layer.FOREGROUND+1,sun,SUN_HALO_COLOR);
        Terrain terrain = new Terrain(gameObjects(),Layer.FOREGROUND +2,windowController.getWindowDimensions(),10);
        terrain.createInRange(0,(int)windowController.getWindowDimensions().x());
        pepse.world.treee.Tree trees = new pepse.world.treee.Tree(gameObjects(),Layer.FOREGROUND +3, windowController.getWindowDimensions(),terrain);
        trees.createInRange(0,(int)windowController.getWindowDimensions().x());
        Avatar.create(gameObjects(),Layer.UI,Vector2.ZERO,inputListener,imageReader);
        int x=(int) Math.floor((double) -50 / (Block.SIZE*3)) * Block.SIZE*3;
        int z= (int) Math.floor((double) 50 / (Block.SIZE*3)) * Block.SIZE*3;
        System.out.printf("%d,%d",x,z);


    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

