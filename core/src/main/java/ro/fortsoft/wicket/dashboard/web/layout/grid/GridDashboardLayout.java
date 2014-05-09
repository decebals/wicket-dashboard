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
package ro.fortsoft.wicket.dashboard.web.layout.grid;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.template.PackageTextTemplate;
import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.WidgetLocation;
import ro.fortsoft.wicket.dashboard.web.layout.DashboardLayout;
import ro.fortsoft.wicket.dashboard.web.layout.LayoutAjaxBehavior;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class GridDashboardLayout extends DashboardLayout {

	private static final long serialVersionUID = 1L;

    private LayoutAjaxBehavior layoutAjaxBehavior;

    public GridDashboardLayout(String id, IModel<Dashboard> model) {
        super(id, model);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // add grister.js
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(GridDashboardLayout.class, "res/jquery.gridster.js")));
        response.render(CssHeaderItem.forReference(new PackageResourceReference(GridDashboardLayout.class, "res/jquery.gridster.css")));

        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(GridDashboardLayout.class, "res/layout.js")));

        // add css contributions
        response.render(CssHeaderItem.forReference(new PackageResourceReference(GridDashboardLayout.class, "res/layout.css")));

        CharSequence script = layoutAjaxBehavior.getCallbackFunctionBody();

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("stopBehavior", script.toString());

        PackageTextTemplate template = new PackageTextTemplate(GridDashboardLayout.class, "res/behavior.template.js");
        template.interpolate(vars);

        response.render(OnDomReadyHeaderItem.forScript(template.getString()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addWidgets();

        layoutAjaxBehavior = createLayoutAjaxBehavior("Dashboard.GridLayout.serialize");
        add(layoutAjaxBehavior);
    }

    private void addWidgets() {
        final List<Widget> widgets = getDashboard().getWidgets();
        ListView<Widget> listView = new ListView<Widget>("widgetList", widgets) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Widget> item) {
                Widget widget = item.getModelObject();
                String widgetId = widget.getId();
                item.add(createWidgetPanel("widget", widgetId));

                item.setOutputMarkupId(true);
                item.setMarkupId("widget-" + widgetId);

                // set row, col attribute
                WidgetLocation location = widget.getLocation();
                item.add(AttributeModifier.append("data-col", location.getColumn() + 1)); // first col index is 1 for gridster
                item.add(AttributeModifier.append("data-row", location.getRow() + 1)); // first row index is 1 for gridster
                item.add(AttributeModifier.append("data-sizex", location.getColumnSpan()));
                item.add(AttributeModifier.append("data-sizey", location.getRowSpan()));
            }

        };

        add(listView);
    }

}
