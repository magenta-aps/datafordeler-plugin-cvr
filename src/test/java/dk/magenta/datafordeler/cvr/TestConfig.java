package dk.magenta.datafordeler.cvr;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by lars on 12-01-17.
 */
@ComponentScan({
        "dk.magenta.datafordeler",
        "dk.magenta.datafordeler.core", "dk.magenta.datafordeler.core.database", "dk.magenta.datafordeler.core.util",
        "dk.magenta.datafordeler.cvr", "dk.magenta.datafordeler.cvr.data", "dk.magenta.datafordeler.cvr.data.company"
})
@EntityScan("dk.magenta.datafordeler")
@ServletComponentScan
@SpringBootApplication
public class TestConfig {

}
