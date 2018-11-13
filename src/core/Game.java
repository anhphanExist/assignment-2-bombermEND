package core;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.io.IOException;

import core.animation.GameObject;
import core.character.*;

public class Game extends JFrame implements Runnable {
    private final static int width = 720, height = 640;

    private Canvas canvas = new Canvas(); //canvas

    private RenderHandler renderer; //renderer

    private KeyBoard keyBoard = new KeyBoard(); //KeyBoard event

    private GameObject[] gameObjects;

    public static int randomColor = 0xFFFF00FF;

    //region testing
    public Rectangle testRect;

    private Player player = new Player();
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
        addGameObjects(1);

        testRect = new Rectangle(32,16,16,16);
        testRect.generateGraphic(1,0xFF00FF90);

        //endregion

    }


    public void addGameObjects(int num) {
        gameObjects = new GameObject[num];
        gameObjects[0] = player;
    }

    /**
     * update method
     */
    public void update() {
        for (int i = 0; i < gameObjects.length; i++ ) {
            gameObjects[i].update(this);
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
        renderer.renderRectangle(testRect,3,3);

        for (int i = 0; i < gameObjects.length; i++ ) {
            gameObjects[i].render(renderer,2,2);
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
}
