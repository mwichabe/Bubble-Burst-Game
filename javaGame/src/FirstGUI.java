import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FirstGUI extends JFrame {
    private JButton startButton;
    private JButton restartButton;
    private JSlider difficultySlider;

    public FirstGUI() {
        setTitle("Bubble Burst Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLayout(new FlowLayout());

        startButton = new JButton("Start");
        restartButton = new JButton("Restart");
        difficultySlider = new JSlider(JSlider.HORIZONTAL, 4, 6, 4);

        add(startButton);
        add(restartButton);
        add(difficultySlider);

        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SecondGUI secondGUI = new SecondGUI(difficultySlider.getValue());
                secondGUI.setVisible(true);
                dispose();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Game restarted");
            }
        });
    }

    public static void main(String[] args) {
        FirstGUI firstGUI = new FirstGUI();
        firstGUI.setVisible(true);
    }
}