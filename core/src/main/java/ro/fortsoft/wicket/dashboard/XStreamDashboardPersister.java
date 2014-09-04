/*
 * Copyright 2012 Decebal Suiu
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
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * @author Decebal Suiu
 */
public class XStreamDashboardPersister implements DashboardPersister {

    private static final Logger log = LoggerFactory.getLogger(DashboardPersister.class);

	private File file;
    private XStreamHandler handler;

	public XStreamDashboardPersister(File file) {
		this.file = file;
	}

	@Override
	public Dashboard load() {
		try {
            log.debug("Restore dashboard from xml file '{}'", file);
            XStreamHandler handler = getHandler();
			Dashboard dashboard = (Dashboard) handler.fromXML(file);
            if (handler.isMigrated()) {
                log.debug("Save back the migrated xml to avoid migration when it is loaded next time");
                save(dashboard);
            }

            return dashboard;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void save(Dashboard dashboard) {
		// sort widgets
		Collections.sort(dashboard.getWidgets(), new WidgetComparator());

		try {
            log.debug("Save dashboard to xml file '{}'", file);
			getHandler().toXML(dashboard, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @XStreamHandler.Migration(version = 1)
    public static void migrate1(XStreamHandler.MigrationContext context) {
        Element rootElement = context.getRootElement();

        // migrate widget's settings
        Iterator<Element> iterator = rootElement.getDescendants(Filters.element("entry"));
        List<Element> items = new ArrayList<Element>();
        while (iterator.hasNext()) {
            items.add(iterator.next());
        }
        for (int i = 0; i < items.size(); i++) {
            Element item = items.get(i);
            List<Element> tmp = item.getChildren();
            String key = tmp.get(0).getText();
            String value = tmp.get(1).getText();

            item.getParentElement().addContent((new Element(key).setText(value)));
            item.getParentElement().removeChild("entry");
        }

        // add 'columnSpan' and 'rowSpan' for widget's location
        iterator = rootElement.getDescendants(Filters.element("location"));
        List<Element> locations = new ArrayList<Element>();
        while (iterator.hasNext()) {
            locations.add(iterator.next());
        }

        for (Element location : locations) {
            location.addContent(new Element("columnSpan").setText("1"));
            location.addContent(new Element("rowSpan").setText("1"));
        }

        // create 'settings' and move the old 'columnCount'
        String columnCount = rootElement.getChild("columnCount").getText();
        Element settings = new Element("settings");
        settings.addContent(new Element("columnCount").setText(columnCount));
        rootElement.addContent(settings);
        rootElement.removeChild("columnCount");

        // debug
//        context.printXml();
    }

    private XStreamHandler getHandler() {
        if (handler == null) {
            XStream xstream = createXStream();

            handler = new XStreamHandler(xstream);
            handler.setMigrationsClass(XStreamDashboardPersister.class);
        }

        return handler;
    }

    private XStream createXStream() {
        XStream xstream = new XStream(new DomDriver("UTF-8"));
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("dashboard", DefaultDashboard.class);
        xstream.registerConverter(new SettingsConverter());
        xstream.alias("settings", Settings.class);
//        xstream.alias("textWidget", TextWidget.class);
//        xstream.alias("chartWidget", ChartWidget.class);

        return xstream;
    }

    public static class SettingsConverter implements Converter {

        public boolean canConvert(Class clazz) {
            return Settings.class.isAssignableFrom(clazz);
        }

        public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
            Settings settings = (Settings) value;
            for (Object object : settings.entrySet()) {
                Map.Entry entry = (Map.Entry) object;
                writer.startNode(entry.getKey().toString());
                writer.setValue(entry.getValue().toString());
                writer.endNode();
            }
        }

        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
            Settings settings = new Settings();
            while (reader.hasMoreChildren()) {
                reader.moveDown();

                String key = reader.getNodeName(); // nodeName aka element's name
                String value = reader.getValue();
                settings.put(key, value);

                reader.moveUp();
            }

            return settings;
        }

    }

}
