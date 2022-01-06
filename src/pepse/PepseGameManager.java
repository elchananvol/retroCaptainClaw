package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.Avatar;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 40);
    private static final int DEY_NIGHT_CYCLE_LENGTH = 30;
    private static final String PATH_TO_NUSIC = "pepse/assets/clawmusic/Level_1.wav";
    private static final Random random = new Random();
    private static final int INITIAL_AVATAR_LOCATION_X = 0;
    private static final float WINDOW_SIZE = 1.5f;
    private static final String BACKGROUND_IMG = "pepse/assets/claw_art/level6.jpg";


    pepse.world.trees.Tree trees;
    Avatar avatar;
    private Vector2 windowDimensions;
    private int lastLocationOfAvatar = 0;
    private Terrain terrain;
    private EnemyFactory enmeis;
    private Counter counter_life;
    private Counter counter_energy;
    private Counter counter_treasure;
    private GameObject text_obj;
    private static final Vector2 SIZE_OF_TEXT =new Vector2(30,40);

    PepseGameManager() {
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        this.counter_life =new Counter(2);
        this.counter_energy = new Counter(200);
        this.counter_treasure = new Counter(0);
        TextRenderable text = new TextRenderable("");
        this.text_obj =new GameObject(new Vector2(windowDimensions.x()- SIZE_OF_TEXT.x()*7,0), SIZE_OF_TEXT,text);
        text_obj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(text_obj,Integer.MAX_VALUE -1);
        Sound sound = soundReader.readSound(PATH_TO_NUSIC);
        sound.playLooped();

        GameObject background = new GameObject(Vector2.ZERO,windowDimensions.subtract(Vector2.ONES),imageReader.readImage(BACKGROUND_IMG,true));
        gameObjects().addGameObject(background,Layer.BACKGROUND-1);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        GameObject sky = Sky.create(gameObjects(), new Vector2(windowDimensions.x(), windowDimensions.y() / 5), Layer.BACKGROUND);
        GameObject night = Night.create(gameObjects(), Integer.MAX_VALUE-1, windowDimensions, DEY_NIGHT_CYCLE_LENGTH);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND , windowDimensions, DEY_NIGHT_CYCLE_LENGTH);
        GameObject sunHalo = SunHalo.create(gameObjects(), Layer.BACKGROUND , sun, SUN_HALO_COLOR);

        this.terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), 10);

        this.trees = new pepse.world.trees.Tree(gameObjects(), Layer.STATIC_OBJECTS + 1, random.nextInt(), terrain);

        Vector2 initialAvatarLocation = new Vector2(INITIAL_AVATAR_LOCATION_X, terrain.groundHeightAt(INITIAL_AVATAR_LOCATION_X) - Avatar.IMAGE_SIZE);
        this.avatar = Avatar.create(gameObjects(), Integer.MAX_VALUE, initialAvatarLocation, inputListener, imageReader,counter_energy,counter_treasure);
        terrain.createInRange((int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * -WINDOW_SIZE), (int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * WINDOW_SIZE));
        trees.createInRange((int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * -WINDOW_SIZE), (int) (INITIAL_AVATAR_LOCATION_X + windowDimensions.x() * WINDOW_SIZE));
        this.gameObjects().layers().shouldLayersCollide(Integer.MAX_VALUE, Layer.STATIC_OBJECTS, true);
//        this.gameObjects().layers().shouldLayersCollide(Integer.MAX_VALUE, Layer.STATIC_OBJECTS + 1, true);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
        this.enmeis= new EnemyFactory(gameObjects(),Layer.UI,terrain,random.nextInt());
        enmeis.createInRange((int)(INITIAL_AVATAR_LOCATION_X + windowDimensions.x()*-WINDOW_SIZE),(int)(INITIAL_AVATAR_LOCATION_X +windowDimensions.x()*WINDOW_SIZE));


                String solider_path = "pepse/assets/enemy/mercat.gif";
        Renderable r =imageReader.readImage(solider_path,false);
        enemy s = new enemy(Vector2.ZERO,Vector2.ONES.mult(Avatar.IMAGE_SIZE),r,terrain,400);
        gameObjects().addGameObject(s,Layer.UI);
        this.gameObjects().layers().shouldLayersCollide(Layer.UI, Layer.STATIC_OBJECTS, true);
        this.gameObjects().layers().shouldLayersCollide(Layer.UI, Integer.MAX_VALUE, true);
        this.gameObjects().layers().shouldLayersCollide(Layer.DEFAULT, Integer.MAX_VALUE, true);

//        float location = avatar.getTopLeftCorner().x();
//        lastLocationOfAvatar = windowDimensions.x() * 10;
//        update(1f);
//        lastLocationOfAvatar = -windowDimensions.x() * 10;
//        update(1f);
//        trees.createInRange((int) (location-windowDimensions.x()*1.5),(int) (location+windowDimensions.x()*1.5));


//        int x=(int) Math.floor((double) -50 / (Block.SIZE*3)) * Block.SIZE*3;
//        int z= (int) Math.floor((double) 50 / (Block.SIZE*3)) * Block.SIZE*3;
//        System.out.printf("%d,%d",x,z);
//        Random a = new Random(30);
//        Random b = new Random(30);
//        System.out.println(a.nextInt());
//        System.out.println(b.nextInt());
//        System.out.println(a.nextInt());
//        System.out.println(b.nextInt());


    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
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
                enmeis.createInRange(rightMin, rightMAX);
                enmeis.deleteInRange(leftMin, leftMax);
            } else {
                trees.deleteInRange(rightMAX, rightMin);
                trees.createInRange(leftMax,leftMin);
                terrain.deleteInRange(rightMAX, rightMin);
                terrain.createInRange(leftMax, leftMin);
                enmeis.deleteInRange(rightMAX, rightMin);
                enmeis.createInRange(leftMax, leftMin);


            }

//        trees.createInRange((int) (location-windowDimensions.x()*1.5),(int) (location+windowDimensions.x()*1.5));


            lastLocationOfAvatar = location;
            text_obj.renderer().setRenderable(new TextRenderable("treasure: " +Integer.toString(counter_treasure.value())));
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }

}

