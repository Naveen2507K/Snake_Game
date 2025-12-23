import javax.swing.*;

public class SnakeGame extends JFrame {
    
    public SnakeGame() {
        super("Snake Game");

        add(new Board());
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String args[]){
        SnakeGame sg = new SnakeGame();
        sg.setVisible(true);
    }
}
