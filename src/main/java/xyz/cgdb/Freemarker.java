package xyz.cgdb;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;

import java.io.FileWriter;
import java.util.Map;

public class Freemarker {
    private final String dir;
    private final Configuration cfg;

    public Freemarker(String dir) {
        this.dir = dir;
        var cfg = new Configuration(Configuration.VERSION_2_3_29);
        cfg.setClassForTemplateLoading(this.getClass(), "/");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
        this.cfg = cfg;
    }

    void render(String template, String file, Map<Object, Object> data) throws Exception {
        try (var writer = new FileWriter(dir + file)) {
            cfg.getTemplate(template).process(data, writer);
        }
    }
}
