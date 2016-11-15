package at.ac.htlhl.nucleij;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import java.util.ResourceBundle;

/**
 * Created by Stefan on 11.11.2016.
 */
public class AppContext
{
    // Constants
    // ************************************************************************
    public final static String APPLICATION_NAME = "NucleiJ";

    // Fields
    // ************************************************************************
    private ObjectMapper jsonMapper;
    private ResourceBundle resourceBundle;

    private static AppContext instance = null;

    private AppContext()
    {
        jsonMapper = new ObjectMapper();
        jsonMapper.setAnnotationIntrospector(new IgnoreInheritedIntrospector());

        resourceBundle = ResourceBundle.getBundle("at.ac.htlhl.nucleij.resources.NucleiJ");
    }

    public static synchronized AppContext getInstance()
    {
        if(instance == null)
        {
            instance = new AppContext();
        }
        return instance;
    }

    public ObjectMapper getJsonMapper()
    {
        return jsonMapper;
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    // region Nested classes
    // ************************************************************************

    /**
     * Jackson JSON Library:
     * Ignore Model Bean properties like "vetoableChangeListeners" or "propertyChangeListeners
     */
    private static class IgnoreInheritedIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public boolean hasIgnoreMarker(final AnnotatedMember m) {
            return m.getDeclaringClass() == com.jgoodies.common.bean.Bean.class || super.hasIgnoreMarker(m);
        }
    }
    // endregion
}
