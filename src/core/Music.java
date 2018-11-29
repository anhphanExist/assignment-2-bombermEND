package core;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Music {
    private Clip clip;
    private boolean isLoop;

    public Music(String pathName, boolean isLoop) {
        try {
            this.isLoop = isLoop;

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(Game.class.getResourceAsStream(pathName));
            clip = AudioSystem.getClip();

            clip.open(inputStream);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startPlaying() {
        clip.start();
        if (isLoop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopPlaying() {
        clip.stop();
    }
}
