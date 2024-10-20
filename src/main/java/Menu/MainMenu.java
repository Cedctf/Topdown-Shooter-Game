package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import Sound.Sound;
import UserInfo.*;


public class MainMenu extends JFrame implements ActionListener {


    private String username;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private ShooterGameButton signUpButton;
    private ShooterGameButton logInButton;
    private static final String FONT_PATH = "Menu/Kenney Future.ttf";


    public MainMenu() {
        // Frame setup
        setTitle("Main Menu");
        setSize(1520, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        Sound.menuTheme.playMusic();
        Sound.menuTheme.loop();
        Sound.menuTheme.setVolume(0.9f);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 1520, 780);

        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(575, 225, 100, 30);

        panel.add(usernameLabel);

        // Username field
        usernameField = new JTextField();
        usernameField.setBounds(675, 225, 200, 30);
        usernameField.setBackground(Color.LIGHT_GRAY);
        panel.add(usernameField);

        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(575, 275, 100, 30);
        panel.add(passwordLabel);

        // Password field
        passwordField = new JPasswordField();
        passwordField.setBounds(675, 275, 200, 30);
        passwordField.setBackground(Color.LIGHT_GRAY);
        panel.add(passwordField);

        // Sign-up button
        signUpButton = new ShooterGameButton("Sign Up");
        signUpButton.setBounds(600, 325, 260, 60);
        signUpButton.addActionListener(this);
        panel.add(signUpButton);

        // Log-in button
        logInButton = new ShooterGameButton("Log In");
        logInButton.setBounds(600, 395, 260, 60);
        logInButton.addActionListener(this);
        panel.add(logInButton);

        // Background image
        ImageIcon backgroundPic = new ImageIcon(getClass().getResource("/Menu/background.png"));
        JLabel background = new JLabel(backgroundPic);
        background.setBounds(0, -30, 1520, 780);
        panel.add(background);

        // Add panel to frame
        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        Sound.buttonSound.playSoundEffect();

        if (e.getSource() == signUpButton) {
            // Check if username or password is empty
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username and password cannot be empty.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if (UserUtil.isUsernameTaken(username)) {
                    JOptionPane.showMessageDialog(this,
                            "Username already taken.",
                            "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    UserUtil.saveUser(username, password, UserUtil.getDefaultKeybinds());
                    JOptionPane.showMessageDialog(this, "Sign Up Successful!");
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error saving user data: " + ioException.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == logInButton) {
            try {
                if (UserUtil.validateUser(username, password)) {
                    UserSession.setUsername(username);
                    JOptionPane.showMessageDialog(this,
                            "Login Successful!");
                    new GameMenu().setVisible(true);
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid username or password.");
                }
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(this, "Error reading user data: " + ioException.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Clear the text fields
        usernameField.setText("");
        passwordField.setText("");
    }

    public String getUsername() {
        return username;
    }

}

