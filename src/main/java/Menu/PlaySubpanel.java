package Menu;

import Main.GameApplication;
import Sound.Sound;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlaySubpanel extends JPanel {
    private static final String BACKGROUND_IMAGE = "/Menu/subpanel.png";
    private boolean isHidden = true;
    private Timer slideTimer;
    private ShooterGameButton backButton;

    private JLabel playerImage;
    private JButton[] gunButtons = new JButton[4];
    private JLabel gunDescription;
    private JRadioButton[] difficultyButtons = new JRadioButton[3];
    private ShooterGameButton startButton;
    private ButtonGroup difficultyGroup;
    private Runnable onBackAction;
    private GameMenu parentFrame;

    private static String selectedDifficulty;

    public PlaySubpanel(Runnable onBackAction, GameMenu parentFrame) {
        this.onBackAction = onBackAction;
        this.parentFrame = parentFrame;
        setLayout(null);
        setSize(780, 580);
        setOpaque(false); // Make the panel transparent so background shows

        // Load background image
        ImageIcon originalIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE));
        Image image = originalIcon.getImage().getScaledInstance(780, 580, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);

        JLabel playLabel = new JLabel("Play");
        playLabel.setFont(new Font("PixelFont", Font.BOLD, 36));
        playLabel.setHorizontalAlignment(JLabel.CENTER);
        playLabel.setBounds(0, 30, 780, 50);
        add(playLabel);

        // Back button setup
        backButton = new ShooterGameButton("Back");
        Dimension backButtonSize = backButton.getPreferredSize();
        backButton.setBounds(220 - backButtonSize.width / 2, 450, backButtonSize.width, backButtonSize.height);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveSubPanel(1520);
                onBackAction.run();
            }
        });
        add(backButton);

        // Start button setup
        startButton = new ShooterGameButton("Start");
        Dimension startButtonSize = startButton.getPreferredSize();
        startButton.setBounds(560 - startButtonSize.width / 2, 450, startButtonSize.width, startButtonSize.height);
        startButton.setEnabled(false); // Initially disabled
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (getSelectedDifficulty() != null) {
                    System.out.println("Selected Difficulty: " + getSelectedDifficulty());
                    Sound.menuTheme.stop();
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            parentFrame.dispose();
                            GameApplication.execute();
                        }
                    });
                }
            }
        });
        add(startButton);

        // Set initial position
        setLocation(1520, 75);


        ImageIcon originalPlayer = new ImageIcon(getClass().getResource("/Menu/characters/player1.png"));
        Image playerImage = originalPlayer.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledPlayerIcon = new ImageIcon(playerImage);

        JLabel player = new JLabel(scaledPlayerIcon);
        player.setBounds(500, 140, 100, 100);
        add(player);

        /*
        // Player image setup
        playerImage = new JLabel(new ImageIcon(getClass().getResource("/Menu/characters/player1.png")));
        playerImage.setBounds(500, 100, 150, 150);
        add(playerImage);
        */

        // Gun buttons setup
        String[] gunDescriptions = {"Sniper: Far range with highest damage", "Pistol: Fast reload", "Rifle: High fire rate", "Shotgun: Big AOE"};
        for (int i = 0; i < gunButtons.length; i++) {
            gunButtons[i] = new JButton(new ImageIcon(getClass().getResource("/Menu/gun/gun" + (i + 1) + ".png")));
            gunButtons[i].setBounds(190 + (i % 2) * 150, 80 + (i / 2) * 100, 100, 100);
            gunButtons[i].setOpaque(false);
            gunButtons[i].setContentAreaFilled(false);
            gunButtons[i].setBorderPainted(false);
            final String description = gunDescriptions[i];
            gunButtons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    gunDescription.setText(description);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    gunDescription.setText("");
                }
            });
            add(gunButtons[i]);
        }

        gunDescription = new JLabel("", SwingConstants.CENTER);
        gunDescription.setBounds(100, 300, 580, 30);
        gunDescription.setFont(new Font("Arial", Font.BOLD, 24));
        add(gunDescription);

        difficultyGroup = new ButtonGroup();
        String[] difficulties = {"Easy", "Medium", "Hard"};
        for (int i = 0; i < difficultyButtons.length; i++) {
            final int index = i;
            difficultyButtons[i] = new JRadioButton(difficulties[i]);
            difficultyButtons[i].setFont(new Font("Arial", Font.BOLD, 24));
            difficultyButtons[i].setBounds(190 + i * 150, 370, 150, 50);
            difficultyButtons[i].setOpaque(false); // Make the background transparent
            difficultyButtons[i].setContentAreaFilled(false); // Make the content area not filled
            difficultyButtons[i].setBorderPainted(false); // Remove the border
            difficultyButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedDifficulty = difficultyButtons[index].getText();
                    //System.out.println(selectedDifficulty); For testing purpose
                    startButton.setEnabled(true); // Enable start button when a difficulty is selected
                }
            });
            difficultyGroup.add(difficultyButtons[i]);
            add(difficultyButtons[i]);
        }

        // Background label
        JLabel background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 780, 580);
        add(background);
    }

    public static String getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public void moveSubPanel(int endX) {
        Sound.buttonSound.playSoundEffect();
        int startX = getX();
        int step = 30; // Step for each animation frame
        int delay = 5; // Delay between each step in milliseconds

        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isHidden) {
                    if (getX() > endX) {
                        setLocation(getX() - step, getY());
                    } else {
                        ((Timer) e.getSource()).stop();
                        isHidden = false;
                    }
                } else {
                    if (getX() < endX) {
                        setLocation(getX() + step, getY());
                    } else {
                        ((Timer) e.getSource()).stop();
                        isHidden = true;
                    }
                }
            }
        });

        timer.start();
    }
}
