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
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.template.PackageTextTemplate;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.WidgetLocation;
import ro.fortsoft.wicket.dashboard.web.WidgetPanel;
import ro.fortsoft.wicket.dashboard.web.layout.DashboardLayout;
import ro.fortsoft.wicket.dashboard.web.layout.LayoutAjaxBehavior;
import ro.fortsoft.wicket.dashboard.web.layout.WidgetLoadingPanel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class GridDashboardLayout extends DashboardLayout {

	private static final long serialVersionUID = 1L;

    private int widgetBaseWidth = 600;
    private int widgetBaseHeight = 330;

    private LayoutAjaxBehavior layoutAjaxBehavior;
    private WidgetResizeAjaxBehavior widgetResizeBehavior;

    public GridDashboardLayout(String id, IModel<Dashboard> model) {
        super(id, model);
    }

    public int getWidgetBaseWidth() {
        return widgetBaseWidth;
    }

    public void setWidgetBaseWidth(int widgetBaseWidth) {
        this.widgetBaseWidth = widgetBaseWidth;
    }

    public int getWidgetBaseHeight() {
        return widgetBaseHeight;
    }

    public void setWidgetBaseHeight(int widgetBaseHeight) {
        this.widgetBaseHeight = widgetBaseHeight;
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

        response.render(OnDomReadyHeaderItem.forScript(getJavaScript()));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addWidgets();

        layoutAjaxBehavior = createLayoutAjaxBehavior("Dashboard.GridLayout.serialize");
        add(layoutAjaxBehavior);

        widgetResizeBehavior = new WidgetResizeAjaxBehavior() {

            @Override
            public void onWidgetResized(final AjaxRequestTarget target, final String widgetId) {
                visitChildren(WidgetPanel.class, new IVisitor<WidgetPanel, Void>() {

                    @Override
                    public void component(WidgetPanel object, IVisit<Void> visit) {
                        if (widgetId.equals(object.getModelObject().getId())) {
                            visit.stop();
                            // refresh the resized widget panel
                            target.add(object);
                        }
                    }

                });

            }

        };
        add(widgetResizeBehavior);
    }

    private String getJavaScript() {
        CharSequence script = layoutAjaxBehavior.getCallbackFunctionBody();
        CharSequence widgetResizeScript = widgetResizeBehavior.getCallbackFunctionBody();

        Map<String, String> vars = new HashMap<String, String>();
        vars.put("widgetBaseWidth", String.valueOf(widgetBaseWidth));
        vars.put("widgetBaseHeight", String.valueOf(widgetBaseHeight));
        vars.put("stopBehavior", script.toString());
        vars.put("widgetResizeBehavior", widgetResizeScript.toString());

        PackageTextTemplate template = new PackageTextTemplate(GridDashboardLayout.class, "res/behavior.template.js");
        template.interpolate(vars);

        return template.getString();
    }

    private void addWidgets() {
        final List<Widget> widgets = getDashboard().getWidgets();
        ListView<Widget> listView = new ListView<Widget>("widgetList", widgets) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<Widget> item) {
                Widget widget = item.getModelObject();
                String widgetId = widget.getId();
//                item.add(createWidgetPanel("widget", widgetId));
                item.add(new WidgetLoadingPanel("widget", item.getModel()));

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

    private abstract class WidgetResizeAjaxBehavior extends AbstractDefaultAjaxBehavior {

        private static final long serialVersionUID = 1L;

        private static final String WIDGET_ID = "widgetId";

        public abstract void onWidgetResized(AjaxRequestTarget target, String widgetId);

        @Override
        protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
            super.updateAjaxAttributes(attributes);

            StringBuilder buffer = new StringBuilder();
            buffer.append("var widgetId = widget.attr('id').substring(7);"); // 'widget-'.length;
            buffer.append("return { '" + WIDGET_ID + "': widgetId };");

            attributes.getDynamicExtraParameters().add(buffer);
        }

        @Override
        protected void respond(AjaxRequestTarget target) {
            String widgetId = getComponent().getRequest().getRequestParameters().getParameterValue(WIDGET_ID).toString();
            onWidgetResized(target, widgetId);
        }

    }

}
