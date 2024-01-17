package br.com.teste.camelaccountmanagement.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;

public final class ObjectMapperConfig {
    private static ObjectMapper INSTANCE = new ObjectMapper();

    private ObjectMapperConfig() {}

    public static ObjectMapper getInstance() {
        if (INSTANCE.getRegisteredModuleIds() == null ||
            (INSTANCE.getRegisteredModuleIds() != null &&
            !INSTANCE.getRegisteredModuleIds().contains("com.fasterxml.jackson.datatype.jsr310.JavaTimeModule"))) {
            INSTANCE.registerModules(new JavaTimeModule().addSerializer(OffsetDateTimeSerializer.INSTANCE));
        }

        return INSTANCE;
    }
}
