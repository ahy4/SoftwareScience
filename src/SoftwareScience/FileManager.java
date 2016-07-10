package SoftwareScience;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by noko on 2016/06/20.
 */
public class FileManager
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
