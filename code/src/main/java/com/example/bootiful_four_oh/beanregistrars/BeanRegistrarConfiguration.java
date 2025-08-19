package com.example.bootiful_four_oh.beanregistrars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanRegistrar;
import org.springframework.beans.factory.BeanRegistry;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.Locale;
import java.util.Map;

@Configuration
@Import(DemoBeanRegistrar.class)
class BeanRegistrarConfiguration {
}

class CartsRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Map<String, LocaleAwareCart> carts;

    CartsRunner(Map<String, LocaleAwareCart> carts) {
        this.carts = carts;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.carts.forEach((cartName, localeAwareCart) -> {
            this.logger.info("found cart named {}: {}", cartName , localeAwareCart);
        });
    }
}

class DemoBeanRegistrar implements BeanRegistrar {

    @Override
    public void register(BeanRegistry registry, Environment env) {
        var shouldRegister = env.getProperty("carts.register", Boolean.class, false);
        if (shouldRegister) {
            var localeStream = Locale.availableLocales()
                    .filter( l -> l.getLanguage().toLowerCase(Locale.ROOT).contains("fr") );
            localeStream.forEach(locale -> registry.registerBean(LocaleAwareCart.class,
                    spec -> spec
                            .description("a cart for " + locale.toLanguageTag())
                            .supplier(_ -> new LocaleAwareCart(locale))
            ));
        }
        registry.registerBean(CartsRunner.class);
    }
}

class LocaleAwareCart {

    private final Locale locale;

    LocaleAwareCart(Locale locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "LocaleAwareCart{" +
                "locale=" + this.locale.toLanguageTag() +
                '}';
    }
}