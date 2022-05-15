package com.nofdev.markdown.controller;

import com.nofdev.markdown.model.FormData;
import com.nofdev.markdown.service.MarkdownService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MarkdownController {

    private final MarkdownService markdownService;

    public MarkdownController(MarkdownService markdownService) {
        this.markdownService = markdownService;
    }

    @GetMapping("/markdown")
    public String getForm(Model model) {
        model.addAttribute("formData", new FormData());
        return "markdownConverter";
    }

    @PostMapping("/markdown")
    public String postForm(FormData formData, Model model) {
        formData.setHtml(markdownService.convertToHtml(formData.getMarkdown()));
        model.addAttribute("formData", formData);
        return "markdownConverter";

    }


}
