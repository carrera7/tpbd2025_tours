package unlp.info.bd2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import unlp.info.bd2.utils.DBInitializer;

@Configuration
public class DBInitializerConfig {
    @Bean
    @Primary
    public DBInitializer createDBInitializer() {
        return new DBInitializer();
    }
}
