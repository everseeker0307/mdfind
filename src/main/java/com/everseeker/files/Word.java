package com.everseeker.files;

import com.everseeker.App;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by everseeker on 16/9/7.
 */
public class Word {
    private static Map<String, Map<String, String>> wordCache = App.getCache();

    //读取.doc文档文本内容
    public static void readFileDocToMemory(String filePath) {
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try (WordExtractor extractor = new WordExtractor(new FileInputStream(new File(filePath)))) {
            pageMap.put("AllPages", extractor.getText().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordCache.put(filePath, pageMap);
    }

    //读取.docx文档文本内容
    public static void readFileDocxToMemory(String filePath) {
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try (XWPFWordExtractor extractor = new XWPFWordExtractor(new XWPFDocument(new FileInputStream(new File(filePath))))) {
            pageMap.put("AllPages", extractor.getText().toLowerCase());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        wordCache.put(filePath, pageMap);
    }
}
