import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {
    private int dots;
    private Image apple, head, dot;

    private final int MAX_DOTS = 900;
    private final int DOT_SIZE = 10;

    private final int x[] = new int[MAX_DOTS];
    private final int y[] = new int[MAX_DOTS];
    private final int RANDOM_POSITION = 29;

    private int apple_x, apple_y;

    private Timer timer;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;

    private boolean inGame = true;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Board() {
        addKeyListener(new TAdapter());
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(300, 300));
        setFocusable(true);
        loadImages();
        initGame();
    }

    public void loadImages(){
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/apple.png"));
        ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("icons/head.png"));
        ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("icons/dot.png"));
        
        apple = i1.getImage();
        head = i2.getImage();
        dot = i3.getImage();
    }

    public void initGame(){
        dots = 3;
        inGame = true;

        for(int i=0; i<dots; i++){
            y[i] = 50;
            x[i] = 50-i*DOT_SIZE;
        }

        locateApple();

        timer = new Timer(140, this);
        timer.start();
    }

    public void locateApple(){
        int r = (int)(Math.random() * RANDOM_POSITION);
        apple_x = r * DOT_SIZE;

        r = (int)(Math.random()*RANDOM_POSITION);
        apple_y = r * DOT_SIZE;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void checkCollision(){
        for(int i=dots; i>0; i--){
            if((i>4) && (x[0] == x[i]) && (y[0] == y[i])){
                inGame = false;
            }
        }

        if(y[0] >= 300){
            inGame = false;
        }
        if(x[0] >= 300){
            inGame = false;
        }

        if(y[0] < 0){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }

        if(!inGame){
            timer.stop();
        }
    }

    public void draw(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y, this);

            for(int i=0; i<dots; i++){
                if(i==0){
                    g.drawImage(head, x[i], y[i], this);
                }else{
                    g.drawImage(dot, x[i], y[i], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }
    }

    public void gameOver(Graphics g){
        String msg1 = "Game Over!";
        String msg2 = "Press Enter to Restart...";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(msg1, (300 - metrics.stringWidth(msg1))/2, 300/2);
        g.drawString(msg2, (300 - metrics.stringWidth(msg2))/2, 300/2 + 20);
    }

    public void move(){
        for(int i=dots; i>0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        
        if(leftDirection){
            x[0] -= DOT_SIZE;
        }
        if(rightDirection){
            x[0] += DOT_SIZE;
        }
        if(upDirection){
            y[0] -= DOT_SIZE;
        }
        if(downDirection){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple() {
        if((apple_x == x[0]) && (apple_y == y[0])){
            dots++;
            locateApple();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        if(inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();

            if(key == KeyEvent.VK_LEFT && (!rightDirection)){
                leftDirection = true;
                upDirection = downDirection = false;
            }
            if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
                rightDirection = true;
                upDirection = downDirection = false;
            }
            if(key == KeyEvent.VK_UP && (!downDirection)){
                upDirection = true;
                rightDirection = leftDirection = false;
            }
            if(key == KeyEvent.VK_DOWN && (!upDirection)){
                downDirection = true;
                rightDirection = leftDirection = false;
            }

            if((key == KeyEvent.VK_ENTER) && (!inGame)){
                initGame();
            }
        }
    }
}
