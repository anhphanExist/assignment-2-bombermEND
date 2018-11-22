package core.Level;

import core.Rectangle;
import core.RenderHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// The Maze we play
public class Map {

    private Tiles tileSet;
    private int fillTileID = -1;
    private ArrayList<MappedTile> mappedTiles = new ArrayList<>();

    private File mapFile;

    public Map(File mapFile, Tiles tileSet)
    {
        this.mapFile = mapFile;
        this.tileSet = tileSet;
        int rows = 0;
        int cols = 0;
        int curRow = 0;
        try
        {
            Scanner scanner = new Scanner(mapFile);

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                // Check if the line is comment or not
                if(!line.startsWith("//")) {

                    // Read the info of the maze
                    if (!line.contains("#")) {
                        String[] splitString = line.split(" ");
                        rows = Integer.parseInt(splitString[0]);
                        cols = Integer.parseInt(splitString[1]);
                    }
                    else {
                        // Read the maze, # = wall, * = brick, f = item
                        for (int i = 0; i < cols; i++) {
                            if (line.charAt(i) == '#') {
                                mappedTiles.add(new MappedTile(Tiles.WALL_ID, i, curRow, true));
                            } else if (line.charAt(i) == '*') {
                                mappedTiles.add(new MappedTile(Tiles.BRICK_ID, i, curRow, true));
                            } else if (line.charAt(i) == 'f') {
                                mappedTiles.add(new MappedTile(Tiles.ITEM_ID, i, curRow, true));
                            } else {
                                mappedTiles.add(new MappedTile(Tiles.GRASS_ID, i, curRow, false));
                            }
                        }
                        // increase current row (line)
                        curRow++;
                        if (curRow >= rows) {
                            break;
                        }
                    }
                }
            }
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


    public void render(RenderHandler renderer, int xZoom, int yZoom)
    {
        int tileWidth = 16 * xZoom;
        int tileHeight = 16 * yZoom;


        Rectangle camera = renderer.getCamera();
        // Load all the begin tiles with grass
        for(int y = camera.y - tileHeight - (camera.y % tileHeight); y < camera.y + camera.h; y += tileHeight)
        {
            for(int x = camera.x - tileWidth - (camera.x % tileWidth); x < camera.x + camera.w; x+= tileWidth)
            {
                tileSet.renderTile(Tiles.GRASS_ID, renderer, x, y, xZoom, yZoom); // load Grass
            }
        }

        // Load the maze
        for(int tileIndex = 0; tileIndex < mappedTiles.size(); tileIndex++)
        {
            MappedTile mappedTile = mappedTiles.get(tileIndex);
            tileSet.renderTile(mappedTile.id, renderer, mappedTile.x * tileWidth, mappedTile.y * tileHeight, xZoom, yZoom);
        }
    }

    /**
     * Check collision of the main rectangle (check adjacent tile)
     * @param rect
     * @param xZoom
     * @param yZoom
     * @return
     */
    public boolean checkCollision(Rectangle rect, int xZoom, int yZoom) {
        int tileWidth = Level.MATERIALS_SPRITE_SIZE * xZoom;
        int tileHeight = Level.MATERIALS_SPRITE_SIZE * yZoom;

        //Coordinates to check all tiles in a radius of 4 around the rectangle
        int topLeftX = (rect.x - tileWidth) / tileWidth;
        int topLeftY = (rect.y - tileHeight) / tileHeight;
        int bottomRightX = (rect.x + rect.w + tileWidth) / tileWidth;
        int bottomRightY = (rect.y + rect.h + tileHeight) / tileHeight;

        //Starting at the top left tile and going to the bottom right
        for (int x = topLeftX; x < bottomRightX; x++) {
            for (int y = topLeftY; y < bottomRightY; y++) {
                MappedTile tile = getMappedTile(x, y);
                if (tile != null && tile.collidable) {
                    Rectangle tileRectangle = new Rectangle(tile.x * tileWidth, tile.y * tileHeight, tileWidth, tileHeight);
                    if (tileRectangle.intersects(rect))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * get type of Tile at coordinates x,y in the map
     * @param x
     * @param y
     * @return
     */
    public MappedTile getMappedTile(int x, int y) {

        for (MappedTile curMappedTileInMap : this.mappedTiles) {
            if (x == curMappedTileInMap.x && y == curMappedTileInMap.y) {
                return curMappedTileInMap;
            }
        }

        return null;
    }

}
