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
package ro.fortsoft.wicket.dashboard.web.layout;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Settings;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.WidgetLocation;
import ro.fortsoft.wicket.dashboard.web.*;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Decebal Suiu
 */
public abstract class DashboardLayout extends GenericPanel<Dashboard> {

	private static final long serialVersionUID = 1L;

	public DashboardLayout(String id, IModel<Dashboard> model) {
		super(id, model);
	}

	public Dashboard getDashboard() {
		return getModelObject();
	}

    /**
	 * Used by children.
	 */
	public DashboardContext getDashboardContext() {
		return findParent(DashboardPanel.class).getDashboardContext();
	}

    protected WidgetPanel createWidgetPanel(String id, IModel<Widget> model) {
        return new WidgetPanel(id, model);
    }

    protected WidgetPanel createWidgetPanel(String id, String widgetId) {
        IModel<Widget> widgetModel = new WidgetModel(getModel(), widgetId);
        WidgetPanel widgetPanel = createWidgetPanel("widget", widgetModel);

        return widgetPanel;
    }

    protected LayoutAjaxBehavior createLayoutAjaxBehavior(final String serializeFunctionName) {
        return new LayoutAjaxBehavior() {

            private static final long serialVersionUID = 1L;

            @Override
            public String getSerializeFunctionName() {
                return serializeFunctionName;
            }

            @Override
            public void onLayoutChanged(AjaxRequestTarget target, Map<String, WidgetLocation> widgetLocations) {
                send(getPage(), Broadcast.BREADTH, new DashboardEvent(target, DashboardEvent.EventType.WIDGETS_SORTED, widgetLocations));
            }

        };
    }

    protected Properties getDefaultProperties() {
        Properties defaults = new Properties();
        try {
            defaults.load(getClass().getResourceAsStream("layout.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defaults;
    }

    protected Settings getSettings() {
        Properties defaults = getDefaultProperties();
        for (String key : defaults.stringPropertyNames()) {
            if (!getDashboard().getSettings().containsKey(key)) {
                getDashboard().getSettings().put(key, defaults.getProperty(key));
            }
        }

        return getDashboard().getSettings();
    }

}
