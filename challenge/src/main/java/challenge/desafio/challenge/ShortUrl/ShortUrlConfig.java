package challenge.desafio.challenge.ShortUrl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShortUrlConfig {
    @Bean
    CommandLineRunner commandLineRunner2(ShortUrlRepository shortUrlRepository){
        return null;
    }
}
