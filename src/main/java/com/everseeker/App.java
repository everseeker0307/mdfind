package com.everseeker;

import com.everseeker.files.*;

import java.io.File;
import java.io.FileFilter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class App {
    /**
     * 保存文件内容.
     * 数据结构为: Map<filePath, Map<pageNum, pageContext>>,
     * 针对pdf, ppt格式, 保存格式为: 文件名 => (page1, page1内容), (page2, page2内容), ...
     * 针对xls格式, 保存格式为: 文件名+sheet表名 => (row1, row1内容), (row2, row2内容), ...
     */
    private static Map<String, Map<String, String>> cache = new LinkedHashMap<String, Map<String, String>>();

    public static Map<String, Map<String, String>> getCache() {
        return cache;
    }

    //根据关键字查找
    public static Map<String, Map<String, String>> find(String keyword) {
        Map<String, Map<String, String>> map = new LinkedHashMap<String, Map<String, String>>();
        for (Map.Entry<String, Map<String, String>> file : cache.entrySet()) {
            Map<String, String> sourceContent = file.getValue();
            Map<String, String> destContent = new LinkedHashMap<String, String>();
            for (Map.Entry<String, String> entry : sourceContent.entrySet()) {
                if (entry.getValue().indexOf(keyword.toLowerCase()) > -1) {
                    destContent.put(entry.getKey(), entry.getValue());
                }
            }
            if (!destContent.isEmpty()) {
                map.put(file.getKey(), destContent);
            }
        }

        return map;
    }

    //输出查询结果
    public static void show(Map<String, Map<String, String>> result) {
        if (!result.isEmpty()) {
            for (Map.Entry<String, Map<String, String>> entry : result.entrySet()) {
                System.out.println("*******************************************************************************");
                System.out.println(entry.getKey());
                for (Map.Entry<String, String> page : entry.getValue().entrySet()) {
                    System.out.println(page.getKey());
                    System.out.println(page.getValue());
                    System.out.println("------------------------------------------------------------------");
                }
            }
        }
    }

    //遍历目录, 保存所有文件内容到内存
    public static void listAndSave(File dest) {
        if (dest.isDirectory()) {
            //列出所有pdf, ppt, pptx, xls...等文件
            File[] files = dest.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isDirectory()) {
                        listAndSave(file);
                    }
                    String reg = "\\.pdf|\\.ppt|\\.pptx|\\.xls|\\.xlsx|\\.txt|\\.log|\\.md|\\.doc|\\.docx$";
                    if (Pattern.compile(reg).matcher(file.getName().toLowerCase()).find())
                        return true;
                    return false;
                }
            });
            //针对不同格式的文件, 分别处理
            for (File file : files) {
                String filePath = file.getAbsolutePath();
//                System.out.println(filePath);
                if (filePath.endsWith(".pdf")) {
                    PDF.readFileToMemory(filePath);
                } else if (filePath.endsWith(".xls")) {
                    Excel.readFileToMemory(filePath);
                } else if (filePath.endsWith(".pptx")) {
                    PPT.readFilePPTXToMemory(filePath);
                } else if (filePath.endsWith(".ppt")) {
                    PPT.readFilePPTAllToMemory(filePath);
                } else if (filePath.endsWith(".doc")) {
                    Word.readFileDocToMemory(filePath);
                } else if (filePath.endsWith(".docx")) {
                    Word.readFileDocxToMemory(filePath);
                } else {
                    Document.readFileToMemory(filePath);
                }
            }
        }
    }

    public static void clearCache() {
        cache.clear();
    }

    public static void main(String[] args) {
        String dest = "/Users/everseeker/Telecom/mdfindtest";
        listAndSave(new File(dest));
        show(find("酒后驾驶"));
        System.out.println("   ".trim().equals(""));
    }
}
