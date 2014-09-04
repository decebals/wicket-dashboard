/*
 * Copyright 2014 Decebal Suiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
 * the License. You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package ro.fortsoft.wicket.dashboard;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.HierarchicalStreams;
import com.thoughtworks.xstream.io.xml.JDom2Reader;
import com.thoughtworks.xstream.io.xml.JDom2Writer;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Decebal Suiu
 */
class XStreamHandler {

    private static final Logger log = LoggerFactory.getLogger(XStreamHandler.class);

    private final XStream xstream;
    private final XMLOutputter writer;
    private final SAXBuilder builder;

    private Class<?> objectType;
    private Class<?> migrationsClass;

    private boolean migrated;

    public XStreamHandler(XStream xstream) {
        this(xstream, new SAXBuilder(), new XMLOutputter());
    }

    public XStreamHandler(XStream xstream, SAXBuilder builder, XMLOutputter writer) {
        this.xstream = xstream;
        this.writer = writer;
        this.builder = builder;
    }

    public Object fromXML(File in) throws JDOMException, IOException {
        return fromXML(builder.build(in));
    }

    public Object fromXML(Document doc) {
        return fromXML(doc.getRootElement());
    }

    public Object fromXML(Element rootElement) {
        // read the document version
        String xmlVersion = rootElement.getAttributeValue("version", "0");
        log.debug("The xml version is '{}'", xmlVersion);

        JDom2Reader reader = new JDom2Reader(rootElement);
        objectType = HierarchicalStreams.readClassType(reader, xstream.getMapper());
        log.debug("The object type is '{}'", objectType);

        MigrationContext context = new MigrationContext(rootElement);

        // change the xml (prepare for correct deserialization)
        migrate(context, Integer.parseInt(xmlVersion));

        return xstream.unmarshal(reader);
    }

    public void toXML(Object object, OutputStream out) throws IOException {
        Element rootElement = new Element("container");
        xstream.marshal(object, new JDom2Writer(rootElement));

        Document doc = new Document(rootElement.getChildren().get(0).detach());
        int lastVersion = getLastVersion();
        doc.getRootElement().setAttribute("version", String.valueOf(lastVersion));

        writer.setFormat(Format.getPrettyFormat()); // default is COMPACT (one line)
        writer.output(doc, out);
    }

    public Class<?> getMigrationsClass() {
        if (migrationsClass == null) {
            migrationsClass = objectType;
        }

        return migrationsClass;
    }

    public void setMigrationsClass(Class<?> migrationsClass) {
        this.migrationsClass = migrationsClass;
    }

    public boolean isMigrated() {
        return migrated;
    }

    private int getLastVersion() {
        int maxVersion = 0;
        for (Method method : getMigrationsClass().getDeclaredMethods()) {
            Migration migration = method.getAnnotation(Migration.class);
            if (migration == null) {
                continue;
            }

            int version = migration.version();
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                Class<?>[] params = method.getParameterTypes();
                if ((params.length == 1) && params[0].isAssignableFrom(MigrationContext.class)) {
                    if (version > maxVersion) {
                        maxVersion = version;
                    }
                } else {
                    log.warn("Ignoring @Migration on method with wrong parameter count or type '{}'", method.getName());
                }
            } else {
                log.warn("Ignoring @Migration on non-static method '{}'", method.getName());
            }
        }

        return maxVersion;
    }

    private void migrate(MigrationContext context, int version) {
        log.debug("Search migrations in '{}'", getMigrationsClass());
        Map<Integer, Method> methods = new TreeMap<Integer, Method>(); // key=version, value=method
        for (Method method : getMigrationsClass().getDeclaredMethods()) {
            // check for static methods
            if ((method.getModifiers() & Modifier.STATIC) != 0) {
                // check for @Migration(version = x)
                Migration migration = method.getAnnotation(Migration.class);
                if (migration != null) {
                    int migrationVersion = migration.version();
                    if (migrationVersion > version) {
                        Class<?>[] params = method.getParameterTypes();
                        // check for one parameter with type Element
                        if ((params.length == 1) && params[0].isAssignableFrom(MigrationContext.class)) {
                            Method oldMethod = methods.put(migrationVersion, method);
                            if (oldMethod != null) {
                                throw new RuntimeException("In class '" + getMigrationsClass().getName() +
                                        "': Duplicate migration versions defined for '"
                                        + method.getName() + "' and '" + oldMethod.getName() + "'");
                            }
                        }
                    }
                }
            }
        }

        migrated = false;

        if (methods.isEmpty()) {
            log.debug("No migrations needed");
            return;
        }

        // invoke all migration methods with version greater than document version
        Set<Integer> versions = methods.keySet();
        for (Integer migrationVersion : versions) {
            Method method = methods.get(migrationVersion);
            log.debug("Migrate to version '{}' invoking '{}' method", migrationVersion, method.getName());
            try {
                method.invoke(null, context);
                log.debug("Migrated to version {}", migrationVersion);
                migrated = true;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                log.error("Failed to invoke migration '{}' method", method.getName(), e);
            }
        }
    }

    public static class MigrationContext {

        private Element rootElement;

        public MigrationContext(Element rootElement) {
            this.rootElement = rootElement;
        }

        public Element getRootElement() {
            return rootElement;
        }

        public void printXml() {
            printXml(System.out);
        }

        public void printXml(OutputStream outputStream) {
            XMLOutputter writer = new XMLOutputter();
            writer.setFormat(Format.getPrettyFormat()); // default is COMPACT (one line)
            try {
                writer.output(rootElement.getDocument(), outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Migration {

        int version();

    }

}