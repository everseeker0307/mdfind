package com.everseeker.web;

import com.everseeker.App;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by everseeker on 16/9/8.
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/", method = GET)
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/", method = POST)
    public String home(@RequestParam("filepath") String filepath) {
        App.listAndSave(new File(filepath));
        return "redirect:/search/";
    }

    @RequestMapping(value = "/search", method = GET)
    public String search() {
        return "search";
    }

    @RequestMapping(value = "/search", method = POST)
    public String search(@RequestParam("keyword") String keyword, Map model) {
        try {
            //tomcat默认采用ISO-8859-1对中文进行编码, 由于jsp中实际编码格式为utf8, 因此需要重新编码; 也可以直接修改tomcat的默认编码
            keyword = new String(keyword.getBytes("iso-8859-1"), "UTF-8");
            model.put("keyFileList", App.find(keyword));
            model.put("keyword", keyword);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "search";
    }
}
