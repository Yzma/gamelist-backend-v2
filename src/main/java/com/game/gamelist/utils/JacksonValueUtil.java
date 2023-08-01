package com.game.gamelist.utils;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonValue;

public class JacksonValueUtil {
    private JacksonValueUtil() {
        throw new UnsupportedOperationException("Utility class");
    }
    public static MappingJacksonValue getMappingJacksonValue(Object object) {
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(object);

        SimpleBeanPropertyFilter filter =
                SimpleBeanPropertyFilter.filterOutAllExcept("id", "firstName", "lastName", "email_address", "roles");

        FilterProvider filters =
                new SimpleFilterProvider().addFilter("UserInfoNeeded", filter);

        mappingJacksonValue.setFilters(filters);

        return mappingJacksonValue;
    }
}
