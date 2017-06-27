package dk.magenta.datafordeler.cvr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dk.magenta.datafordeler.core.exception.InvalidServiceOwnerDefinitionException;
import dk.magenta.datafordeler.core.fapi.ServletConfiguration;
import dk.magenta.datafordeler.core.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;

/**
 * Created by lars on 19-05-17.
 */
@Configuration
@EnableWs
public class CvrServletConfiguration extends ServletConfiguration {

    @Autowired
    private CvrPlugin cvrPlugin;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private XmlMapper xmlMapper;

    @Override
    protected Plugin getPlugin() {
        return this.cvrPlugin;
    }

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    protected XmlMapper getXmlMapper() {
        return this.xmlMapper;
    }

    @Override
    protected String getServiceOwner() {
        return "cvr";
    }

    @Bean
    public ServletRegistrationBean cvrServlet() {
        try {
            return this.dispatcherServlet();
        } catch (InvalidServiceOwnerDefinitionException e) {
            e.printStackTrace();
        }
        return null;
    }
}

