import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Time;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int castiTela = 6;
    int snezeneJablka;
    int jablkoX;
    int jablkoY;
    String direction = "R";
    boolean runing = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.green);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newJablko();
        move();
        runing = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (runing) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(Color.red);
            g.fillOval(jablkoX, jablkoY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < castiTela; i++) {
                if (i == 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Skóre: "+snezeneJablka, (SCREEN_WIDTH - metrics.stringWidth("Skóre: "+snezeneJablka))/2, g.getFont().getSize());
        }
        else gameOver(g);
    }
    public void newJablko(){

        jablkoX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        jablkoY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    public void move() {

        for(int i = castiTela;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case "U":
                y[0] = y[0] - UNIT_SIZE;
                break;
            case "D":
                y[0] = y[0] + UNIT_SIZE;
                break;
            case "L":
                x[0] = x[0] - UNIT_SIZE;
                break;
            case "R":
                x[0] = x[0] + UNIT_SIZE;
                break;
        }

    }

    public void checkJablko() {

        if ((x[0] == jablkoX) && (y[0] == jablkoY)) {
            castiTela++;
            snezeneJablka++;
            newJablko();
        }
    }

    public void checkKolize() {

       // kontroluje jestli se hlava srazila s telem
        for(int i = castiTela;i>0;i--) {
            if((x[0] == x[i])&& (y[0] == y[i])) {
                runing = false;
            }
        }

        //kontroluje jestli se hlava dotkla leve steny
        if (x[0] < 0) {
            runing = false;
        }

        //kontroluje jestli se hlava dotkla prave steny
        if(x[0] > SCREEN_WIDTH) {
            runing = false;
        }

        //kontroluje jestli se hlava dotkla horní stěny
        if(y[0] < 0) {
            runing = false;
        }

        //kontroluje jestli se hlava dotkla spodni steny
        if(y[0] > SCREEN_HEIGHT) {
            runing = false;
        }

        if(!runing) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {

        // skore
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Skóre: "+snezeneJablka, (SCREEN_WIDTH - metrics1.stringWidth("Skóre: "+snezeneJablka))/2, g.getFont().getSize());
        //game over napis
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e){

        if(runing) {
            move();
            checkJablko();
            checkKolize();
        }
        repaint();
     }

     public class MyKeyAdapter extends KeyAdapter {

        @Override
         public void keyPressed(KeyEvent e){

            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != "R") {
                        direction = "L";
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != "L") {
                        direction = "R";
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != "D") {
                        direction = "U";
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != "U") {
                        direction = "D";
                    }
                    break;
            }
        }
     }

}
