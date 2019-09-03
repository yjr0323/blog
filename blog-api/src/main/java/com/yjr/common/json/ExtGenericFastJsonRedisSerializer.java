package com.yjr.common.json;

import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;


@Deprecated
public class ExtGenericFastJsonRedisSerializer extends GenericFastJsonRedisSerializer {

    private static int NO_SkipTransientFieldFEATURE;

    {
        int features = SerializerFeature.config(JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.SkipTransientField, false);
        NO_SkipTransientFieldFEATURE = SerializerFeature.config(features, SerializerFeature.WriteClassName, true);
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {

        if (object == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(object, NO_SkipTransientFieldFEATURE);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }

    }

}
