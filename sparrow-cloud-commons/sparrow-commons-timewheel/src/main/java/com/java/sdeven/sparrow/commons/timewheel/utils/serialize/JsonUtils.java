/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.java.sdeven.sparrow.commons.timewheel.utils.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.java.sdeven.sparrow.commons.timewheel.exception.ImpossibleException;
import com.java.sdeven.sparrow.commons.timewheel.exception.TimeWheelJobException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author tjq
 * @since 2020/4/16
 */
@Slf4j
public class JsonUtils {

    private static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(JsonParser.Feature.IGNORE_UNDEFINED, true)
            .build();

    private static final TypeReference<Map<String, Object>>  MAP_TYPE_REFERENCE  = new TypeReference<Map<String, Object>> () {};

    private JsonUtils(){

    }

    public static String toJSONString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        }catch (Exception e) {
            log.error("[PowerJob] toJSONString failed", e);
        }
        return null;
    }

    public static String toJSONStringUnsafe(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return JSON_MAPPER.writeValueAsString(obj);
        }catch (Exception e) {
            throw new TimeWheelJobException(e);
        }
    }

    public static byte[] toBytes(Object obj) {
        try {
            return JSON_MAPPER.writeValueAsBytes(obj);
        }catch (Exception e) {
            log.error("[PowerJob] serialize failed", e);
        }
        return null;
    }

    public static <T> T parseObject(String json, Class<T> clz) throws JsonProcessingException {
        return JSON_MAPPER.readValue(json, clz);
    }

    public static Map<String, Object> parseMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return new HashMap<>();
        }
        try {
            return JSON_MAPPER.readValue(json, MAP_TYPE_REFERENCE);
        } catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
        throw new ImpossibleException();
    }

    public static <T> T parseObject(byte[] b, Class<T> clz) throws IOException {
        return JSON_MAPPER.readValue(b, clz);
    }

    public static <T> T parseObject(byte[] b, TypeReference<T> typeReference) throws IOException {
        return JSON_MAPPER.readValue(b, typeReference);
    }

    public static <T> T parseObject(String json, TypeReference<T> typeReference) throws IOException {
        return JSON_MAPPER.readValue(json, typeReference);
    }

    public static <T> T parseObjectIgnoreException(String json, Class<T> clz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JSON_MAPPER.readValue(json, clz);
        }catch (Exception e) {
            log.error("unable to parse json string to object,current string:{}",json,e);
            return null;
        }

    }

    public static <T> T parseObjectUnsafe(String json, Class<T> clz) {
        try {
            return JSON_MAPPER.readValue(json, clz);
        }catch (Exception e) {
            ExceptionUtils.rethrow(e);
        }
        throw new TimeWheelJobException("impossible");
    }
}
