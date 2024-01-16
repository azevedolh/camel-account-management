package br.com.teste.camelaccountmanagement.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SwaggerController {

    @Value("${spring.mvc.servlet.path}")
    private String basePath;

    @Value("${camel.servlet.mapping.context-path}")
    private String camelBasePath;

    @RequestMapping("/swagger-ui")
    public RedirectView redirectToUi() {
        return new RedirectView(basePath + "/webjars/swagger-ui/index.html?url=" + camelBasePath.substring(0, camelBasePath.length() - 1) + "swagger&validatorUrl=", true, false);
    }
}
