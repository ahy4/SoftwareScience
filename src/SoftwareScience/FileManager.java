package SoftwareScience;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by noko on 2016/06/20.
 */
public class FileManager
{
    public static String readFileSync(String filePath) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
    }
}
