package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.io.File;

public class Avatar extends GameObject {

    private static final String PATH_TO_IMG = "clow/claw_art/RUN.GIF";
    private static final String abs ="C:\\Users\\OWNER\\IdeaProjects\\miniGame\\clow\\claw_art\\RUN.GIF";

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
    }

    /**
     * This function creates an avatar that can travel the world and is followed by the camera.
     * The can stand, walk, jump and fly, and never reaches the end of the world.
     * @param gameObjects The collection of all participating game objects.
     * @param layer The number of the layer to which the created avatar should be added.
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Used for reading input from the user.
     * @param imageReader Used for reading images from disk or from within a jar.
     * @return A newly created representing the avatar.
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader){
        ImageRenderable image = new ImageRenderable(Toolkit.getDefaultToolkit().createImage(PATH_TO_IMG));
        ImageRenderable coin = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("clow/claw_art/coin.gif"));

        ImageRenderable img = imageReader.readImage(PATH_TO_IMG, true);
        Avatar avatar = new Avatar(Vector2.ZERO, Vector2.ONES.mult(70), coin);
        gameObjects.addGameObject(avatar, Layer.FOREGROUND +10);

        return avatar;

    }
}
