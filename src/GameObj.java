import java.awt.Graphics;


public abstract class GameObj {
    private int px;
    private int py;

    // size của object
    private int width;
    private int height;


    private int maxX;
    private int maxY;


    public GameObj(int vx, int vy, int px, int py, int width, int height, int courtWidth,
                   int courtHeight) {
        this.px = px;
        this.py = py;
        this.width  = width;
        this.height = height;

        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }


    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public abstract void draw(Graphics g);
}