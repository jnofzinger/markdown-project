package com.nofdev.markdown.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MarkdownService {

    private static final Logger logger = LoggerFactory.getLogger(MarkdownService.class);

    private static final String LINK_TEXT_REGEX = "\\[(.*?)\\]";
    private static final String LINK_URL_REGEX = "\\((.*?)\\)";
    private static final String HEADER_REGEX = "(#{1,6})";

    public String convertToHtml(String markdown) {
        logger.info("processing markdown:\n{}", markdown);
        String[] lines = markdown.split("\n");
        boolean openPar = false;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Check if line is header
            if (line.startsWith("#")) {
                line = convertHeader(line);
            } else {

                // If there is not an open paragraph and line isn't blank, open one
                if (!openPar && line.length() > 0) {
                    line = "<p>" + line;
                    openPar = true;
                }

                // If there is an open paragraph, close it if next line is blank or header or end of document
                if (openPar) {
                    if (i < lines.length - 1) {
                        String nextLine = lines[i + 1].trim();
                        if (nextLine.startsWith("#") || nextLine.length() == 0) {
                            line = line + "</p>";
                            openPar = false;
                        }
                    } else {
                        line = line + "</p>";
                    }
                }
            }

            // Convert links if there are any
            line = convertLinks(line);

            sb.append(line);
            if (i < lines.length - 1) {
                sb.append("\n");
            }
        }


        return sb.toString();
    }

   public String convertHeader(String line) {
       Matcher m = Pattern.compile(HEADER_REGEX).matcher(line);
       if (m.find()) {
           String header = m.group();
           StringBuilder sb = new StringBuilder();
           sb.append("<h").append(header.length()).append(">")
                   .append(line.replace(header, "").trim())
                   .append("</h").append(header.length()).append(">");
           return sb.toString();
       }
       return line;
   }

    public String convertLinks(String line) {

        List<String> matches = new ArrayList<String>();
        Matcher m = Pattern.compile(LINK_TEXT_REGEX + LINK_URL_REGEX)
                .matcher(line);
        while (m.find()) {
            matches.add(m.group());
        }

        for (String match : matches) {
            String text = extractLinkComponent(LINK_TEXT_REGEX, match);
            String url = extractLinkComponent(LINK_URL_REGEX, match);
            StringBuilder html = new StringBuilder();
            html.append("<a href=\"").append(url).append("\">").append(text).append("</a>");
            line = line.replace(match, html.toString());
        }
        return line;
    }

    private String extractLinkComponent(String regex, String link) {
        Matcher m = Pattern.compile(regex).matcher(link);
        while (m.find()) {
            return  m.group(1);
        }
        return "";
    }

}
