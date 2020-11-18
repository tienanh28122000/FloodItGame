import java.awt.*;

public class Action {

    private Color color;
    private boolean connected[][];


    public Action(Color c, boolean con[][]){
        color = new Color(c.getRed(), c.getGreen(), c.getBlue());

        int row = con.length;
        int col = con[0].length;

        connected = new boolean[row][col];

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                connected[i][j] = con[i][j];
            }
        }
    }


    public Color getColor() {
        return new Color(color.getRed(), color.getGreen(), color.getBlue());
    }


    public boolean[][] getConnected(){

        int row = connected.length;
        int col = connected[0].length;

        boolean connectedCopy[][] = new boolean[row][col];

        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                connectedCopy[i][j] = connected[i][j];
            }
        }

        return connectedCopy;
    }
}