package com.burakenesdemir.stockmarket.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class MailContentBuilder {

    private TemplateEngine templateEngine;

    @Autowired
    public MailContentBuilder(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public <T> String buildTemplate(Map<String, T> variables, String templateName) {
        Context context = new Context();
        var keys = variables.keySet();
        keys.stream().forEach(o -> context.setVariable(o, variables.get(o)));
        return templateEngine.process(templateName, context);
    }
}