import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SecondGUI extends JFrame {
    private JPanel playingField;
    private ArrayList<Point> bubbleOrigins;
    private int neighborhoodSize = 50;
    private int round = 1;
    private int maxRounds = 10;
    private int difficulty;
    private int initialRoundTime = 15;
    private int currentRoundTime;
    private Timer timer;
    private JLabel timerLabel;

    public SecondGUI(int difficulty) {
        this.difficulty = difficulty;
        setTitle("Playing Field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450); // Increased height for timer display
        setLayout(new BorderLayout());

        bubbleOrigins = new ArrayList<>();

        playingField = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point origin : bubbleOrigins) {
                    int x = (int) origin.getX();
                    int y = (int) origin.getY();
                    g.setColor(Color.BLUE);
                    g.fillOval(x, y, 20, 20);
                }
                g.drawString("Round: " + round, 10, 20);
            }
        };
        playingField.setBackground(Color.WHITE);

        timerLabel = new JLabel("Time Left: " + initialRoundTime + "s");
        add(timerLabel, BorderLayout.NORTH);

        playingField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int bubbleIndex = getClickedBubbleIndex(clickPoint);
                if (bubbleIndex != -1) {
                    // Bubble clicked, remove it
                    bubbleOrigins.remove(bubbleIndex);
                    playingField.repaint();
                    if (bubbleOrigins.isEmpty()) {
                        if (round < maxRounds) {
                            // All bubbles burst, start a new round
                            round++;
                            startNewRound();
                        } else {
                            // All rounds completed
                            endGame("All rounds completed. Game Over.");
                        }
                    }
                } else {
                    // If no bubble was clicked, add a new one if space allows
                    if (bubbleOrigins.size() < difficulty) {
                        if (isValidBubbleOrigin(clickPoint)) {
                            bubbleOrigins.add(clickPoint);
                            playingField.repaint();
                            if (bubbleOrigins.size() == difficulty) {
                                // Start Round 1
                                startNewRound();
                            }
                        } else {
                            JOptionPane.showMessageDialog(SecondGUI.this,
                                    "Bubble must be fully contained within the playing field",
                                    "Invalid Selection", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }

            private int getClickedBubbleIndex(Point clickPoint) {
                for (int i = 0; i < bubbleOrigins.size(); i++) {
                    Point bubbleOrigin = bubbleOrigins.get(i);
                    double distance = Math.sqrt(Math.pow(clickPoint.getX() - bubbleOrigin.getX(), 2) +
                                                Math.pow(clickPoint.getY() - bubbleOrigin.getY(), 2));
                    if (distance <= 20) {
                        return i;
                    }
                }
                return -1;
            }
        });

        add(playingField, BorderLayout.CENTER);
    }

    private boolean isValidBubbleOrigin(Point point) {
        int x = (int) point.getX();
        int y = (int) point.getY();
        return x >= 0 && x <= playingField.getWidth() - 20 &&
                y >= 0 && y <= playingField.getHeight() - 20;
    }

    private void startNewRound() {
        currentRoundTime = initialRoundTime - (round - 1);
        timerLabel.setText("Time Left: " + currentRoundTime + "s");
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentRoundTime--;
                timerLabel.setText("Time Left: " + currentRoundTime + "s");
                if (currentRoundTime == 0) {
                    // Round time is up
                    endGame("Time's up! Game Over.");
                }
            }
        });
        timer.start();
        repositionBubblesGlobally();
        playingField.repaint();
        JOptionPane.showMessageDialog(SecondGUI.this,
                "Round " + round + " started", "Round Start", JOptionPane.INFORMATION_MESSAGE);
    }

    private void endGame(String message) {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        JOptionPane.showMessageDialog(SecondGUI.this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void repositionBubblesGlobally() {
        bubbleOrigins.clear();
        Random random = new Random();
        for (int i = 0; i < difficulty; i++) {
            int x = random.nextInt(playingField.getWidth() - 20);
            int y = random.nextInt(playingField.getHeight() - 20);
            bubbleOrigins.add(new Point(x, y));
        }
    }
}
