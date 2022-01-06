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


import java.awt.*;
import java.awt.event.KeyEvent;


public class Avatar extends GameObject {

    private static final String PATH_TO_IMG_RIGHT = "src/pepse/assets/claw_art/RUN.GIF";
    private static final String PATH_TO_IMG = "pepse/assets/claw_art/PUNCH.gif";
    private static final float MOVEMENTS_SPEED = 400;
    public static final int IMAGE_SIZE =150;
    private final UserInputListener inputListener;
    private final ImageReader imageReader;
    private final ImageRenderable image_right;
    private final ImageRenderable img;
    private final GameObject text_obj;
    private final GameObjectCollection gameObjects;
    private final Counter energyCounter;
    private final Counter counter_treasure;
    private boolean wait =false;

    public Avatar(Vector2 topLeftCorner, Vector2 dimensions,
                  UserInputListener inputListener,
                  ImageReader imageReader,GameObjectCollection gameObjects,Counter counter,Counter counter_treasure) {

        super(topLeftCorner, dimensions, imageReader.readImage(PATH_TO_IMG, true));
        this.inputListener =inputListener;
        this.imageReader =imageReader;

        image_right = new ImageRenderable(Toolkit.getDefaultToolkit().createImage(PATH_TO_IMG_RIGHT));
        img = imageReader.readImage(PATH_TO_IMG, true);
        transform().setAccelerationY(5000);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);

        TextRenderable text = new TextRenderable("");
        this.text_obj =new GameObject(Vector2.ZERO,new Vector2(30,40),text);
        text_obj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        this.gameObjects =gameObjects;
        this.gameObjects.addGameObject(text_obj,Integer.MAX_VALUE -1);
        this.energyCounter =counter;
        this.counter_treasure= counter_treasure;





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
                                ImageReader imageReader, Counter energy_counter,Counter counter_treasure){

//        ImageRenderable image = new ImageRenderable(Toolkit.getDefaultToolkit().createImage(PATH_TO_IMG_LEFT));
//        ImageRenderable coin = new ImageRenderable(Toolkit.getDefaultToolkit().createImage("clow/claw_art/coin.gif"));
//        ImageRenderable img = imageReader.readImage(PATH_TO_IMG_RIGHT, true);
        Avatar avatar = new Avatar(topLeftCorner, Vector2.ONES.mult(IMAGE_SIZE),inputListener,imageReader,gameObjects,energy_counter,counter_treasure);
        gameObjects.addGameObject(avatar, layer);
        return avatar;

    }



    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movement = Vector2.ZERO;
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
        else if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().x() ==0){
            movement = movement.add(Vector2.UP);
            renderer().setRenderable(img);
        }


        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && inputListener.isKeyPressed(KeyEvent.VK_SHIFT) && energyCounter.value()>0){

            movement = movement.add(Vector2.UP);
            energyCounter.decrement();
//            renderer().setRenderable(img);

        }
        else {
            energyCounter.increment();
        }


        movement= movement.mult(MOVEMENTS_SPEED);
        setVelocity(movement);
        text_obj.renderer().setRenderable(new TextRenderable("energy: " +Integer.toString(energyCounter.value())));


//        setVelocity(movement.mult(MOVEMENTS_SPEED));
    }

    @Override
    public void onCollisionStay(GameObject other, Collision collision) {
        if( other instanceof enemy) {
            enemy otherr = (enemy) other;
            energyCounter.decrement();

            if (inputListener.isKeyPressed(KeyEvent.VK_CONTROL)) {
                TreasureFactory.create(gameObjects,other.getCenter(),counter_treasure);
                gameObjects.removeGameObject(other, Layer.UI);

            }
        }
    }


}


