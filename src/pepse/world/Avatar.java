package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.ImageRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;


public class Avatar extends GameObject {

    private static final String PATH_TO_IMG_RIGHT = "src/pepse/assets/claw_art/RUN.GIF";
    private static final String PATH_TO_IMG = "pepse/assets/claw_art/PUNCH.gif";
    private static final String ENERGY_IMG = "pepse/assets/claw_art/Light.jpg";
    private static final String TREASURE_IMG = "pepse/assets/claw_art/TREASURE.gif";
    private static final String CLAW_LIFE_IMG = "pepse/assets/claw_art/CLAWLIFE.gif";
    private static final String CLAW_PUNCH_IMG = "pepse/assets/claw_art/punch1.gif";
    private static final Vector2 SIZE_OF_TEXT =new Vector2(30,30);
    private static final Vector2 SIZE_OF_IMG = new Vector2(SIZE_OF_TEXT.y(),SIZE_OF_TEXT.y());
    private static final float MOVEMENTS_SPEED = 400;
    private static final int IMAGE_SIZE =Terrain.GROUND_SIZE;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final ImageRenderable image_right;
    private final ImageRenderable img;
    private final Counter energyCounter;
    private final Counter counter_treasure;
    private final Counter counter_life;
    private final GameObjectCollection gameObjects;
    private static final int ENERGY =200;
    private static final int LIFE =2;
    private static final int TREASURE =0;
    private final ImageRenderable punch;
    private GameObject counterTreasureObj;
    private GameObject counterEnergyObj;
    private GameObject counterLifeObj;
    private boolean wait =false;

    /**
     * constructor of avatar obj
     * @param topLeftCorner The location of the top-left corner of the created avatar.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether a given key is
     *                     currently pressed by the user or not. See its documentation.
     * @param imageReader Used for reading images from disk or from within a jar.
     * @param gameObjects The collection of all participating game objects.
     */
    Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader,GameObjectCollection gameObjects,int layer) {
        super(topLeftCorner, Vector2.ONES.mult(IMAGE_SIZE),
                imageReader.readImage(PATH_TO_IMG, true));
        this.inputListener =inputListener;
        this.imageReader =imageReader;
        this.gameObjects=gameObjects;

        image_right = new ImageRenderable(Toolkit.getDefaultToolkit().createImage(PATH_TO_IMG_RIGHT));
        img = imageReader.readImage(PATH_TO_IMG, true);
        this.punch = imageReader.readImage(CLAW_PUNCH_IMG, true);
        transform().setAccelerationY(5000);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.counter_life =new Counter(LIFE);
        this.energyCounter = new Counter(ENERGY);
        this.counter_treasure = new Counter(TREASURE);
        setCounters(imageReader,layer-1);

    }


    /**
     * to be call on init. create all the counters on screen.
     * @param imageReader Used for reading images from disk or from within a jar.
     */
    private void setCounters(ImageReader imageReader,int layer){
        GameObject img = new GameObject(new Vector2(0,SIZE_OF_TEXT.y()),SIZE_OF_IMG
                ,imageReader.readImage(TREASURE_IMG,true));
        img.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(img,layer);
        this.counterTreasureObj =new GameObject(new Vector2(SIZE_OF_TEXT.y(),SIZE_OF_TEXT.y()),
                                SIZE_OF_TEXT,new TextRenderable(Integer.toString(TREASURE)));
        counterTreasureObj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(counterTreasureObj,layer);


        this.counterEnergyObj =new GameObject(new Vector2(SIZE_OF_TEXT.x(),0),SIZE_OF_TEXT,
                        new TextRenderable(Integer.toString(ENERGY)));
        GameObject sImg = new GameObject(Vector2.ZERO,SIZE_OF_IMG,
                imageReader.readImage(ENERGY_IMG,true));
        sImg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sImg,layer);
        counterEnergyObj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(counterEnergyObj,layer);

        this.counterLifeObj =new GameObject(new Vector2(SIZE_OF_TEXT.x(),SIZE_OF_TEXT.y()*2),
                SIZE_OF_TEXT,new TextRenderable(Integer.toString(LIFE)));
        counterLifeObj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(counterLifeObj,layer);
        GameObject tImg =new GameObject(new Vector2(0,SIZE_OF_TEXT.y()*2),SIZE_OF_IMG,
                imageReader.readImage(CLAW_LIFE_IMG,true));
        tImg.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(tImg,layer);



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
        Avatar avatar = new Avatar(topLeftCorner,inputListener,imageReader,gameObjects, layer);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * update the location of avatar
     * @param deltaTime deltaTime
     */

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movement = Vector2.ZERO;
        renderer().setRenderable(img);
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movement = movement.add(Vector2.LEFT);
            renderer().setRenderable(image_right);
            renderer().setIsFlippedHorizontally(true);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movement = movement.add(Vector2.RIGHT);
            renderer().setIsFlippedHorizontally(false);
            renderer().setRenderable(image_right);
        }
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() ==0f){
            movement = movement.add(Vector2.UP).mult(MOVEMENTS_SPEED/12);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && energyCounter.value()>0){
            movement = movement.add(Vector2.UP);
            energyCounter.decrement();
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_CONTROL)){
            renderer().setRenderable(punch);
        }
        movement= movement.mult(MOVEMENTS_SPEED);
        setVelocity(movement);
        updateCounters();
    }

    /**
     *  update the counters.
     *  if avatar on ground it increase the energyCounter, if energyCounter less than zero is decrease life.
     */
    private void updateCounters(){
        if(getVelocity().y() == 0f){
            energyCounter.increment();
        }
        if(energyCounter.value() <0){

            if(!wait) {
                counter_life.decrement();
                wait = !wait;
            }

        }
        else{wait = false;}
        counterTreasureObj.renderer().setRenderable(new TextRenderable(
                Integer.toString(counter_treasure.value())));
        counterEnergyObj.renderer().setRenderable(new TextRenderable(Integer.toString(energyCounter.value())));
        counterLifeObj.renderer().setRenderable(new TextRenderable(Integer.toString(counter_life.value())));

    }

    /**
     * check if we have collision with enemy. if so, its decrease the energy,
     * until that collision spot or that enemy was killed by pushing on control bottom
     * @param other other game object
     * @param collision collision
     */
    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        if( other instanceof Enemy) {
            energyCounter.decrement();
            if (inputListener.isKeyPressed(KeyEvent.VK_CONTROL)) {
                TreasureFactory.create(gameObjects,other.getCenter(),counter_treasure);
                gameObjects.removeGameObject(other);
            }
        }
    }

    /**
     * getter method
     * @return the sum of treasure that collected
     */
    public int getTreasure(){
        return counter_treasure.value();
    }

    /**
     * getter method
     * @return the number of life
     */
    public int getLife(){
        return counter_life.value();
    }


}


