package com.everseeker.files;

import com.everseeker.App;
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
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try (PowerPointExtractor extractor = new PowerPointExtractor(new FileInputStream(new File(filePath)))) {
            pageMap.put("AllPages", extractor.getText().toLowerCase());
        } catch (IOException e) {
            e.printStackTrace();
        }

        pptCache.put(filePath, pageMap);
    }

    //不按页读取, 直接读取全部.pptx的文本内容
    public static void readFilePPTXAllToMemory(String filePath) {
        Map<String, String> pageMap = new LinkedHashMap<String, String>();
        try (XSLFPowerPointExtractor extractor = new XSLFPowerPointExtractor(new XSLFSlideShow(filePath))) {
            pageMap.put("AllPages", extractor.getText().toLowerCase());
        } catch (XmlException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OpenXML4JException e) {
            e.printStackTrace();
        }

        pptCache.put(filePath, pageMap);
    }

    // 没成功
    public static void readFilePPTToMemory(String filePath) {
        try (HSLFSlideShow slideShow = new HSLFSlideShow(new HSLFSlideShowImpl(filePath))) {
            List<HSLFSlide> slides = slideShow.getSlides();
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

    // 按页读取.pptx中的文本内容
    public static void readFilePPTXToMemory(String filePath) {
        try (XMLSlideShow xmlSlideShow = new XMLSlideShow(new FileInputStream(new File(filePath)));) {
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
