package com.void01.bukkit.voidframework.core.dependency;

import com.void01.bukkit.voidframework.api.common.dependency.Dependency;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class DependencyPomParser {
    @SneakyThrows
    public static List<Dependency> parseCompileDependencies(@NonNull File file) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(file);
        org.w3c.dom.NodeList dependencyElements = document.getElementsByTagName("dependency");
        List<Dependency> results = new ArrayList<>();

        for (int i = 0; i < dependencyElements.getLength(); i++) {
            org.w3c.dom.Element dependencyElement = (org.w3c.dom.Element) dependencyElements.item(i);
            String groupId = getElementValue(dependencyElement, "groupId");
            String artifactId = getElementValue(dependencyElement, "artifactId");
            String version = getElementValue(dependencyElement, "version");
            String scope = getElementValue(dependencyElement, "scope");

            Objects.requireNonNull(groupId, "groupId cannot be null");
            Objects.requireNonNull(artifactId, "artifactId cannot be null");
            Objects.requireNonNull(version, "version cannot be null");

            // 默认或显示指定 compile 级别
            if (scope == null || scope.equals("compile")) {
                results.add(new Dependency(groupId, artifactId, version));
            }
        }

        return results;
    }

    private static String getElementValue(@NonNull org.w3c.dom.Element parentElement, @NonNull String tagName) {
        org.w3c.dom.NodeList elements = parentElement.getElementsByTagName(tagName);

        if (elements.getLength() > 0) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) elements.item(0);
            return element.getTextContent();
        }

        return null;
    }
}
