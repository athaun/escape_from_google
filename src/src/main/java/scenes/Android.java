package scenes;

import ecs.*;
import graphics.Camera;
import graphics.Color;
import graphics.Sprite;
import graphics.Spritesheet;
import graphics.Texture;
import graphics.postprocess.BloomEffect;
import graphics.postprocess.PostProcessStep;
import input.Keyboard;
import input.Keys;

import org.joml.Vector2f;
import physics.collision.Shapes;
import scene.Scene;
import tiles.Tilesystem;
import util.Assets;
import util.Engine;
import util.Log;
import util.MathUtils;
import scenes.android.*;
import java.util.Arrays;

import static graphics.Graphics.setDefaultBackground;

public class Android extends Scene {

    Spritesheet androidCharacter;
    Tilesystem tilesystem;

    PointLight booperLight;

    Player player;
    GameObject greenLight;

    Dialog dialog;
    Ghosts ghosts;
    BloomEffect bloom;

    boolean flip = true;

    public static void main(String[] args) {
        Log.setLogLevel(Log.ALL);
        Engine.init("Adventures in Google", 0.2f);
        Engine.scenes().switchScene(new Android());
        Engine.showWindow();
    }

    public void awake() {
        camera = new Camera();
        setDefaultBackground(0);

        androidCharacter = new Spritesheet(Assets.getTexture("src/assets/images/Android.png"), 40, 29, 36, 1);

        player = new Player(androidCharacter);
        dialog = new Dialog(player);
        ghosts = new Ghosts(player, dialog);
        
        tilesystem = new Tilesystem("src/assets/tiles/android-world.tmx", 200, 200, dialog, ghosts);

        bloom = new BloomEffect(PostProcessStep.Target.DEFAULT_FRAMEBUFFER, 0.3f);
    }

    public void update() {
        super.update();
        if (Keyboard.getKey(Keys.KEY_ESCAPE)) {
            System.exit(0);
        }

        player.update();
        ghosts.update();
        dialog.update();

        player.getPlayer().getComponent(PointLight.class).intensity = MathUtils.map((float) Math.sin(Engine.millisRunning() / 600), -1, 1, 100, 140);

        camera.smoothFollow(player.getPlayer().getReadOnlyPosition());
    }

    @Override
    public void postProcess(Texture texture) {
        bloom.apply(texture);
    }
}
