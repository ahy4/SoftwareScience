package ForReport;

/**
 * Created by noko on 2016/07/10.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Bundled file for teacher's form.
 */
public class R1414048
{
    public static void main(String[] args)
    {
        Main.main(new String[]{});
    }
}

class FileManager
{
    public static String readFileSync(String filePath) throws IOException
    {
        String result = "";
        File file = new File(filePath);
        FileReader filereader = new FileReader(file);
        int ch;
        while ((ch = filereader.read()) != -1)
        {
            result += (char) ch;
        }
        filereader.close();
        return result;
    }
}

class Main
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

class MergeSort
{

    private static final int INSERTIONSORT_THRESHOLD = 7;

    public static <T extends Comparable> T[] mergeSort(T[] ary)
    {
        return mergeSort(ary, (a, b) -> a.compareTo(b));
    }

    public static <T> T[] mergeSort(T[] a, Comparator<? super T> c)
    {
        T[] src = a.clone();
        T[] result = a.clone();
        mergeSort(src, result, 0, a.length, 0, c);
        return result;
    }

    /**
     * Src is the source array that starts at index 0
     * Dest is the (possibly larger) array destination with a possible offset
     * low is the index in dest to start sorting
     * high is the end index in dest to end sorting
     * off is the offset to generate corresponding low, high in src
     */
    private static void mergeSort(Object[] src,
                                  Object[] dest,
                                  int low, int high, int off,
                                  Comparator c)
    {
        int length = high - low;

        // Insertion sort on smallest arrays
        if (length < INSERTIONSORT_THRESHOLD)
        {
            for (int i = low; i < high; i++)
                for (int j = i; j > low && c.compare(dest[j - 1], dest[j]) > 0; j--)
                    swap(dest, j, j - 1);
            return;
        }

        // Recursively sort halves of dest into src
        int destLow = low;
        int destHigh = high;
        low += off;
        high += off;
        int mid = (low + high) >>> 1;
        mergeSort(dest, src, low, mid, -off, c);
        mergeSort(dest, src, mid, high, -off, c);

        // If list is already sorted, just copy from src to dest.  This is an
        // optimization that results in faster sorts for nearly ordered lists.
        if (c.compare(src[mid - 1], src[mid]) <= 0)
        {
            System.arraycopy(src, low, dest, destLow, length);
            return;
        }

        // Merge sorted halves (now in src) into dest
        for (int i = destLow, p = low, q = mid; i < destHigh; i++)
        {
            if (q >= high || p < mid && c.compare(src[p], src[q]) <= 0)
                dest[i] = src[p++];
            else
                dest[i] = src[q++];
        }
    }

    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(Object[] x, int a, int b)
    {
        Object _ref = x[a];
        x[a] = x[b];
        x[b] = _ref;
    }

}

class ParseData
{
    public static Score[] parseData(String data)
    {
        String[] lines = data.split("\\n");
        Score[] scores = new Score[lines.length];
        int count = 0;
        for (int i = 0; i < lines.length; i++) if (!lines[i].equals(""))
        {
            int[] _ref = stringsToInts(lines[i].split("\\s+"));
            int[] ary = Arrays.copyOfRange(_ref, 1, _ref.length);
            scores[i] = new Score(ary);
            count++;
        }
        return Arrays.copyOf(scores, count); // 空データ分消す
    }

    private static int[] stringsToInts(String[] xs)
    {
        int[] result = new int[xs.length];
        for (int i = 0; i < xs.length; i++)
        {
            try {
                result[i] = Integer.parseInt(xs[i]);
            } catch (Exception e) {
                result[i] = 0;
            }
        }
        return result;
    }
}

class ScienceScore
{
    public String subject;
    public int score;

    ScienceScore(String subject, int score)
    {
        this.subject = subject;
        this.score = score;
    }
}

class Score
{
    private int id;
    private int math2B;
    private int math3B;
    private int english;
    private int physicsB;
    private int chemistryB;
    private int biologyB;
    private int geoscienceB;

