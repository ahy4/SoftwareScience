package SoftwareScience;

/**
 * Created by noko on 2016/07/10.
 */
public class Main
{
    public static void main(String[] args)
    {
        try
        {
            Score[] scores = ParseData.parseData(FileManager.readFileSync("/Users/noko/Desktop/Seiseki.dat"));
            System.out.println(new ScoreAssessment(scores));
        } catch (Exception e)
        {
            System.out.println("ファイル読み込みエラー");
            e.printStackTrace();
        }
    }
}

