package core;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.ArrayList;

import core.Level.Level;
import core.animation.GameObject;
import core.animation.SpriteSheet;
import core.character.*;

public class Game extends JFrame implements Runnable {
    private final static int width = 720, height = 640;

    public static final int MATERIAL_ZOOM = 4;
    public static final int PLAYER_ZOOM = 2;

    private Canvas canvas = new Canvas(); //canvas

    private RenderHandler renderer; //renderer

    private KeyBoard keyBoard = new KeyBoard(); //KeyBoard event

    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    public static int randomColor = 0xFFFF00FF;

    private BufferedImage imageBom = loadImage("imageFolder/bomb_party_v3.png");
    private SpriteSheet bombSheet = new SpriteSheet(imageBom);

    //region testing
    private Level level1;

    private Player player = new Player(6);
    private ArrayList<Enemy> enemies = new ArrayList<>();

    //endregion

    public Game() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0,0,width,height);
        setLocationRelativeTo(null);

        //Add graphic component
        add(canvas);

        //Make frame visible
        setVisible(true);

        //Create for buffer strategy
        canvas.createBufferStrategy(3);

        //Adding renderer
        renderer = new RenderHandler(getWidth(),getHeight());

        //Add keyBoard resource
        canvas.addKeyListener(keyBoard);
        canvas.addFocusListener(keyBoard);

        //region testing
        level1 = new Level(); // add level1
        addGameObjects();
        //endregion

    }


    public void addGameObjects() {
        gameObjects.add(player);
        for (int i = 0; i < 3; i++ ){
            Enemy enemy = new Enemy(256 * i + 256,256);
            enemies.add(enemy);
        }
        gameObjects.addAll(enemies);
    }

    /**
     * update method
     */
    public void update() {
        for (int i = 0; i < gameObjects.size(); i++ ) {
            gameObjects.get(i).update(this);
        }

        //Update bomb list
        if (!player.getBombs().isEmpty()) {
            for (int i = 0; i < player.getBombs().size(); i++) {
                player.getBombs().get(i).update(this);
            }
        }
    }

    /**
     * render method
     */
    public void render() {
        //Drawing graphic
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();
        Graphics graphics = bufferStrategy.getDrawGraphics();
        super.paint(graphics);

        //region testing
        level1.render(renderer, MATERIAL_ZOOM, MATERIAL_ZOOM); // render level1 (including map)

        for (int i = 0; i < gameObjects.size(); i++ ) {
            if (! (gameObjects.get(i) instanceof Enemy))
                gameObjects.get(i).render(renderer,PLAYER_ZOOM,PLAYER_ZOOM);
            else {
                gameObjects.get(i).render(renderer,MATERIAL_ZOOM,MATERIAL_ZOOM);
            }
        }

        //Render bomb list
        if (!player.getBombs().isEmpty()) {
            for (int i = 0; i < player.getBombs().size(); i++) {
                player.getBombs().get(i).render(renderer, MATERIAL_ZOOM, MATERIAL_ZOOM);
            }
        }

        //endregion

        renderer.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
        renderer.clearPixel();
    }

    /**
     * game loop
     */
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60; //60 frames per sec
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now - lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1 ){
                update();
                changeInSeconds--;
            }
            render();
            lastTime = now;
        }
    }

    /**
     * load Image method
     * @param path
     * @return BufferImage from file
     */
    public static BufferedImage loadImage(String path){
        try {
            BufferedImage loadImage = ImageIO.read(Game.class.getResource(path));

            BufferedImage formattedImage = new BufferedImage(
                    loadImage.getWidth(),loadImage.getHeight(),BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadImage,0,0,null);

            return formattedImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public KeyBoard getKeyBoard() {
        return keyBoard;
    }

    public RenderHandler getRenderer() {
        return renderer;
    }

    public SpriteSheet getBombSheet() {
        return bombSheet;
    }

    public Level getLevel1() {
        return level1;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
