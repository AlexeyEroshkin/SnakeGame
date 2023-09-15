import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel  implements ActionListener {
//    Размер игрового поля
    static final int WIDTH = 600;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 30;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;
     final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();

    }
//    Метод начать игру
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

    }

//    Нарисовать компоненты
@Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

//    Рисовать компоненты
    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);

            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
/*
                    один цвет
                    g.setColor(new Color(45, 180, 0));
                    много цветов
*/
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));

                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
        } else {
            gameOver(g);
        }
    }
// Новые яблоки
    public void newApple(){
        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;


    }
//    Двигать компоненты
    public void move(){
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (direction == 'U') {
            y[0] = y[0] - UNIT_SIZE;
        } else if (direction == 'D') {
            y[0] = y[0] + UNIT_SIZE;
        } else if (direction == 'L') {
            x[0] = x[0] - UNIT_SIZE;
        } else if (direction == 'R') {
            x[0] = x[0] + UNIT_SIZE;
        }

    }

//    Проверка яблок
    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

//    Проверка столкновения
    public void checkCollision(){
//        Если голова столкнулась с телом
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
                break;
            }
        }
//        Если голова столкнулась с границами
        if (x[0] < 0) {
            running = false;
        }
        if (x[0] > WIDTH) {
            running = false;
        }
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > HEIGHT) {
            running = false;
        }
//        Остановка
        if (!running) {
            timer.stop();
        }
    }
//    Игра окончена
    public void gameOver(Graphics g){
//        Очки текст
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score" + applesEaten,
                (WIDTH - metrics.stringWidth("Score" + applesEaten)) / 2,g.getFont().getSize());
//        Конец текст
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",WIDTH / 2 - metrics2.stringWidth("Game Over") / 2, HEIGHT / 2 - 100);
//      Продолжение
        g.setColor(Color.BLUE);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Press Enter to continue",WIDTH / 2 - metrics3.stringWidth("Press Enter to continue") / 2, HEIGHT / 2 + 50);
//        Выход
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 20));
        FontMetrics metrics4 = getFontMetrics(g.getFont());
        g.drawString("Press Esc to exit",WIDTH / 2 - metrics4.stringWidth("Press Esc to exit") / 2, HEIGHT / 2 + 100);


    }
// Перезапуск игры
    public void restartGame(){
        applesEaten = 0;
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        startGame();
    }
//    Запись результатов в базу данных
    public void saveResult(){

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();

    }

    public class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_LEFT) {
                if (direction != 'R') {
                    direction = 'L';
                }
            } else if (keyCode == KeyEvent.VK_RIGHT) {
                if (direction != 'L') {
                    direction = 'R';
                }
            } else if (keyCode == KeyEvent.VK_UP) {
                if (direction != 'D') {
                    direction = 'U';
                }
            } else if (keyCode == KeyEvent.VK_DOWN) {
                if (direction != 'U') {
                    direction = 'D';
                }
            } else if (keyCode == KeyEvent.VK_ENTER) restartGame();
            else if (keyCode == KeyEvent.VK_ESCAPE) System.exit(0);
        }
    }

}
