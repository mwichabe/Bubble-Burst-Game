import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SecondGUI extends JFrame {
    private JPanel playingField;
    private ArrayList<BubbleContainer> bubbleContainers;
    private int neighborhoodSize = 50;
    private int round = 1;
    private int maxRounds = 10;
    private int difficulty;
    private int initialRoundTime = 15;
    private int currentRoundTime;
    private Timer timer;
    private JLabel timerLabel;
    private int borderSize = 20;


    public SecondGUI(int difficulty) {
        this.difficulty = difficulty;
        setTitle("Playing Field");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 450); 
        setLayout(new BorderLayout());

        bubbleContainers = new ArrayList<>();

        playingField = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (BubbleContainer container : bubbleContainers) {
                    int x = container.getX();
                    int y = container.getY();
                    int containerSize = neighborhoodSize;
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, containerSize, containerSize);
                    g.setColor(container.getColor());
                    g.fillOval(container.getBubbleX(), container.getBubbleY(), 18, 18);
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
                    bubbleContainers.remove(bubbleIndex);
                    playingField.repaint();
                    if (bubbleContainers.isEmpty()) {
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
                    if (bubbleContainers.size() < difficulty) {
                        if (isValidBubbleOrigin(clickPoint)) {
                            BubbleContainer newContainer = new BubbleContainer(clickPoint, getRandomColor());
                            bubbleContainers.add(newContainer);
                            playingField.repaint();
                            if (bubbleContainers.size() == difficulty) {
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
                for (int i = 0; i < bubbleContainers.size(); i++) {
                    BubbleContainer container = bubbleContainers.get(i);
                    if (container.contains(clickPoint)) {
                        return i;
                    }
                }
                return -1;
            }
        });

        add(playingField, BorderLayout.CENTER);
        Timer bubbleTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveBubbles();
                playingField.repaint();
            }
        });
        bubbleTimer.start();
    }
    private void moveBubbles() {
        for (BubbleContainer container : bubbleContainers) {
            int newX = container.getX() + getRandomMovement();
            int newY = container.getY() + getRandomMovement();
            container.setBubbleX(newX);
            container.setBubbleY(newY);
        }
    }

    private int getRandomMovement() {
        return (int) (Math.random() * 5) - 2;
    
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
        
        // Stop the previous timer if it's running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
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
        bubbleContainers.clear();
        Random random = new Random();
        int count = 0;
        while (count < difficulty) {
            int x = random.nextInt(playingField.getWidth() - 20);
            int y = random.nextInt(playingField.getHeight() - 20);
            BubbleContainer newContainer = new BubbleContainer(new Point(x, y), getRandomColor());
            if (!hasCollision(newContainer)) {
                bubbleContainers.add(newContainer);
                count++;
            }
        }
    }

    private boolean hasCollision(BubbleContainer newContainer) {
        for (BubbleContainer container : bubbleContainers) {
            if (isCollision(container, newContainer)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCollision(BubbleContainer container1, BubbleContainer container2) {
        Rectangle rect1 = new Rectangle(container1.getX(), container1.getY(), 20, 20);
        Rectangle rect2 = new Rectangle(container2.getX(), container2.getY(), 20, 20);
        return rect1.intersects(rect2);
    }

    private Color getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    private class BubbleContainer {
        private Point position;
        private Color color;
        private int bubbleX;
        private int bubbleY;

        public BubbleContainer(Point position, Color color) {
            this.position = position;
            this.color = color;
            this.bubbleX = position.x;
            this.bubbleY = position.y;
        }

       
   
    public int getBubbleX() {
        return bubbleX;
    }

    public void setBubbleX(int bubbleX) {
        this.bubbleX = bubbleX;
    }

    public int getBubbleY() {
        return bubbleY;
    }

    public void setBubbleY(int bubbleY) {
        this.bubbleY = bubbleY;
    }

        public int getX() {
            return (int) position.getX();
        }

        public int getY() {
            return (int) position.getY();
        }

        public boolean contains(Point point) {
            Rectangle rect = new Rectangle(getX(), getY(), 20, 20);
            return rect.contains(point);
        }

        public Color getColor() {
            return color;
        }
    }
}
