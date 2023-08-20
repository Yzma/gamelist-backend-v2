package com.game.gamelist;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GamelistApplication.class)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class JacksonAutoConfigTest {

    @Autowired
    private ApplicationContext context;
    @Test
    public void defaultObjectMapperBuilder() throws Exception {
        Jackson2ObjectMapperBuilder builder =
                context.getBean(Jackson2ObjectMapperBuilder.class);
        ObjectMapper mapper = builder.build();
        assertThat(MapperFeature.DEFAULT_VIEW_INCLUSION.enabledByDefault()).isTrue();
        assertThat(mapper.getDeserializationConfig().isEnabled(
                MapperFeature.DEFAULT_VIEW_INCLUSION)).isFalse();
        assertThat(mapper.getSerializationConfig().isEnabled(
                MapperFeature.DEFAULT_VIEW_INCLUSION)).isFalse();
    }
}


