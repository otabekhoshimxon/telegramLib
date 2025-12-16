package travel.letstrip.config;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

@Slf4j
public class LibContextInitializer 
    implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
        log.info("🚀 base-lib pre-initializing (HIGHEST_PRECEDENCE)");
        
        String applicationName = applicationContext.getEnvironment()
            .getProperty("spring.application.name", "Unknown Service");
        
        log.info("📍 Target service: {} ", applicationName );
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}