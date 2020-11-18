import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class Game implements Runnable {
    public void run() {
        final JFrame frame = new JFrame("Flood-it!");

        // Panel thông báo số bước đi
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.NORTH);
        final JLabel status = new JLabel("Steps: ");
        status_panel.add(status);
        status_panel.setBackground(Color.PINK);

        // Khu vực chính của trò chơi
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);


        // Panel chứa danh sách Highscores
        String leaderBoard = getLeaderBoard(court);
        JTextArea leaderArea = new JTextArea(leaderBoard);
        leaderArea.setSize(230, 100);
        leaderArea.setLineWrap(true);
        leaderArea.setWrapStyleWord(true);
        JScrollPane leaderPanel = new JScrollPane(leaderArea);
        final JPanel leader_panel = new JPanel();
        frame.add(leader_panel, BorderLayout.WEST);
        leader_panel.add(leaderPanel);
        leader_panel.setBackground(Color.PINK);

        // Panel chứa button restart và return
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.EAST);
        control_panel.setBackground(Color.PINK);


        final JButton reset = new JButton("Restart");
        final JButton undo = new JButton("Return");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.reset();
            }
        });
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                court.undo();
            }
        });
        control_panel.add(undo);
        control_panel.add(reset);

        // Đưa Frame lên màn hình
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Bắt đầu game
        court.reset();
    }


    public String getLeaderBoard(GameCourt court){

        String level = court.gameLevel;
        String fileName = "progress/leaderboard" + level + ".txt";
        GameReader reader = new GameReader(fileName);
        List<String> scores = new LinkedList<>();
        try{
            scores = reader.readScore();
        }catch (IOException e){
            System.out.println("Cannot load Highscores");
        }

        Iterator<String> iterator = scores.iterator();
        String leaderBoard = "Highscores:";
        int i = 0;

        while(iterator.hasNext() && i < 5) {
            String setElement = iterator.next();
            leaderBoard = leaderBoard + "\n \n" + setElement;
            i++;
        }
        return leaderBoard;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}

