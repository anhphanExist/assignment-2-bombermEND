package core;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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



    public static int randomColor = 0xFFFF00FF;

    private BufferedImage imageBom = loadImage("imageFolder/bomb_party_v3.png");
    private SpriteSheet bombSheet = new SpriteSheet(imageBom);

    //region testing
    private Level level1;



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
        //endregion
    }


    /**
     * update method
     */
    public void update() {
        level1.update(this);
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

    /**
     * play sound method
     * @param path of the file
     */
    public static synchronized void playSound(final String path , boolean isRepeat){
        new Thread(() -> {
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.class.getResourceAsStream(path));
                clip.open(inputStream);
                clip.start();
                if (isRepeat){
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
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


}
