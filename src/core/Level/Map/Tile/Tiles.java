package core.Level.Map.Tile;

import core.RenderHandler;
import core.animation.SpriteSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Store data of the type of sprites available
public class Tiles {
    public static final int GRASS_ID = 0;
    public static final int WALL_ID = 1;
    public static final int BRICK_ID = 2;
    public static final int ITEM_ID = 3;

    private SpriteSheet spriteSheet;
    private ArrayList<Tile> tilesList = new ArrayList<>();

    // This will only work assuming the sprites in the spriteSheet have loaded
    public Tiles(File tilesFile, SpriteSheet spriteSheet) {

        this.spriteSheet = spriteSheet;

        try {
            Scanner scanner = new Scanner(tilesFile);
            while (scanner.hasNextLine()) {

                // Read each line in the tilesFile

                String line = scanner.nextLine();
                if (!line.startsWith("//")) {
                    String[] splitString = line.split("-");
                    String tileName = splitString[0];
                    int spriteX = Integer.parseInt(splitString[1]);
                    int spriteY = Integer.parseInt(splitString[2]);
                    boolean collidable = Boolean.parseBoolean(splitString[3]);
                    Tile tile = new Tile(tileName, spriteSheet.getSprite(spriteX, spriteY), collidable);
                    tilesList.add(tile);
                }
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void renderTile(int tileID, RenderHandler renderer, int xPosition, int yPosition, int xZoom, int yZoom) {
        if (tileID >= 0 && tilesList.size() > tileID) {
            renderer.renderSprite(tilesList.get(tileID).sprite, xPosition, yPosition, xZoom, yZoom);
        }
        else {
            throw new ArrayIndexOutOfBoundsException("TileID " + tileID + " out of range" + tilesList.size());
        }
    }
}
