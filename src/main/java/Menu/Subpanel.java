package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Sound.Sound;
import UserInfo.*;


public class Subpanel extends JPanel {
    private static final String BACKGROUND_IMAGE = "/Menu/subpanel.png";
    private boolean isHidden = true;
    private Timer slideTimer;
    private ShooterGameButton backButton;
    private String username;
    private Runnable onBackAction;

    private String[] actions = {"Move up", "Move down", "Move left", "Move right"};
    private JTextField[] player1KeyFields = new JTextField[actions.length];
    private String[] player1DefaultKeys = {"W", "S", "A", "D"};
    private Map<String, String> keybinds;

    public Subpanel( Runnable onBackAction) {
        this.username = UserSession.getUsername();
        this.onBackAction = onBackAction;
        setLayout(null);
        setSize(780, 580);
        setOpaque(false);

        // Load keybinds from file
        keybinds = loadKeybinds();

        ImageIcon originalIcon = new ImageIcon(getClass().getResource(BACKGROUND_IMAGE));
        Image image = originalIcon.getImage().getScaledInstance(780, 580, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(image);

        JLabel settingsLabel = new JLabel("Settings");
        settingsLabel.setFont(new Font("PixelFont", Font.BOLD, 36));
        settingsLabel.setHorizontalAlignment(JLabel.CENTER);
        settingsLabel.setBounds(0, 30, 780, 50);
        add(settingsLabel);

        backButton = new ShooterGameButton("Back");
        Dimension buttonSize = backButton.getPreferredSize();
        int buttonX = (getWidth() - buttonSize.width) / 2;
        backButton.setBounds(buttonX, 450, buttonSize.width, buttonSize.height);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveKeybinds();
                moveSubPanel(1520);
                onBackAction.run();
            }
        });
        add(backButton);

        setLocation(1520, 75);

        int centerX = getWidth() / 2;

        // Creating player key fields
        for (int i = 0; i < actions.length; i++) {
            JLabel actionLabel = new JLabel(actions[i]);
            actionLabel.setFont(new Font("PixelFont", Font.PLAIN, 24));
            actionLabel.setHorizontalAlignment(JLabel.RIGHT);
            actionLabel.setPreferredSize(new Dimension(150, 30));

            final int index = i; // Declare index as final to be used within inner class
            final JTextField keyField = new JTextField(1);
            keyField.setFont(new Font("PixelFont", Font.BOLD, 16));
            keyField.setOpaque(false);
            keyField.setBackground(new Color(0, 0, 0, 150));
            keyField.setHorizontalAlignment(JTextField.CENTER);
            keyField.setText(keybinds.getOrDefault(actions[i], player1DefaultKeys[i]));
            keyField.setEditable(false);
            keyField.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

            player1KeyFields[i] = keyField; // Store the text field in the array

            // Add labels and text fields to the panel
            add(actionLabel);
            add(keyField);

            // Calculate positions
            int verticalOffset = 80; // Vertical offset to move everything down
            int rowSpacing = 60; // Increased spacing between rows
            int labelY = verticalOffset + 70 + i * rowSpacing; // Vertical position for label
            int fieldY = verticalOffset + 70 + i * rowSpacing; // Vertical position for text field

            actionLabel.setBounds(centerX - 160, labelY, 150, 30);
            keyField.setBounds(centerX + 10, fieldY, 60, 30);

            // Make text fields editable on click
            keyField.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    keyField.setEditable(true);
                    keyField.setText(""); // Clear text on click to avoid showing default key
                }
            });


            keyField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    String newKey = KeyEvent.getKeyText(e.getKeyCode());
                    boolean isDuplicate = false;
                    for (int j = 0; j < player1KeyFields.length; j++) {
                        if (j != index && player1KeyFields[j].getText().equals(newKey)) {
                            isDuplicate = true;
                            break;
                        }
                    }
                    if (!isDuplicate) {
                        keyField.setText(newKey);
                        keyField.setEditable(false);
                        keybinds.put(actions[index], newKey);
                        Subpanel.this.requestFocusInWindow();
                    } else {
                        System.out.println("Duplicate key detected: " + newKey);
                        keyField.setText(keybinds.get(actions[index]));
                        keyField.setEditable(false);
                        Subpanel.this.requestFocusInWindow();
                    }
                }
            });
        }

        JLabel background = new JLabel(scaledIcon);
        background.setBounds(0, 0, 780, 580);
        add(background);
    }

    private Map<String, String> loadKeybinds() {
        try {
            return UserUtil.loadUserKeybinds(username);
        } catch (IOException e) {
            System.out.println("Error loading keybinds: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private void saveKeybinds() {
        try {
            Map<String, User> users = UserUtil.loadUsers();
            User user = users.get(username);
            if (user != null) {
                user.setKeybinds(keybinds);
                UserUtil.saveAllUsers(users);
            }
        } catch (IOException e) {
            System.out.println("Error saving keybinds: " + e.getMessage());
        }
    }

    public void moveSubPanel(int endX) {
        Sound.buttonSound.playSoundEffect();
        int startX = getX();
        int step = 30;
        int delay = 5;

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
