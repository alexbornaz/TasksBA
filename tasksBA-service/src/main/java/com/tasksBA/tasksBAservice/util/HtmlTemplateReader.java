package com.tasksBA.tasksBAservice.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Component
public class HtmlTemplateReader {
    private final ResourceLoader resourceLoader;

    public HtmlTemplateReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public String readHtmlTemplateFrom(String path) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + path);
        return asString(resource);

    }

    public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
