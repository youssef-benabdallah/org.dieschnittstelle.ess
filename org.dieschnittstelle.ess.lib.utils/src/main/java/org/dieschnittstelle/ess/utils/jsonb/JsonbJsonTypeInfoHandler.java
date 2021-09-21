package org.dieschnittstelle.ess.utils.jsonb;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbTypeDeserializer;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.bind.serializer.JsonbSerializer;
import javax.json.bind.serializer.SerializationContext;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static org.dieschnittstelle.ess.utils.Utils.show;

/*
 * this is our own implementation of the @JsonTypeInfo semantics from Jackson using
 * json-b standard expressive means, see: https://stackoverflow.com/questions/62398858/deserialize-json-into-polymorphic-pojo-with-json-b-yasson (the following ones are only partially useful: https://itsallbinary.com/java-json-polymorphic-type-handling-jackson-vs-gson-vs-json-b/, see also: https://javaee.github.io/jsonb-spec/users-guide.html)
 *
 * starting from tomee maven plugin version 8.0.2, there is a loop issue as the fromJson() method also
 * delegates to this handler for concrete types on which the deserialisation annotation has NOT been set!
 * For this reason, we keep version 8.0.1 for the time being.
 */
public class JsonbJsonTypeInfoHandler<T> implements JsonbDeserializer<T>, JsonbSerializer<T> {

    public static final String KLASSNAME_PROPERTY = "@class";

    private static final Jsonb jsonb = JsonbBuilder.create();

    private ObjectMapper jacksonMapper = new ObjectMapper();

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

        logger.info("deserialise(): type is: " + rtType);

        JsonObject jsonObj = parser.getObject();
        String jsonString = jsonObj.toString();
        logger.info("deserialise(): jsonString is: " + jsonString);

        if (rtType instanceof Class
                && !Modifier.isAbstract(((Class)rtType).getModifiers())) {
            if (superclassUsesCustomisedDeserialiser((Class) rtType)) {
                // if we do not have an abstract class, we just use the type for deserialisation
                logger.info("deserialise(): we do not have an abstract type. Use standard deserialisation implemented by " + jsonb.getClass());
//            T obj = (T)jsonb.fromJson(jsonString, ((Class)rtType));
                try {
                    T obj = (T) jacksonMapper.readValue(jsonString, ((Class) rtType));
                    logger.info("deserialise(): deserialised json to concrete type using jackson as johnzon recursion workaround: " + obj);
                    return obj;
                } catch (Exception e) {
                    String err = "got exception trying to deserialise json using jackson as johnzon workaround: " + e;
                    throw new PolymorphicTypeException(err, e);
                }
            }
            else {
                T obj = (T)jsonb.fromJson(jsonString, ((Class)rtType));
                logger.info("deserialise(): deserialised json to concrete type using johnzon: " + obj);
                return obj;
            }
        }
        else {
            // this is a custom implementation of the JsonTypeInfo logics that is possible for deserialisation
            // however, as serialisation does not seem to appear possible without duplicating large part of
            // serialisation logic, we just embed the jackson processing here
            logger.info("deserialise(): we have an abstract type. Lookup classname of concrete class...");

            String klassname = jsonObj.getString(lookupClassnameProperty((Class)rtType));
            logger.info("deserialise(): klassname is: " + klassname);

            try {
                Class klass = Class.forName(klassname);
                logger.info("deserialise(): klass is: " + klass);
                T obj = (T)jsonb.fromJson(jsonString, klass);
                logger.info("deserialise(): deserialised instance of polymorphic type: " + obj);
                return obj;
            }
            catch (Exception e) {
                throw new PolymorphicTypeException("Cannot deserialise object of class: " + klassname + ". Got: " + e,e);
            }
        }
    }

    public boolean superclassUsesCustomisedDeserialiser(Class klass) {
        if (klass == null) {
            return false;
        }
        else if (klass == Object.class) {
            return false;
        }
        else if (klass.isAnnotationPresent(JsonbTypeDeserializer.class) &&
                ((JsonbTypeDeserializer)klass.getAnnotation(JsonbTypeDeserializer.class)).value() == JsonbJsonTypeInfoHandler.class) {
            return true;
        }
        else {
            return superclassUsesCustomisedDeserialiser(klass.getSuperclass());
        }
    }

    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
        logger.info("serialise(): java object is: " + t + ", generator is: " + jsonGenerator);
        try {
            // we need to embed the complete serialisation logics here as there does not seem
            // to exist a way how to simply add a single property (specifying the classname) and
            // then run the normal serialisation for the object. It seems that serialise is called
            // after the object boundaries have already been marked in the generator, therefore
            // we also need to embed the logics for embedded objects and arrays, which may then,
            // however, delegate to the serialisation context. I.e. only those objects whose class
            // uses the JsonTypeInfo annotation need to be handled.
            jsonGenerator.write(lookupClassnameProperty(t.getClass()),t.getClass().getName());
            for (Method m : collectGetters(t.getClass())) {
                String fieldname = getFieldnameForGetter(m.getName());
                Object fieldvalue = m.invoke(t);
                if (fieldvalue != null) {
                    Type fieldtype = m.getGenericReturnType();
                    // cover the primitive types
                    // TODO: for full coverage, this would need to be extended
                    if (fieldtype == Integer.TYPE || fieldtype == Integer.class) {
                        jsonGenerator.write(fieldname,(int)fieldvalue);
                    }
                    else if (fieldtype == Long.TYPE || fieldtype == Long.class) {
                        jsonGenerator.write(fieldname,(long)fieldvalue);
                    }
                    else if (fieldtype == Double.TYPE || fieldtype == Double.class) {
                        jsonGenerator.write(fieldname,(double)fieldvalue);
                    }
                    else if (fieldtype == Boolean.TYPE || fieldtype == Boolean.class) {
                        jsonGenerator.write(fieldname,(boolean)fieldvalue);
                    }
                    else if (fieldtype == String.class) {
                        jsonGenerator.write(fieldname,(String)fieldvalue);
                    }
                    else if (fieldvalue instanceof Collection) {
                        jsonGenerator.writeStartArray();
                        ((Collection)fieldvalue).forEach(el -> {
                            jsonGenerator.writeStartObject();
                            // serialise the embedded value
                            serializationContext.serialize(fieldvalue,jsonGenerator);
                            jsonGenerator.writeEnd();
                        });
                        jsonGenerator.writeEnd();
                    }
                    // if we have an embedded object, we somehow need to manage to write it as well...
                    else {
                        jsonGenerator.writeStartObject(fieldname);
                        // serialise the embedded value
                        serializationContext.serialize(fieldvalue,jsonGenerator);
                        jsonGenerator.writeEnd();
                    }
                }
            }
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
                    .filter(m -> Modifier.isPublic(m.getModifiers()) &&  m.getParameterCount() == 0 && !m.isAnnotationPresent(JsonbTransient.class) && m.getName().startsWith("get") || m.getName().startsWith("is"))
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

    private String lookupClassnameProperty(Class klass) {
        if (klass.isAnnotationPresent(JsonTypeInfo.class)) {
            logger.info("lookup classname property from annotated klass " + klass);
            return ((JsonTypeInfo)klass.getAnnotation(JsonTypeInfo.class)).property();
        }
        else if (klass.getSuperclass() != null && klass.getSuperclass() != Object.class) {
            return lookupClassnameProperty(klass.getSuperclass());
        }
        else {
            throw new PolymorphicTypeException("Could not determine classname property from class " + klass);
        }
    }

}
