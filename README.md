Note that for running the project on Wildfly 20 and higher, EJB run configurations need to specify the following parameters for the Jackson JSON Processor:
+ -Dresteasy.preferJacksonOverJsonB=true 
+ -Dresteasy.jackson.deserialization.whitelist.allowIfBaseType.prefix=* 
+ -Dresteasy.jackson.deserialization.whitelist.allowIfSubType.prefix=*

Otherwise, usage of polymorphic types annotated with @JsonTypeInfo and foreseeing the full classname of concrete subtypes, when accessing the application's web api, will result in a 400 error