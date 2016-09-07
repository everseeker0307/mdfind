package com.everseeker;

import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hslf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideShow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by everseeker on 16/9/6.
 */
public class PPT {
    private static Map<String, Map<String, String>> pptCache = App.getCache();

    //不按页读取, 直接读取全部.ppt的文本内容
    public static void readFilePPTAllToMemory(String filePath) {
        PowerPointExtractor extractor = null;
        try {
            extractor = new PowerPointExtractor(new FileInputStream(new File(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        pageMap.put("AllPages", extractor.getText());
        pptCache.put(filePath, pageMap);
    }

    //不按页读取, 直接读取全部.pptx的文本内容
    public static String readFilePPTXAllToMemory(String filePath) {
        XSLFPowerPointExtractor extractor = null;
        try {
            extractor = new XSLFPowerPointExtractor(new XSLFSlideShow(filePath));
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        }
        return extractor.getText();
    }

    public static void readFilePPTToMemory(String filePath) {
        try {
            HSLFSlideShow slideShow = new HSLFSlideShow(new HSLFSlideShowImpl(filePath));
            List<HSLFSlide> slides = slideShow.getSlides();
            List<HSLFNotes> notes = slideShow.getNotes();
            int pageNum = 0;
            Map<String, String> pageMap = new LinkedHashMap<String, String>();

            for (HSLFSlide slide : slides) {
                StringBuffer pageContent = new StringBuffer();
                List<List<HSLFTextParagraph>> textParasList = slide.getTextParagraphs();
                for (List<HSLFTextParagraph> textParas : textParasList) {
                    for (HSLFTextParagraph textParagraph : textParas) {
                        List<HSLFTextRun> textRuns = textParagraph.getTextRuns();
                        for (HSLFTextRun textRun : textRuns) {
                            pageContent.append(textRun.getRawText());
                        }
                    }
                }
                pageMap.put("page " + (++pageNum), pageContent.toString().toLowerCase());
                System.out.println(pageContent.toString());
            }
            pptCache.put(filePath, pageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFilePPTXToMemory(String filePath) {
        try {
            XMLSlideShow xmlSlideShow = new XMLSlideShow(new FileInputStream(new File(filePath)));
            List<XSLFSlide> slides = xmlSlideShow.getSlides();
            Map<String, String> pageMap = new LinkedHashMap<String, String>();
            int pageNum = 0;
            for (XSLFSlide slide : slides) {
                CTSlide rawSlide = slide.getXmlObject();
                CTGroupShape gs = rawSlide.getCSld().getSpTree();
                CTShape[] shapes = gs.getSpArray();
                StringBuffer pageContent = new StringBuffer();
                for (CTShape shape : shapes) {
                    CTTextBody tb = shape.getTxBody();
                    if (tb == null)
                        continue;
                    CTTextParagraph[] paras = tb.getPArray();
                    for (CTTextParagraph textParagraph : paras) {
                        CTRegularTextRun[] textRuns = textParagraph.getRArray();
                        for (CTRegularTextRun textRun : textRuns) {
                            pageContent.append(textRun.getT());
                        }
                    }
                }
                pageMap.put("page " + (++pageNum), pageContent.toString().toLowerCase());
            }
            pptCache.put(filePath, pageMap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
