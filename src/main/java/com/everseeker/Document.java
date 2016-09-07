package com.everseeker;

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
        StringBuffer content = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath)), encoding));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            System.out.println(filePath);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        pageMap.put("AllPages", content.toString());
        docCache.put(filePath, pageMap);
    }
}
