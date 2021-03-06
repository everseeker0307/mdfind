package com.everseeker.files;

import com.everseeker.App;
import com.everseeker.tools.EncodingUtils;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by everseeker on 16/9/7.
 */
public class Document {
    private static Map<String, Map<String, String>> docCache = App.getCache();

    public static void readFileToMemory(String filePath) {
        String encoding = EncodingUtils.getFileEncode(filePath);
        if (encoding == null) {
            return;
        }

        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), encoding))) {
            String line;
            int rowNum = 0;
            while ((line = reader.readLine()) != null) {
                pageMap.put("Row " + (++rowNum), line.toLowerCase());
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(filePath);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        docCache.put(filePath, pageMap);
    }
}
