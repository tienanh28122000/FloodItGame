import java.awt.*;

public class Circle extends GameObj {
    public static final int SIZE = 30;
    public static final int INIT_VEL_X = 2;
    public static final int INIT_VEL_Y = 3;

    private Color color;

    public Circle(int courtWidth, int courtHeight, int offsetXPos, int xPos, int yPos, Color color){
        super(INIT_VEL_X, INIT_VEL_Y,  xPos + offsetXPos, yPos,SIZE, SIZE,
                courtWidth, courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}