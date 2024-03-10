package com.jk.sns.configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceResolver resolver = new ReactResourceResolver();
        registry.addResourceHandler("/**")
            .resourceChain(true)
            .addResolver(resolver);
    }

    public class ReactResourceResolver implements ResourceResolver {

        private static final String REACT_DIR = "/static/";
        private static final String REACT_STATIC_DIR = "static";

        private Resource index = new ClassPathResource(REACT_DIR + "index.html");
        private List<String> rootStaticFiles = Arrays.asList("favicon.io",
            "asset-manifest.json", "manifest.json", "service-worker.js");

        private Resource resolve(String requestPath, List<? extends Resource> locations) {
            if (requestPath == null) {
                return null;
            }
            if (rootStaticFiles.contains(requestPath)
                || requestPath.startsWith(REACT_STATIC_DIR)) {
                return new ClassPathResource(REACT_DIR + requestPath);
            } else {
                return index;
            }
        }

        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath,
            List<? extends Resource> locations, ResourceResolverChain chain) {
            return resolve(requestPath, locations);
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations,
            ResourceResolverChain chain) {
            Resource resolvedResource = resolve(resourcePath, locations);
            if (resolvedResource == null) {
                return null;
            }
            try {
                return resolvedResource.getURL().toString();
            } catch (IOException e) {
                return resolvedResource.getFilename();
            }
        }
    }
}