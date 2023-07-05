package scenes.android;

import ecs.GameObject;
import ecs.*;
import graphics.Color;
import graphics.Spritesheet;
import input.Keyboard;
import input.Keys;

import java.util.Arrays;
import org.joml.Vector2f;
import util.Log;

import physics.collision.Shapes;
import scenes.android.Player;

public class Player {
    
    private GameObject player;
    private SpriteRenderer playerRenderer;
    private SpriteAnimation playerAnimation;
    private Spritesheet a;

    private boolean reset = false;

    public Player (Spritesheet a) {
        this.a = a;
        // Create the gameObject for the player
        player = new GameObject(new Vector2f(600, 1000), 1);

        // Add a light
        player.addComponent(new PointLight(new Color(250, 255, 181), 30));

        // Add a polygon collider
        PolygonCollider playerBody = new PolygonCollider(Shapes.axisAlignedRectangle(0, 0, 100, 100)).layer(2).mask(2);
        player.addComponent(playerBody);
        player.addComponent(CollisionHandlers.unpassablePolygonCollider(playerBody));

        // Set up the sprite renderer and animations
        playerRenderer = new SpriteRenderer(a.getSprite(0), new Vector2f(40 * 8, 29 * 8));
        playerRenderer.setOffset(new Vector2f(-100, -100));
        playerAnimation = new SpriteAnimation(playerRenderer, a.getSprite(2), 1);

        // Define the animations
        playerAnimation.setAnimation("idle", Arrays.asList(a.getSprite(21), a.getSprite(0), a.getSprite(7)));
        playerAnimation.setAnimation("walk", Arrays.asList(a.getSprite(9), a.getSprite(4), a.getSprite(8), a.getSprite(8)));
        playerAnimation.setAnimation("surprised!", Arrays.asList(a.getSprite(12)));

        player.addComponent(playerRenderer);
        player.addComponent(playerAnimation);

        // Add the character controller
        Dynamics dynamics = new Dynamics();
        player.addComponent(dynamics);
        player.addComponent(CharacterController.standardTopDown(dynamics, 250));
    }

    private boolean animationLock = false;
    public void update () {
        
        if (Keyboard.keyHeld(Keys.KEY_W) || Keyboard.keyHeld(Keys.KEY_A) || Keyboard.keyHeld(Keys.KEY_S) || Keyboard.keyHeld(Keys.KEY_D)) {
            playerAnimation.nextAnimation("walk", 0);
            playerAnimation.setTimePerSprite(0.3f);

            if (Keyboard.getKeyDown(Keys.KEY_SPACE)) {
                playerAnimation.nextAnimation("surprised!", 0);
                animationLock = false;
            }   

            if (!animationLock) {
                playerAnimation.switchAnimation(true);
            }
            animationLock = true;     
            
            if (Keyboard.keyHeld(Keys.KEY_A)) {
                playerRenderer.flip(true);
            }
            if (Keyboard.keyHeld(Keys.KEY_D)) {
                playerRenderer.flip(false);
            }
        } else {
            playerAnimation.nextAnimation("idle", 0);
            playerAnimation.setTimePerSprite(1.0f);
            
            if (Keyboard.getKeyDown(Keys.KEY_SPACE)) {
                playerAnimation.nextAnimation("surprised!", 0);
                animationLock = true;
            }   
            if (animationLock) {
                playerAnimation.switchAnimation(true);
            }
            animationLock = false;
        }            

        if (reset) {
            player.setPosition(new Vector2f(600, 1000));
            reset = false;
        }
    }

    public void reset () {
        reset = true;
    }

    public GameObject getPlayer() {
        return player;
    }
}
