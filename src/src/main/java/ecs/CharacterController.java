package ecs;

import input.Keyboard;
import input.Keys;
import org.joml.Vector2f;
import physics.force.Force;
import util.Engine;

/**
 * Character controllers built to support the Top down and Side scroller Demo scenes.
 */
public class CharacterController extends Component {

    private final float speedModifier;
    private final Force playerInputForce;

    private CharacterController(float speedModifier, Force playerInputForce) {
        super(ComponentOrder.INPUT);
        this.playerInputForce = playerInputForce;
        this.speedModifier = speedModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public Force getPlayerInputForce() {
        return playerInputForce;
    }

    public static CharacterController standardPlatformer(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier);
                //nothing on down input
                if (left()) direction.add(-speedModifier, 0);
                if (right()) direction.add(speedModifier, 0);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    public static CharacterController standardTopDown(Dynamics dynamics, float speedModifier) {
        Force f = new Force() {
            private final Vector2f direction = new Vector2f(0, 0);

            @Override
            public String identifier() {
                return "GodlikePlayerInput";
            }

            @Override
            public boolean update(float dt) {
                direction.set(0, 0);
                if (up()) direction.add(0, -speedModifier * dt);
                if (down()) direction.add(0, speedModifier * dt);
                if (left()) direction.add(-speedModifier * dt, 0);
                if (right()) direction.add(speedModifier * dt, 0);
                // if (Keyboard.keyHeld(Keys.KEY_LEFT_SHIFT))
                //     direction.mul(4);

                direction.mul(2);
                return true;
            }

            @Override
            public Vector2f direction() {
                return direction;
            }
        };
        dynamics.applyForce(f);
        return new CharacterController(speedModifier, f);
    }

    private static boolean up() {
        return Keyboard.keyHeld(Keys.KEY_UP) || Keyboard.keyHeld(Keys.KEY_W);
    }

    private static boolean down() {
        return Keyboard.keyHeld(Keys.KEY_DOWN) || Keyboard.keyHeld(Keys.KEY_S);
    }

    private static boolean left() {
        return Keyboard.keyHeld(Keys.KEY_LEFT) || Keyboard.keyHeld(Keys.KEY_A);
    }

    private static boolean right() {
        return Keyboard.keyHeld(Keys.KEY_RIGHT) || Keyboard.keyHeld(Keys.KEY_D);
    }
}