    private HashMap<String, ScienceScore> selectedScience;
    private Integer sum = null;

    public Score(int[] ary)
    {
        bulkSet(ary);
        selectedScience = SelectedScoreOfScience.select(physicsB, chemistryB, biologyB, geoscienceB);
    }

    private void bulkSet(int[] ary)
    {
        this.id = ary[0];
        this.math2B = ary[1];
        this.math3B = ary[2];
        this.english = ary[3];
        this.physicsB = ary[4];
        this.chemistryB = ary[5];
        this.biologyB = ary[6];
        this.geoscienceB = ary[7];
    }

    public int getId()
    {
        return this.id;
    }

    public int getSum()
    {
        this.sum = math2B + math3B + english + selectedScience.get("first").score + selectedScience.get("second").score;
        return this.sum;
    }

    public HashMap<String, ScienceScore> getSelectedScience()
    {
        return selectedScience;
    }

    @Override
    public String toString()
    {
        return (
            "[ " +
                id + "," +
                math2B + "," +
                math3B + "," +
                english + "," +
                physicsB + "," +
                chemistryB + "," +
                biologyB + "," +
                geoscienceB +
                " ]"
        );
    }

}

class ScoreAssessment
{

    Score[] scores;
    private  HashMap<String, String> languageMapping;


    public ScoreAssessment(Score[] scores)
    {
        this.createLanguageMapping();
        this.scores = scores;
        this.sortByScore();
    }

    public void sortByScore()
    {
        this.scores = MergeSort.mergeSort(this.scores, (a, b) -> {
            return Integer.compare(b.getSum(), a.getSum());
        });
    }

    // 順位, id, 点数, 使用科目
    @Override
    public String toString()
    {
        String result = "順位,受験番号,点数,選択理科1,選択理科2";
        int rank = 1;
        int prevScore = 0;
        for (int i = 0; i < scores.length; i++)
        {
            Score score = scores[i];
            HashMap<String, ScienceScore> selectedScience = score.getSelectedScience();
            String science1 = subjectToJapanese(selectedScience.get("first").subject);
            String science2 = subjectToJapanese(selectedScience.get("second").subject);

            if (prevScore != score.getSum())
            {
                rank = i+1;
                if (rank > 300) break;
            }
            result += rank + "," + score.getId() + "," + score.getSum() + "," + science1 + "," + science2 + "\n";
            prevScore = score.getSum();
        }
        return result;
    }

    public String subjectToJapanese(String english)
    {
        return this.languageMapping.get(english);
    }

    private void createLanguageMapping()
    {
        String[] subjectEnglish = { "physics", "chemistry", "biology", "geoscience" };
        String[] subjectJapanese = { "物理B", "化学B", "生物学B", "地学B" };
        this.languageMapping = new HashMap<>();
        for (int i = 0; i < subjectEnglish.length; i++)
        {
            languageMapping.put(subjectEnglish[i], subjectJapanese[i]);
        }
    }
}

class SelectedScoreOfScience
{

    public static HashMap<String, ScienceScore> select(int physics, int chemistry, int biology, int geoscience)
    {
        HashMap<String, ScienceScore> data = new HashMap<String, ScienceScore>();

        int[] scoreAry = { physics, chemistry, biology, geoscience };
        String[] subjectAry = { "physics", "chemistry", "biology", "geoscience" };
        ScienceScore[] scores = new ScienceScore[scoreAry.length];
        for (int i = 0; i < scoreAry.length; i++)
        {
            scores[i] = new ScienceScore(subjectAry[i], scoreAry[i]);
        }
        ArrayList<HashMap<String, Integer>> sorted = new ArrayList<>();
        Arrays.sort(scores, (a, b) -> Integer.compare(a.score, b.score));

        data.put("first", scores[scores.length-1]);
        data.put("second", scores[scores.length-2]);
        return data;
    }

}


