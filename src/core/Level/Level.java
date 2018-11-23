package core.Level;

import core.Game;
import core.RenderHandler;
import core.animation.SpriteSheet;
import core.character.Player;

import java.io.File;
import java.util.ArrayList;

// The level have the maze we play
public class Level {
    public static final int NUMBER_OF_LEVELS = 1;
    public static final int MATERIALS_SPRITE_SIZE = 16;
    public static final int PLAYER_SPRITE_SIZE = 32;

    private static final String LEVEL1_PATH = "src/core/Level/level1.txt";
    private static final String MATERIALS_PATH = "imageFolder/materials.png";
    private static final String TILES_PATH = "src/core/Level/tiles.txt";

    private int currentLevel = 1;
    private SpriteSheet levelMaterials;
    private Tiles tiles;
    private Map map;

    public Level() {
        try {
            // Get spritesheet
            levelMaterials = new SpriteSheet(Game.loadImage(MATERIALS_PATH));

            // load materials into the map with its sprite size
            levelMaterials.loadSprites(MATERIALS_SPRITE_SIZE, MATERIALS_SPRITE_SIZE);

            // Create new map
            tiles = new Tiles(new File(TILES_PATH), levelMaterials);
            map = new Map(new File(LEVEL1_PATH), tiles);
        }
        catch (NullPointerException e) {
            System.out.println("Load level failed");
            e.printStackTrace();
        }
    }

    public void render(RenderHandler renderer, int xZoom, int yZoom) {
        map.render(renderer, xZoom, yZoom);
    }

    public Map getMap() {
        return map;
    }
}
