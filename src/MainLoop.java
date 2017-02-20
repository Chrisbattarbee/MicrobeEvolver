import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

public class MainLoop extends Frame {

  public static void main(String[] args) {
    MainLoop loop = new MainLoop();
    loop.setVisible(true);
  }

  public static final int SCREEN_WIDTH = 600;
  public static final int SCREEN_HEIGHT = 400;

  private boolean running = true;

  private GameManager gameManager;

  public MainLoop() {
    super("Microbe Evolution Arena");

    setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

    setIgnoreRepaint(true);

    setResizable(false);

    addKeyListener(new KeyAdapter() {

      @Override
      public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();

        // Not rendering increases simulation speed
        if (keyCode == e.VK_SPACE) {
          gameManager.setRender(!gameManager.getRender());
        }
      }

    });

    init();

    this.addWindowListener( new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent we) {
        System.exit(0);
      }
    } );

    runLoop();
  }

  public void init() {
    gameManager = new GameManager(100, 100, SCREEN_WIDTH, SCREEN_HEIGHT);
  }

  public void runLoop() {
    Thread loopThread = new Thread()
    {
      public void run()
      {
        loop();
      }
    };
    loopThread.start();
  }

  public void loop() {

    long timer = System.currentTimeMillis();

    while (running) {

      render();

      // Every second print the best microbe food score
      if (System.currentTimeMillis() - timer > 1000) {
        if (gameManager.getBest() != null) {
          System.out.println(gameManager.getBest().getTotalEnergy());
        }
        timer += 1000;
      }
    }
  }

  public void render() {
    this.repaint();
  }

  public void paint(Graphics g) {
    /*
     * Clear the screen from the previous draw call
     */
    BufferStrategy bs = getBufferStrategy();
    if (bs == null){
      createBufferStrategy(2);
      return;
    }

    g = bs.getDrawGraphics();

    //clear screen
    g.clearRect(0, 0, getWidth(), getHeight());

    //Draw gameState
    if (g != null) {
      gameManager.tick(g);
      render();

    }

    g.setColor(Color.BLACK);
    g.drawString("TICKS: " + gameManager.getTicks(), 10, 55);

    g.dispose();
    bs.show();
  }

}
