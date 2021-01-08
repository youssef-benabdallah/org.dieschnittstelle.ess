package org.dieschnittstelle.ess.utils.jsonb;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.management.AttributeList;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * this is our own implementation of the @JsonTypeInfo semantics from Jackson using
 * json-b standard expressive means, see: https://stackoverflow.com/questions/62398858/deserialize-json-into-polymorphic-pojo-with-json-b-yasson (the following ones are only partially useful: https://itsallbinary.com/java-json-polymorphic-type-handling-jackson-vs-gson-vs-json-b/, see also: https://javaee.github.io/jsonb-spec/users-guide.html)
 */
public class JsonbJsonTypeInfoHandler<T> implements JsonbDeserializer<T>, JsonbSerializer<T> {

    public static final String KLASSNAME_PROPERTY = "@class";

    private static final Jsonb jsonb = JsonbBuilder.create();

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(JsonbJsonTypeInfoHandler.class);

    public static class PolymorphicTypeException extends RuntimeException {
        public PolymorphicTypeException(String message) {
            super(message);
        }

        public PolymorphicTypeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context, Type rtType) {

        JsonObject jsonObj = parser.getObject();
        String jsonString = jsonObj.toString();

        // this is a custom implementation of the JsonTypeInfo logics that is possible for deserialisation
        // however, as serialisation does not seem to appear possible without duplicating large part of
        // serialisation logic, we just embed the jackson processing here

        String klassname = jsonObj.getString(KLASSNAME_PROPERTY);
        logger.info("deserialise(): klassname is: " + klassname);

        try {
            Class klass = Class.forName(klassname);
            T obj = (T)jsonb.fromJson(jsonString, klass);
            logger.info("deserialise(): deserialised instance of polymorphic type: " + obj);
            return obj;
        }
        catch (Exception e) {
            throw new PolymorphicTypeException("Cannot deserialise object of class: " + klassname + ". Got: " + e,e);
        }

    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        logger.info("serialise(): java object is: " + t);
        try {
            // we convert the object into a map, using the instance attributes, and ignoring for the time being
            // any attribute name mappings... there seems to be no straightforward way to access the result
            // of default generation for the given object and simply add the klassname attribute...
            Map<String,Object> intermediateObj = new HashMap<>();
            intermediateObj.put(KLASSNAME_PROPERTY,t.getClass());
            for (Method m : collectGetters(t.getClass())) {
                    intermediateObj.put(getFieldnameForGetter(m.getName()), m.invoke(t));
            }
            logger.info("serialise(): created intermediate map object: " + intermediateObj);
            serializationContext.serialize(intermediateObj,jsonGenerator);
        }
        catch (Exception e) {
            throw new PolymorphicTypeException("Cannot serialise object: " + t + ". Got: " + e,e);
        }
    }

    private static List<Method> collectGetters(Class klass) {
        if (klass == Object.class) {
            return new ArrayList<>();
        }
        else {
            List methods = Arrays.asList(klass.getDeclaredMethods())
                    .stream()
                    .filter(m -> Modifier.isPublic(m.getModifiers()) &&  m.getParameterCount() == 0 && m.getName().startsWith("get") || m.getName().startsWith("is"))
                    .collect(Collectors.toList());
            methods.addAll(collectGetters(klass.getSuperclass()));
            return methods;
        }
    }

    private static String getFieldnameForGetter(String getterName) {
        if (getterName.startsWith("is")) {
            return getterName.substring(2,3).toLowerCase() + getterName.substring(3);
        }
        else {
            return getterName.substring(3,4).toLowerCase() + getterName.substring(4);
        }
    }


}
