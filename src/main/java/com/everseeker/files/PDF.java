package com.everseeker.files;

import com.everseeker.App;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by everseeker on 16/9/5.
 */
public class PDF {
    private static Map<String, Map<String, String>> pdfCache = App.getCache();

    //读取文件内容到内存中
    public static void readFileToMemory(String filePath) {
        PdfReader reader = null;
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try {
            reader = new PdfReader(filePath);
            int pageSize = reader.getNumberOfPages();
            for (int i = 1; i <= pageSize; i++) {
                pageMap.put("Page " + i, PdfTextExtractor.getTextFromPage(reader, i).toLowerCase());

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
        pdfCache.put(filePath, pageMap);
    }
}
