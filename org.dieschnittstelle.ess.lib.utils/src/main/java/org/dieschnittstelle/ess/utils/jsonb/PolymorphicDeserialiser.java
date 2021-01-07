package org.dieschnittstelle.ess.utils.jsonb;

import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;

/*
 * this is our own implementation of the @JsonTypeInfo semantics from Jackson using
 * json-b standard expressive means, see: https://stackoverflow.com/questions/62398858/deserialize-json-into-polymorphic-pojo-with-json-b-yasson, https://itsallbinary.com/java-json-polymorphic-type-handling-jackson-vs-gson-vs-json-b/, see also: https://javaee.github.io/jsonb-spec/users-guide.html
 */
public class PolymorphicDeserialiser<T> implements JsonbDeserializer<T> {

    public static final String KLASSNAME_PROPERTY = "@class";

    private static final Jsonb jsonb = JsonbBuilder.create();

    protected static Logger logger = org.apache.logging.log4j.LogManager.getLogger(PolymorphicDeserialiser.class);

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

}
