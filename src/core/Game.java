package core;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Game extends JFrame implements Runnable {
    private final static int width = 720, height = 640;
    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    public static int randomColor = 0xFFFF00FF;

    //region testing
    public Rectangle testRect;

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

        //region testing
        testRect = new Rectangle(32,16,16,32);
        testRect.generateGraphic(3,0xFF00FF90);

        //endregion

    }

    /**
     * update method
     */
    public void update() {

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
}
