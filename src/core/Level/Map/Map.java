package core.Level.Map;

import core.Game;
import core.Level.Level;
import core.Level.Map.Tile.Tiles;
import core.Rectangle;
import core.RenderHandler;
import core.animation.GameObject;
import core.character.Bomb;
import core.character.Enemy.Enemy;
import core.character.Enemy.Ghost;
import core.character.Enemy.Water;
import core.character.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

// The Maze we play
public class Map {

    private Tiles tileSet;
    private int fillTileID = -1;
    private ArrayList<MappedTile> mappedTiles = new ArrayList<>();

    private ArrayList<GameObject> gameObjects = new ArrayList<>();
    private Player player = new Player(6);
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<Brick> bricks = new ArrayList<>();

    private File mapFile;

    public Map(File mapFile, Tiles tileSet)
    {
        this.mapFile = mapFile;
        this.tileSet = tileSet;
        int rows = 0;
        int cols = 0;
        int curRow = 0;
        int enemyID = 0;
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
                            // Read the character in the level txt file
                            if (line.charAt(i) == '#') {
                                mappedTiles.add(new MappedTile(Tiles.WALL_ID, i, curRow, true));
                            } else if (line.charAt(i) == '*') {
                                mappedTiles.add(new MappedTile(Tiles.GRASS_ID, i, curRow, false));
                                Brick newBrick = new Brick(i, curRow, tileSet);
                                bricks.add(newBrick);
                                gameObjects.add(newBrick);
                            } else if (line.charAt(i) == 'f') {
                                mappedTiles.add(new MappedTile(Tiles.ITEM_ID, i, curRow, true));
                            } else if (line.charAt(i) == 'p'){
                                // At the location of gameObject need to add grass as default
                                mappedTiles.add(new MappedTile(Tiles.GRASS_ID, i, curRow, false));
                                gameObjects.add(player);
                            } else if (line.charAt(i) == '1') {
                                // At the location of gameObjects need to add grass as default
                                mappedTiles.add(new MappedTile(Tiles.GRASS_ID, i, curRow, false));
                                // Set mapped enemy base on txt file
                                Enemy enemy;
                                int k = ThreadLocalRandom.current().nextInt(1,3);
                                if (k % 2 == 0) {
                                    enemy = new Ghost(i, curRow);
                                }
                                else {
                                    enemy = new Water(i, curRow);
                                }
                                enemy.setEnemyID(enemyID);
                                enemyID++;
                                enemies.add(enemy);
                                gameObjects.add(enemy);
                            } else {
                                // All other location is grass
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
        int tileWidth = Level.MATERIALS_SPRITE_SIZE * xZoom;
        int tileHeight = Level.MATERIALS_SPRITE_SIZE * yZoom;


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

        // Load game objects in the maze
        for (int i = 0; i < gameObjects.size(); i++) {
            if (gameObjects.get(i) instanceof Brick) {
                gameObjects.get(i).render(renderer, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM);
            }
        }

        for (int i = 0; i < gameObjects.size(); i++ ) {
            if (gameObjects.get(i) instanceof Enemy) {
                gameObjects.get(i).render(renderer, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM);
            }
            else if (gameObjects.get(i) instanceof Player) {
                gameObjects.get(i).render(renderer, Game.PLAYER_ZOOM, Game.PLAYER_ZOOM);
            }
        }

        //Render bomb list
        if (!player.getBombs().isEmpty()) {
            for (int i = 0; i < player.getBombs().size(); i++) {
                player.getBombs().get(i).render(renderer, Game.MATERIAL_ZOOM, Game.MATERIAL_ZOOM);
            }
        }
    }

    /**
     * Update object and bomb
     * @param game
     */
    public void update(Game game) {
        for (int i = 0; i < gameObjects.size(); i++ ) {
            gameObjects.get(i).update(game);
        }

        //Update bomb list
        if (!player.getBombs().isEmpty()) {
            for (int i = 0; i < player.getBombs().size(); i++) {
                player.getBombs().get(i).update(game);
            }
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
//                Enemy enemy = getEnemyAt(x * tileWidth, y * tileHeight);
                if (tile != null && tile.collidable) {
                    Rectangle tileRectangle = new Rectangle(tile.x * tileWidth, tile.y * tileHeight, tileWidth, tileHeight);
                    if (tileRectangle.intersects(rect))
                        return true;
                }
//                if (enemy != null) {
//                    if (enemy.getCollisionCheckRectangle().intersects(rect)) {
//                        return true;
//                    }
//                }
            }
        }
        return false;
    }

    public boolean checkCollisionPlayerVsEnemy(Rectangle rect) {
        for (Enemy curEnemy : enemies) {
            if (rect.intersects(curEnemy.getCollisionCheckRectangle())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionMobVsBrick(Rectangle rect) {
        for (Brick curBrick : bricks) {
            if (curBrick.getCollisionCheckRectangle().intersects(rect)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionEnemyVsPlayer(Rectangle rect) {
        if (rect.intersects(player.getCollisionCheckRectangle())) {
            return true;
        }
        return false;
    }

    public boolean checkCollisionEnemyVsEnemy(Enemy thisEnemy, Rectangle rect) {
        for (Enemy curEnemy : enemies) {
            if (rect.intersects(curEnemy.getCollisionCheckRectangle()) && curEnemy.getEnemyID() != thisEnemy.getEnemyID()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkCollisionEnemyVsBomb(Enemy thisEnemy, Rectangle rect) {
        for (Bomb curBomb : player.getBombs()) {
            if (rect.intersects(curBomb.getCollisionCheckRectangle())) {
                return true;
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

    /**
     * Get the enemy at specific location if there is any
     * @param xLoc
     * @param yLoc
     * @return
     */
    public Enemy getEnemyAt(int xLoc, int yLoc) {

        for (Enemy curEnemy : enemies) {
            if (curEnemy.getPlayerRectangle().x == xLoc && curEnemy.getPlayerRectangle().y == yLoc) {
                return curEnemy;
            }
        }
        return null;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }


}
