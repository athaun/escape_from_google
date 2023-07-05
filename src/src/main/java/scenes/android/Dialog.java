package scenes.android;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.lwjgl.system.MathUtil;

import ecs.GameObject;
import ecs.SpriteRenderer;
import graphics.Color;
import graphics.Sprite;
import graphics.Spritesheet;
import graphics.Texture;
import graphics.Window;
import ui.EventHandler;
import ui.Frame;
import ui.Layer;
import ui.Text;
import ui.element.Button;
import ui.element.CheckBox;
import ui.element.CheckBoxGroup;
import ui.fonts.Font;
import util.Engine;
import util.Log;
import util.MathUtils;

public class Dialog {

    Player player;
    ArrayList<GameObject> dialogObjects = new ArrayList<>();

    Text text;
    Font f;

    Layer menu;
    CheckBoxGroup checks;
    Text description;
    Sprite check, x_mark;

    Button restart;

    public List<String> checkOptions = new ArrayList<>();;
    public String[] dialogs = {
        /* 0            */ "A great error has occured! If it isn't solved, society will collapse.\nIt is your responsibility to find the solution by traversing the halls of Google.\n\n\nChoose your deck wisely.",
        /* 1 ghost 0    */ "Your error is too cluttered!\nThe Ghost of File Paths will not let you pass.\nDrop any cards that have file paths in them.",
        /* 2            */ "Choose your path wisely.\nThis will decide your fate.\n",
        /* 3 ghost 1    */ "This is the bottomless pit of results.\nThe ghost of vague error messages welcomes you.",
        /* 4 ghost 2    */ "The ghost of line numbers haunts these halls.\n",
        /* 5 ghost 3    */ "This ghost will dump your core :)\n",
        /* 6            */ "lol\n",
        /* 7            */ "Congratulations, you have successfully made it to the end of the maze!\nYou have delivered the error to the control room, society will stand!\n",
    };
    
    public Dialog (Player player) {
        this.player = player;

        check = new Sprite(new Texture("src/assets/images/c.png"));
        x_mark = new Sprite(new Texture("src/assets/images/x.png"));
        
        checkOptions.add("An error has occured at line 42");
        checkOptions.add("In the file '/kernel/barfoo/HelloWorld.c");
        checkOptions.add("Unable to process request (error 403)");
        checkOptions.add("Dumping core...");

        checks = new CheckBoxGroup(CheckBox.Type.MULTI_SELECT, checkOptions, x_mark, check, new Vector2f(60, 60));
        description = new Text("This is your error deck, select or deselect items to pass ghosts.", Color.WHITE, 10, 30);

        f = new Font("src/assets/fonts/joystix.ttf", Window.getWidth() / 80, true);

        text = new Text("Hello World!", f, Color.WHITE, Window.getWidth() / 2, Window.getHeight() - 300, 1, true, true);

        restart = new Button("Restart", new Color(60), Color.WHITE, new Frame(-1000, -1000, 1, 1));
        restart.tintColor = new Color(90).toNormalizedVec4f();

        restart.getEventHandler().registerListener(EventHandler.Event.MOUSE_CLICK, e -> {
            player.reset();
        });
        Engine.scenes().currentScene().addUIElement(restart);        
    }

    public void addObject (GameObject go) {
        dialogObjects.add(go);
    }

    public ArrayList<GameObject> getObjects () {
        return dialogObjects;
    }

    public void update () {
        String s = "";
        for (GameObject go : dialogObjects) {
            if (MathUtils.dist(go.getReadOnlyPosition(), player.getPlayer().getReadOnlyPosition()) < 250) {
                s += dialogs[Integer.parseInt(go.name())];
                if (go.name().equals("7") && !checks.getSelected().contains("Unable to process request (error 403)")) {
                    s = "";
                    s += "You made it to the control room.\nHowever you have failed to deliver the error.\n\nSociety will still collapse.\n";
                } else if (go.name().equals("7")) {
                    s += "\nPress ESC to exit the game.\n";
                }

                if (go.name().equals("7")) {
                    restart.setFrame(new Frame(Window.getWidth() / 2 - 282 / 2, Window.getHeight() - 450, 282, 53));
                    restart.setRenderFrame(new Frame(Window.getWidth() / 2 - 282 / 2, Window.getHeight() - 450, 282, 53));
                }
            } else {
                restart.setFrame(new Frame(-1000, -1000, 1, 1));
                restart.setRenderFrame(new Frame(-1000, -1000, 1, 1));
            }
        }
        text.change(s);
    }
}
