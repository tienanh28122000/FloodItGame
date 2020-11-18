import java.util.*;
import java.io.*;

public class GameReader {

    private String fileName;
    private Set<String> scores = new TreeSet<>();
    private static BufferedReader reader;


    public GameReader(String fName){
        fileName = fName;
    }

    public List<String>  readScore() throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        String line;
        int score;
        List<Integer> scoresTemp = new LinkedList<>();

        while ((line = reader.readLine()) != null){
            score = Integer.parseInt(line.split(";")[0].
                    replace("Steps:", "").trim());
            scoresTemp.add(score);
            scores.add(line);
        }
        reader.close();
        Collections.sort(scoresTemp);
        List<String> topScores =  new LinkedList<>();
        int maxScores = Math.min(5, scoresTemp.size());

        for (int i = 0; i < maxScores; i++){
            int topScore = scoresTemp.get(i);
            Iterator<String> iterator = scores.iterator();
            while(iterator.hasNext()) {
                String setElement = iterator.next();
                String scoreSub  = "Steps:" + topScore;
                if (setElement.indexOf(scoreSub)!= -1){
                    topScores.add(setElement);
                }
            }
        }

        return topScores;
    }
}