package com.synpulse8.ebank.Serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.synpulse8.ebank.Enums.Currency;

import java.io.IOException;

public class CurrencySerializer extends JsonSerializer<Currency> {

    @Override
    public void serialize(Currency currency, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, IOException {
        jsonGenerator.writeString(currency.name());
    }
}

