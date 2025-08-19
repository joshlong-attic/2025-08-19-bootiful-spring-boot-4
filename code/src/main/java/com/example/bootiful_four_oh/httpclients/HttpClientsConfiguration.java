package com.example.bootiful_four_oh.httpclients;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.registry.AbstractHttpServiceRegistrar;

//@ImportHttpServices(group = "cats", types = {CatFactClient.class})
@Import(HttpClientsConfiguration.MyRegistrar.class)
@Configuration
class HttpClientsConfiguration {

    static class MyRegistrar extends AbstractHttpServiceRegistrar {

        @Override
        protected void registerHttpServices(GroupRegistry registry, AnnotationMetadata importingClassMetadata) {
            registry.forGroup("cats").register(CatFactClient.class);
        }
    }

    @Bean
    RestClientHttpServiceGroupConfigurer configurer() {
        return groups -> groups.filterByName("cats").forEachClient(
                (_, clientBuilder) -> clientBuilder
                    .defaultHeaders(httpHeaders ->
                            httpHeaders.setBasicAuth("user", "password")
                    )
        );
    }

    @Bean
    ApplicationRunner runner(CatFactClient client) {
        return _ -> System.out.println(client.fact());
    }
}

interface CatFactClient {

    @GetExchange("https://catfact.ninja/fact")
    CatFact fact();
}

record CatFact(String fact, int length) {
}
