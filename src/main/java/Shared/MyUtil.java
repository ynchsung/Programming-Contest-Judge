package Shared;

import java.io.*;

/**
 * Created by tenyoku on 2016/1/2.
 */
public class MyUtil {
    public static String readFromFile(File file) throws FileNotFoundException {
        InputStream is = new FileInputStream(file);
        StringBuilder sb = new StringBuilder(512);
        try {
            Reader r = new InputStreamReader(is, "UTF-8");
            int c = 0;
            while ((c = r.read()) != -1) {
                sb.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
