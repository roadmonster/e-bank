package com.synpulse8.ebank.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.synpulse8.ebank.Enums.CountryCode;

import java.io.IOException;

public class CountryCodeSerializer extends JsonSerializer<CountryCode> {
    @Override
    public void serialize(CountryCode code, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(code.name());
    }
}
