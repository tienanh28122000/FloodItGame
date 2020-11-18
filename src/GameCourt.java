import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class GameCourt extends JPanel{


    private int X; // số lượng hình vuông theo chiều ngang
    private int Y; // số lượng hình vuông theo chiều dọc
    private static int MIN_COLORS; // số lượng min của màu
    private static int MAX_COLORS ; // số lượng max của màu
    public static int OFFSET_SQUARE = 0; // đường bao
    private int counter = 0; // tính toán cho số bước thực hiện
    public static int MAX_STEPS; // sô bước tối đa được cho phép
    private int OFFSET_CIRCLE; // offset được sử dụng để tính toán nơi đặt button tròn
    private Color currentColor; // color hiện tại
    private Color [] colors = new Color[10]; // cho phép 10 colors khác nhau
    private Square [][] squares; // lưu trữ mảng 2D đối tượng ô vuông
    private Circle [] circles; // lưu trữ mảng 1D đối tượng buttun tròn
    private boolean visited[][]; // mảng boolean 2D để mask các thành phần đang kết nối
    private Stack<Action>actions = new Stack<>(); // các hành động được thực hiện bởi người chơi
    private JLabel status; // thanh status của game, dùng để record điểm
    private String userName; // người chơi
    public String gameLevel; // các level: Hard, Medium, Easy

    // hằng số của game
    private static int COURT_WIDTH;
    private static int COURT_HEIGHT;

    public GameCourt(JLabel status) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.status = status;
        setGameLevel();
        setUserName();
        setGameStates(gameLevel);
//        StdAudio.play("TheAvengersOST.wav");
        setColors();
        setCourtSizes();
        setInitXPosCircles();
        populateGrid();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                int gameState = getGameStatus();
                if (gameState == 1) {
                    int pos = getCircleIndex(me.getX(), me.getY());
                    if (pos != -1) {
                        currentColor = colors[pos];
                        traverseBoardDFS();
                        actions.push(new Action(squares[0][0].getColor(), visited));
                        updateBoard(currentColor);
                        counter++;
                        updateScore();
                    }
                }
                else if (gameState == 0){
                    status.setText("Chiến thắng :>>>");
//                    StdAudio.play("JustWin.wav");
                    try{
                        writeScore(userName, counter);
                    }
                    catch (IOException e){
                        System.out.println("Lỗi");
                    }
                }
                else{
                    status.setText("Thua cuộc :<<<");
                }
                repaint();
            }
        });
    }

    private void setUserName(){
        String userFirstName = JOptionPane.showInputDialog("Enter your first name");
        String fullName;

        if (userFirstName == null){
            fullName = "No Name";
        }
        else{
            fullName = userFirstName;
        }

        this.userName = fullName;
    }

    // Nếu nhập không đúng yêu cầu, sẽ chơi level Easy
    private void setGameLevel(){
        String level = JOptionPane.showInputDialog("Difficulty level: H: hard " +
                "M: medium E: easy");
        if (level == null){
            this.gameLevel = "E";
        }
        else{
            this.gameLevel = level;
        }
    }

    // update các bước
    private void updateScore(){
        String myText = "Steps: " + counter + "/" + MAX_STEPS;
        status.setText(myText);
    }

    // update color
    private void updateBoard(Color c){

        for (int i = 0; i < X; i++){
            for (int j = 0; j < Y; j++){
                if (visited[i][j]){
                    squares[i][j].setColor(c);
                }
            }
        }
    }

    // khởi tạo 10 colors khác nhau
    private void setColors(){
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        colors[2] = Color.CYAN;
        colors[3] = Color.GRAY;
        colors[4] = Color.GREEN;
        colors[5] = Color.BLACK;
        colors[6] = Color.MAGENTA;
        colors[7] = Color.ORANGE;
        colors[8] = Color.PINK;
        colors[9] = Color.WHITE;
    }

    // tạo size cho game board
    private void setCourtSizes(){
        this.COURT_WIDTH  = X *  (20 + 2 * OFFSET_SQUARE);
        this.COURT_HEIGHT = Y *  (20 + 2);
    }

    // tạo giá trị khi click button tròn
    int getCircleIndex(int x, int y){
        for (int i = 0; i <= MAX_COLORS; i++){
            if (x >= i * 30 + OFFSET_CIRCLE
                    & x < i * 30 + 30 + OFFSET_CIRCLE
                    & y >= Y * 20 & y < Y * 20 + 30){
                return i;
            }
        }
        return -1;
    }

    // đưa ra color, kiểm tra chúng có cùng màu sắc không
    // trả lại true nếu hai object color có cùng màu, false nếu ngược lại
    boolean isSameColor(Color c1, Color c2){
        return c1.equals(c2);
    }

    boolean isOutBound(int x, int y){

        if (x < 0 || x >= X || y < 0 || y >= Y){
            return true;
        }
        return false;
    }

    // kiểm tra nếu tất cả object ô vuông có cùng màu trên board
    // trả lại true nếu tất cả cùng màu và false nếu ngược lại
    private boolean isSameColor(){
        Color c = squares[0][0].getColor();

        for (int i = 0 ; i < X; i++){
            for (int j = 0; j < Y; j++){
                Square s = squares[i][j];
                if (!c.equals(s.getColor())){
                    return false;
                }
            }
        }

        return true;
    }

    private int getGameStatus(){
        if (counter > MAX_STEPS){
            return -1;
        }
        if (isSameColor()){
            return 0;
        }

        return 1;
    }

    // đưa ra một position (x,y) trên board, thêm tất cả các ô liền kề vào danh sách kề
    // liền kề được định nghĩa là các ô lân cận và có cùng màu
    private List<Square> addAdjSquare(int x, int y, List<Square> adjList, Color c, Direction d){
        int x_new = -1;
        int y_new = -1;

        if (d == Direction.UP){
            x_new = x;
            y_new = y - 1;
        }
        else if(d == Direction.DOWN){
            x_new = x;
            y_new = y + 1;
        }
        else if (d == Direction.LEFT){
            x_new = x - 1;
            y_new = y;
        }
        else if (d == Direction.RIGHT){
            x_new = x + 1;
            y_new = y;
        }

        if (isOutBound(x_new, y_new)){
            return adjList;
        }
        else{
            Square square = squares[x_new][y_new];
            if (isSameColor(c, square.getColor())){
                adjList.add(square);
            }
            return adjList;
        }
    }

    // reset mảng boolean 2D
    private void resetVisited(){
        for (int i = 0 ; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                visited[i][j] = false;
            }
        }
    }

    // hàm hỗ trợ DFS
    private void DFSHelper(int x, int y, boolean visited[][], Color c){

        this.visited[x][y] = true;

        List<Square> adjList = new LinkedList<>();

        for (Direction direction : Direction.values()) {
            adjList = addAdjSquare(x, y, adjList, c, direction);
        }

        for (int i = 0; i < adjList.size(); i++){
            int x_new = adjList.get(i).getX();
            int y_new = adjList.get(i).getY();
            if (!visited[x_new][y_new]){
                DFSHelper(x_new, y_new, visited, c);
            }
        }
    }

    // hàm chính của thuật toán DFS
    private void traverseBoardDFS(){
        Square q = squares[0][0];
        Color c = q.getColor();
        resetVisited();

        DFSHelper(q.getX(), q.getY(), visited, c);
    }



    // tạo 3 level cho game, level càng khó thì càng nhiều màu và nhiều ô
    private void setGameStates(String level){
        if (level.equals("H")){
            this.MAX_COLORS = 8;
            this.MIN_COLORS = 0;
            this.MAX_STEPS  = 200;
            this.X = 30;
            this.Y = 30;
        }
        else if (level.equals("M")){
            this.MAX_COLORS = 4;
            this.MIN_COLORS = 0;
            this.MAX_STEPS  = 50;
            this.X = 20;
            this.Y = 20;
        }
        else{
            this.MAX_COLORS = 2;
            this.MIN_COLORS = 0;
            this.MAX_STEPS  = 20;
            this.X = 20;
            this.Y = 20;
            this.gameLevel = "E";
        }
        visited = new boolean[X][Y];
        squares = new Square[X][Y];
        circles = new Circle[MAX_COLORS + 1];
    }

    // viết điểm của người chơi vào leaderboard
    // có 3 leaderboard tương ứng với 3 level
    private void writeScore(String userName, int counter) throws IOException{
        String fileName = "progress/leaderboard" + this.gameLevel + ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        String fileContent = "Steps:" + counter + "; Name:" + userName +"\n";
        writer.write(fileContent);
        writer.close();
    }


    private void initCircles() {

        for (int i = 0; i <= MAX_COLORS; i++){
            circles[i] = new Circle(COURT_WIDTH, COURT_HEIGHT, OFFSET_CIRCLE, i * 30,
                    Y * 20, colors[i]);
        }
    }

    private void initSquares() {
        for (int i = 0 ; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                int ind = (int)(Math.random()*((MAX_COLORS - MIN_COLORS) + 1)) + MIN_COLORS;
                visited[i][j] = false;
                squares[i][j] = new Square(COURT_WIDTH, COURT_HEIGHT,i * 20,
                        j * 20,colors[ind]);
            }
        }
    }


    private void setInitXPosCircles(){
        this.OFFSET_CIRCLE = (this.COURT_WIDTH - MAX_COLORS * 30)/2;;
    }


    private void populateGrid() {
        initSquares();
        initCircles();
        traverseBoardDFS();
    }


    private void drawSquares(Graphics g) {
        for (int i = 0 ; i < X; i++) {
            for (int j = 0; j < Y; j++) {
                squares[i][j].draw(g);
            }
        }
    }


    private void drawCircles(Graphics g){
        for (int i = 0; i <= MAX_COLORS; i++){
            circles[i].draw(g);
        }
    }



    public void reset() {
        counter = 0;
        populateGrid();
        updateScore();
        repaint();
    }

    // return buttun
    public void undo() {
        if (actions.size() <= 0){

        }
        else{
            counter--;
            Action lastAction = actions.pop();
            Color c = lastAction.getColor();
            boolean connections[][] = lastAction.getConnected();
            visited = Arrays.copyOf(connections, connections.length);
            updateBoard(c);
            updateScore();
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSquares(g);
        drawCircles(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}