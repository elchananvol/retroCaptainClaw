package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;

public class PepseGameManager extends GameManager {
    PepseGameManager() {
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        GameObject sky = Sky.create(gameObjects(),new Vector2(windowController.getWindowDimensions().x(), windowController.getWindowDimensions().y()/5), Layer.BACKGROUND);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

