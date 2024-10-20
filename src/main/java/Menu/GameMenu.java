package Menu;

import UserInfo.UserSession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameMenu extends JFrame implements ActionListener {

    private static MainMenu mainMenu = new MainMenu();


    private String username;
    private static final int buttonInitY = 330;
    private ShooterGameButton playButton;
    private ShooterGameButton exitButton;
    private ShooterGameButton scoreButton;
    private ShooterGameButton settingsButton;
    private PlaySubpanel playSubpanel;
    private Subpanel settingsSubpanel;

    public GameMenu() {
        this.username = UserSession.getUsername();

        // Frame setup
        setTitle("Game Menu");
        setSize(1520, 780);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 1520, 780);

        // Logo image
        ImageIcon logoPic = new ImageIcon(getClass().getResource("/Menu/logo.png"));
        JLabel logo = new JLabel(logoPic);
        logo.setBounds((getWidth() - logoPic.getIconWidth()) / 2, 60, logoPic.getIconWidth(), logoPic.getIconHeight());
        panel.add(logo);

        playButton = new ShooterGameButton("Play");
        playButton.setBounds(630, buttonInitY, 260, 60);
        playButton.addActionListener(this);
        panel.add(playButton);

        /*
        scoreButton = new ShooterGameButton("Score");
        scoreButton.setBounds(630, buttonInitY + 70, 260, 60);
        scoreButton.addActionListener(this);
        panel.add(scoreButton);
        */

        settingsButton = new ShooterGameButton("Settings");
        settingsButton.setBounds(630, buttonInitY + 70, 260, 60);
        settingsButton.addActionListener(this);
        panel.add(settingsButton);

        exitButton = new ShooterGameButton("Exit");
        exitButton.setBounds(630, buttonInitY + 140, 260, 60);
        exitButton.addActionListener(this);
        panel.add(exitButton);

        // Background image
        ImageIcon backgroundPic = new ImageIcon(getClass().getResource("/Menu/background.png"));
        JLabel background = new JLabel(backgroundPic);
        background.setBounds(0, -30, 1520, 780);
        panel.add(background);

        // Add sliding panels
        playSubpanel = new PlaySubpanel(() -> setButtonsEnabled(true), this);
        panel.add(playSubpanel, 0); // Add with z-index 0 to ensure it is behind other components

        /*
        scoreSubpanel = new ScoreSubpanel(() -> setButtonsEnabled(true));
        panel.add(scoreSubpanel, 0);

         */

        settingsSubpanel = new Subpanel(() -> setButtonsEnabled(true));
        panel.add(settingsSubpanel, 0);

        // Ensure the background is the last component added to the panel
        panel.add(background);

        // Add panel to frame
        add(panel);
    }

    private void setButtonsEnabled(boolean enabled) {
        playButton.setEnabled(enabled);
        //scoreButton.setEnabled(enabled);
        settingsButton.setEnabled(enabled);
        exitButton.setEnabled(enabled);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setButtonsEnabled(false);
        if (e.getSource() == playButton) {
            playSubpanel.moveSubPanel(350);
        } else if (e.getSource() == settingsButton) {
            settingsSubpanel.moveSubPanel(350);
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    public static void launchGameMenu(){
        GameMenu gameMenu = new GameMenu();
        gameMenu.setVisible(true);
    }

    public static void main(String[] args) {
        launchGameMenu();
    }
}