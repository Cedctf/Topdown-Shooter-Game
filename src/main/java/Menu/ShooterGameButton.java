package Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;

public class ShooterGameButton extends JButton {
    private final String FONT_PATH = "/Menu/Kenney Future.ttf";
    private final String BUTTON_PRESSED_STYLE = "/Menu/yellow_button_pressed.png";
    private final String BUTTON_FREE_STYLE = "/Menu/yellow_button.png";

    public ShooterGameButton(String text) {
        setText(text);
        setButtonFont();
        setPreferredSize(new Dimension(260, 60));
        setButtonFreeStyle();
        initializeButtonListeners();

        // Set properties to ensure text is painted on top of the image
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.CENTER);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    private void setButtonFont() {
        try {
            InputStream is = getClass().getResourceAsStream(FONT_PATH);
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(23f);
            setFont(font);
        } catch (Exception e) {
            setFont(new Font("Arial", Font.PLAIN, 23));
            e.printStackTrace();
        }
    }

    private void setButtonPressedStyle() {
        setIcon(new ImageIcon(getClass().getResource(BUTTON_PRESSED_STYLE)));
        setPreferredSize(new Dimension(260, 56));
    }

    private void setButtonFreeStyle() {
        setIcon(new ImageIcon(getClass().getResource(BUTTON_FREE_STYLE)));
        setPreferredSize(new Dimension(260, 60));
    }

    private void initializeButtonListeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setButtonPressedStyle();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    setButtonFreeStyle();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createRaisedBevelBorder());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(null);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new FlowLayout());

        ShooterGameButton button = new ShooterGameButton("Shoot");
        frame.add(button);

        frame.setVisible(true);
    }
}
