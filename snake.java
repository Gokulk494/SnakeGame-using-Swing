import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Random;

public class snake extends JPanel implements ActionListener {
    static final int S_Width = 1200;
    static final int S_Height = 600;
    static final int Game_unit_size = 50;
    Timer timer;
    Random random;
    int foodEaten;
    int foodX;
    int foodY;
    int bodyLength = 2;
    boolean gameFlag = false;
    char dir = 'R';
    static final int DELAY = 160;
    static final int G_Size = (S_Width * S_Height) / (Game_unit_size * Game_unit_size);
    final int xSnake[] = new int[G_Size];
    final int ySnake[] = new int[G_Size];

    snake() {
        this.setPreferredSize(new Dimension(S_Width, S_Height));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKey());
        random = new Random();
        gameStart();
    }

    public void gameStart() {
        newFoodPosition();
        gameFlag = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (gameFlag) {
            move();
            foodEatenOrNot();
            checkHit();
        }
        repaint();
    }

    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }

    public void draw(Graphics graphic) {
        if (gameFlag) {
            graphic.setColor(Color.yellow);
            graphic.fillOval(foodX, foodY, Game_unit_size, Game_unit_size);
            for (int i = 0; i < bodyLength; i++) {
                if (i == 0) {
                    graphic.setColor(Color.green);
                    graphic.fillRect(xSnake[i], ySnake[i], Game_unit_size, Game_unit_size);
                } else {
                    graphic.setColor(new Color(50, 180, 0));
                    graphic.fillRect(xSnake[i], ySnake[i], Game_unit_size, Game_unit_size);
                }
            }
            graphic.setColor(Color.blue);
            graphic.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics fontMe = getFontMetrics(graphic.getFont());
            graphic.drawString("Score:" + foodEaten,
                    (S_Width - fontMe.stringWidth("Score:" + foodEaten)) / 2, graphic.getFont().getSize());
        } else {
            gameOver(graphic);
        }
    }

    public void move() {
        for (int i = bodyLength; i > 0; i--) {
            xSnake[i] = xSnake[i - 1];
            ySnake[i] = ySnake[i - 1];
        }
        switch (dir) {
            case 'U':
                ySnake[0] = ySnake[0] - Game_unit_size;
                break;
            case 'L':
                xSnake[0] = xSnake[0] - Game_unit_size;
                break;
            case 'D':
                ySnake[0] = ySnake[0] + Game_unit_size;
                break;
            case 'R':
                xSnake[0] = xSnake[0] + Game_unit_size;
                break;
        }
    }

    public void newFoodPosition() {
        foodX = random.nextInt((int) (S_Width / Game_unit_size)) * Game_unit_size;
        foodY = random.nextInt((int) (S_Height / Game_unit_size)) * Game_unit_size;
    }

    public void foodEatenOrNot() {
        if ((xSnake[0] == foodX) && (ySnake[0] == foodY)) {
            bodyLength++;
            foodEaten++;
            newFoodPosition();
        }
    }

    public void checkHit() {
        for (int i = bodyLength; i > 0; i--) {
            if ((xSnake[0] == xSnake[i]) && (ySnake[0] == ySnake[i])) {
                gameFlag = false;
            }
        }
        if (xSnake[0] < 0 || xSnake[0] > S_Width || ySnake[0] < 0 || ySnake[0] > S_Height) {
            gameFlag = false;
        }
        if (!gameFlag) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphic) {
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics fontMe = getFontMetrics(graphic.getFont());
        graphic.drawString("Score:" + foodEaten, (S_Width - fontMe.stringWidth("Score:" + foodEaten)) / 2,
                graphic.getFont().getSize());
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics fontMe2 = getFontMetrics(graphic.getFont());
        graphic.drawString("Game Over", (S_Width - fontMe2.stringWidth("Game Over")) / 2, S_Height / 2);
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics fontMe3 = getFontMetrics(graphic.getFont());
        graphic.drawString("Press R to Replay", (S_Width - fontMe3.stringWidth("Press R to Replay")) / 2,
                S_Height / 2 - 150);
    }

    public class MyKey extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (dir != 'R') {
                        dir = 'L';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (dir != 'D') {
                        dir = 'U';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (dir != 'L') {
                        dir = 'R';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (dir != 'U') {
                        dir = 'D';
                    }
                    break;
                case KeyEvent.VK_R:
                    if (!gameFlag) {
                        foodEaten = 0;
                        bodyLength = 2;
                        dir = 'R';
                        Arrays.fill(xSnake, 0);
                        Arrays.fill(ySnake, 0);
                        gameStart();
                    }
                    break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(new snake());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
