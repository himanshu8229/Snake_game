import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class snake_game extends JPanel implements ActionListener {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int FOOD_SIZE = 10;
    private static final int ALLFOOD = 900;
    private static final int RAND_POS = 45;
    private static final int DELAY = 150;

    private final int[] x = new int[ALLFOOD];
    private final int[] y = new int[ALLFOOD];
    private int food;
    private int foodX;
    private int foodY;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean GameStart = true;

    private Timer timer;

    public snake_game() {
        initGame();
    }

    private void initGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);

        addKeyListener(new GameKeyListener());
        initSnake();
        placeFood();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void initSnake() {
        food = 3;
        for (int i = 0; i < food; i++) {
            x[i] = 50 - i * FOOD_SIZE;
            y[i] = 50;
        }
    }

    private void placeFood() {
        int r = (int) (Math.random() * RAND_POS);
        foodX = r * FOOD_SIZE;

        r = (int) (Math.random() * RAND_POS);
        foodY = r * FOOD_SIZE;
    }

    private void move() {
        for (int i = food; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (leftDirection) {
            x[0] -= FOOD_SIZE;
        }

        if (rightDirection) {
            x[0] += FOOD_SIZE;
        }

        if (upDirection) {
            y[0] -= FOOD_SIZE;
        }

        if (downDirection) {
            y[0] += FOOD_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = food; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                GameStart = false;
                break;
            }
        }

        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) {
            GameStart = false;
        }

        if (!GameStart) {
            timer.stop();
        }
    }

    private void checkFood() {
        if (x[0] == foodX && y[0] == foodY) {
            food++;
            placeFood();
        }
    }

    private void draw(Graphics g) {
        if (GameStart) {
            g.setColor(Color.white);
            g.fillOval(foodX, foodY, FOOD_SIZE, FOOD_SIZE);

            for (int i = 0; i < food; i++) {
                g.fillRect(x[i], y[i], FOOD_SIZE, FOOD_SIZE);
            }
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        String msg = "Game Over";
        Font font = new Font("Serif", Font.BOLD, 18);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(msg, (WIDTH - metrics.stringWidth(msg)) / 2, HEIGHT / 2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (GameStart) {
            checkFood();
            checkCollision();
            move();
        }

        repaint();
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new snake_game());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

