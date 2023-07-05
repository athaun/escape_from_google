package scenes.android;

import java.util.ArrayList;

import org.joml.Vector2f;
import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.Sprite;
import physics.collision.Collider;
import util.Engine;
import util.Log;
import util.MathUtils;

public class Ghosts {

    Player player;
    ArrayList<GameObject> ghosts = new ArrayList<>();
    Sprite fireBallSprite;
    Dialog dialog;

    public Ghosts (Player player, Dialog dialog) {
        this.player = player;
        this.dialog = dialog;
    }

    public void addObject (GameObject go) {
        ghosts.add(go);
        go.setZIndex(100);
    }

    public ArrayList<GameObject> getObjects () {
        return ghosts;
    }    

    // Strings that will trigger the ghost to attack
    private String[] ghostAttackStrings = {
        /* Ghost 0 */ "In the file '/kernel/barfoo/HelloWorld.c",
        /* Ghost 1 */ "Unable to process request (error 403)", 
        /* Ghost 2 */ "An error has occured at line 42",
        /* Ghost 3 */ "Dumping core..."
    };

    public boolean shouldAttack (int index) {
        return dialog.checks.getSelected().contains(ghostAttackStrings[index]);
    }

    public void update () {
        for (GameObject go : ghosts) {
            if (MathUtils.dist(go.getReadOnlyPosition(), player.getPlayer().getReadOnlyPosition()) < 950) {
                
                Vector2f dir = new Vector2f(player.getPlayer().getReadOnlyPosition()).sub(go.getReadOnlyPosition());
                dir.normalize().mul(450 * Engine.deltaTime());
                
                if (shouldAttack(Integer.parseInt(go.name()))) {                    
                    go.addToPosition(dir);
                    
                    if (go.getComponent(Collider.class).detectCollision(player.getPlayer().getComponent(Collider.class)).collision()) {
                        player.reset();
                    }
                } else {
                    go.addToPosition(dir.negate().div(2));
                }         
                
                if (dir.x < 0) {
                    go.getComponent(SpriteRenderer.class).flip(true);
                } else {
                    go.getComponent(SpriteRenderer.class).flip(false);
                }
            }
        }
    }
}
