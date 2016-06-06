package com.lsxy.framework.mq.kafka;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonEncoder implements Encoder<Object> {
    private static final Logger LOGGER = Logger.getLogger(JsonEncoder.class);

    public JsonEncoder(VerifiableProperties verifiableProperties) {
        /* This constructor must be present for successful compile. */
    }

    @Override
    public byte[] toBytes(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object).getBytes();
        } catch (JsonProcessingException e) {
            LOGGER.error(String.format("Json processing failed for object: %s", object.getClass().getName()), e);
        }
        return "".getBytes();
    }
}