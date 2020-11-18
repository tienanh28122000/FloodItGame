import java.awt.*;

public class Square extends GameObj {
    public static final int SIZE = 20;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;
    private Color color;

    public Square(int courtWidth, int courtHeight, int posX, int posY,  Color color) {
        super(INIT_VEL_X, INIT_VEL_Y,posX + GameCourt.OFFSET_SQUARE, posY, SIZE, SIZE,
                courtWidth, courtHeight);

        this.color = color;
    }


    public Color getColor(){
        return new Color(color.getRed(), color.getGreen(), color.getBlue());
    }


    public void setColor(Color c){
        this.color = c;
    }


    public int getX(){
        return (this.getPx() - GameCourt.OFFSET_SQUARE)/SIZE;
    }


    public int getY(){
        return this.getPy()/SIZE;
    }



    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}