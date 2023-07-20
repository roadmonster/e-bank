package com.synpulse8.ebank.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.synpulse8.ebank.Enums.MoneyDirection;

import java.io.IOException;

public class MoneyDirectionSerializer extends JsonSerializer<MoneyDirection> {

    @Override
    public void serialize(MoneyDirection direction, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(direction.name());
    }
}
