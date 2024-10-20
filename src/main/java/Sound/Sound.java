package Sound;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip clip;
    private long clipTimePosition;

    public static final Sound menuTheme = new Sound("src/main/resources/Sound/MusicTheme/MainMenuTheme.wav");
    public static final Sound ingameTheme = new Sound("src/main/resources/Sound/MusicTheme/IngameTheme2.wav");
    public static final Sound buttonSound = new Sound("src/main/resources/Sound/Button.wav");
    public static final Sound zombieDeathSound = new Sound("src/main/resources/Sound/Zombie/ZombieDeath.wav");
    public static final Sound devilDeathSound = new Sound("src/main/resources/Sound/Zombie/DevilDeath.wav");
    public static final Sound devilFireball = new Sound("src/main/resources/Sound/Zombie/FireballSound.wav");
    public static final Sound zombieDogDeathSound = new Sound("src/main/resources/Sound/Zombie/ZombieDogDeath.wav");
    public static final Sound playerHurtSound1 = new Sound("src/main/resources/Sound/Player/HurtedSound1.wav");
    public static final Sound playerHurtSound2 = new Sound("src/main/resources/Sound/Player/HurtedSound2.wav");
    public static final Sound playerWalkingSound = new Sound("src/main/resources/Sound/Player/WalkingSound.wav");
    public static final Sound pistolShootSound = new Sound("src/main/resources/Sound/Weapon/PistolShoot.wav");
    public static final Sound pistolReloadingSound = new Sound("src/main/resources/Sound/Weapon/PistolReloading.wav");
    public static final Sound pistolReloadedSound = new Sound("src/main/resources/Sound/Weapon/PistolReloaded.wav");
    public static final Sound rifleShootSound = new Sound("src/main/resources/Sound/Weapon/RifleShoot.wav");
    public static final Sound rifleReloadingSound = new Sound("src/main/resources/Sound/Weapon/RifleReloading.wav");
    public static final Sound rifleReloadedSound = new Sound("src/main/resources/Sound/Weapon/RifleReloaded.wav");
    public static final Sound shotgunShootSound = new Sound("src/main/resources/Sound/Weapon/ShotgunShoot.wav");
    public static final Sound shotgunReloadShell = new Sound("src/main/resources/Sound/Weapon/ShotgunReloadShell.wav");
    public static final Sound shotgunReloadPump = new Sound("src/main/resources/Sound/Weapon/ShotgunReloadPump.wav");
    public static final Sound sniperShootSound = new Sound("src/main/resources/Sound/Weapon/SniperShoot.wav");
    public static final Sound sniperReloadingSound = new Sound("src/main/resources/Sound/Weapon/SniperReloading.wav");
    public static final Sound sniperReloadedSound = new Sound("src/main/resources/Sound/Weapon/SniperReloaded.wav");
    public static final Sound defeatTheme = new Sound("src/main/resources/Sound/MusicTheme/DefeatTheme.wav");

    public Sound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect() {
        clip.setFramePosition(0);  // Rewind to the beginning
        clip.start();  // Start playing
    }

    public void playMusic(){
        if (clip != null) {
            if (!clip.isRunning()) {
                clip.setFramePosition(0);  // Rewind to the beginning if not already playing
                clip.start();  // Start playing
            }
        }
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void pause() {
        if (clip.isRunning()) {
            clipTimePosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (!clip.isRunning()) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
        }
    }

    public void setVolume(float volume) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * volume) + gainControl.getMinimum();
            gainControl.setValue(gain);
        }
    }
}