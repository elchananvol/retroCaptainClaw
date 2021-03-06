package pepse;



import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.Avatar;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.Random;

/**
 * present the main manager of the game
 */
public class PepseGameManager extends GameManager {
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 40);
    private static final int DEY_NIGHT_CYCLE_LENGTH = 40;
    private static final String PATH_TO_MUSIC = "pepse/assets/claw_art/Level_1.wav";
    private static final Random random = new Random();
    private static final int INITIAL_AVATAR_LOCATION_X = 0;
    private static final float WINDOW_SIZE = 1.5f;
    private static final String BACKGROUND_IMG = "pepse/assets/claw_art/level6.jpg";
    private static final int LAYER_OF_AVATAR = Integer.MAX_VALUE;
    private static final int WIN_SIZE = 1000000;
    private Tree trees;
    private Avatar avatar;
    private Vector2 windowDimensions;
    private int lastLocationOfAvatar = 0;
    private Terrain terrain;
    private EnemyFactory enemies;
    private WindowController windowController;


    /**
     * The method will be called once when a GameGUIComponent is created, and again after every invocation
     * of windowController.resetGame().
     * initializeGame in class danogl.GameManager
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                   See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     *                   See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether a given key is currently
     *                     pressed by the user or not. See its documentation.
     * @param windowController Contains an array of helpful, self explanatory methods concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.windowController=windowController;
        createBackground(imageReader,soundReader);

        this.terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowDimensions, random.nextInt());
        this.trees = new pepse.world.trees.Tree(
                gameObjects(), Layer.STATIC_OBJECTS - 1, random.nextInt(), terrain);
        this.enemies = new EnemyFactory(gameObjects(),terrain,random.nextInt());
        Vector2 initialAvatarLocation = new Vector2(INITIAL_AVATAR_LOCATION_X,
                terrain.groundHeightAt(INITIAL_AVATAR_LOCATION_X) - Terrain.GROUND_SIZE);
        this.avatar = Avatar.create(gameObjects(), LAYER_OF_AVATAR, initialAvatarLocation, inputListener, imageReader);
        this.gameObjects().layers().shouldLayersCollide(LAYER_OF_AVATAR, Layer.STATIC_OBJECTS, true);
        this.gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, LAYER_OF_AVATAR, true);

        int minRangeAtInit = (int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * -WINDOW_SIZE);
        int maxRangeAtInit = (int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * WINDOW_SIZE);
        terrain.createInRange(minRangeAtInit, maxRangeAtInit);
        trees.createInRange(minRangeAtInit, maxRangeAtInit);
        enemies.createInRange(minRangeAtInit, maxRangeAtInit);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    /**
     * create background image, sun and sun halo, night, and play music.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     * @param soundReader Contains a single method: readSound, which reads a wav file from disk.
     */
    private void createBackground(ImageReader imageReader, SoundReader soundReader) {
        Sound sound = soundReader.readSound(PATH_TO_MUSIC);
        sound.playLooped();
        GameObject background = new GameObject(Vector2.ZERO,windowDimensions.subtract(Vector2.ONES),
                        imageReader.readImage(BACKGROUND_IMG,true));
        gameObjects().addGameObject(background,Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        // this line do nothing because the sky is in the same color of background img
        Sky.create(gameObjects(), new Vector2(windowDimensions.x(), windowDimensions.y() / 5)
                , Layer.BACKGROUND-1);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND+1 , windowDimensions, DEY_NIGHT_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), Layer.BACKGROUND+1 , sun, SUN_HALO_COLOR);
        Night.create(gameObjects(), LAYER_OF_AVATAR-2, windowDimensions, DEY_NIGHT_CYCLE_LENGTH);

    }

    /**
     * check if end game.
     * win in game if treasures that collected is more than 1,000,000
     * lose in game if life is zero.
     */
    private void checkEndGame() {
        String prompt ="";
            if (avatar.getLife()<= 0) {
                prompt += "you lose";
        }
        if (avatar.getTreasure() >= WIN_SIZE) {
            prompt += "you win";
        }
        if (!prompt.isEmpty()) {
            prompt += ", play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }

    }

    /**
     * check if game over and update all game object so that will contain just object in window*1.5
     * to direction right and left.
     * @param deltaTime deltaTime (automatically)
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkEndGame();
        int location = (int) avatar.getTopLeftCorner().x();
        if (Math.abs(location - lastLocationOfAvatar) >= windowDimensions.x()) {
            int windowSize = (int)(windowDimensions.x()*WINDOW_SIZE);
            int rightMin =  lastLocationOfAvatar + windowSize;
            int rightMAX = location + windowSize;
            int leftMin=lastLocationOfAvatar - windowSize;
            int leftMax =location - windowSize;
            if (location > lastLocationOfAvatar) {
                trees.createInRange(rightMin, rightMAX);
                trees.deleteInRange(leftMin, leftMax);
                terrain.createInRange(rightMin, rightMAX);
                terrain.deleteInRange(leftMin, leftMax);
                enemies.createInRange(rightMin, rightMAX);
                enemies.deleteInRange(leftMin, leftMax);
            } else {
                trees.deleteInRange(rightMAX, rightMin);
                trees.createInRange(leftMax,leftMin);
                terrain.deleteInRange(rightMAX, rightMin);
                terrain.createInRange(leftMax, leftMin);
                enemies.deleteInRange(rightMAX, rightMin);
                enemies.createInRange(leftMax, leftMin);


            }
            lastLocationOfAvatar = location;
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

