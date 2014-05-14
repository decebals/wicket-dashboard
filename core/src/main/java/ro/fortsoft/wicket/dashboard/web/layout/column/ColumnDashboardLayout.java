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
package ro.fortsoft.wicket.dashboard.web.layout.column;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.util.template.PackageTextTemplate;
import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.web.layout.DashboardLayout;
import ro.fortsoft.wicket.dashboard.web.layout.LayoutAjaxBehavior;
import ro.fortsoft.wicket.dashboard.web.layout.WidgetLoadingPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Decebal Suiu
 */
public class ColumnDashboardLayout extends DashboardLayout {

	private static final long serialVersionUID = 1L;

	private List<ColumnFragment> columnFragments;

    public ColumnDashboardLayout(String id, IModel<Dashboard> model) {
        super(id, model);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        // add javascript contributions
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(ColumnDashboardLayout.class, "res/jquery-ui-1.9.2.min.js")));
        response.render(JavaScriptHeaderItem.forReference(new PackageResourceReference(ColumnDashboardLayout.class, "res/layout.js")));

        // add css contributions
        response.render(CssHeaderItem.forReference(new PackageResourceReference(ColumnDashboardLayout.class, "res/layout.css")));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        addColumns();
    }

	private void addColumns() {
		final int columnCount = getDashboard().getColumnCount();
		Loop columnsView = new Loop("columns", columnCount) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				if (!hasBeenRendered()) {
					columnFragments = new ArrayList<ColumnFragment>();
				}

				super.onBeforeRender();
			}

			@Override
			protected void populateItem(LoopItem item) {
			    float columnPanelWidth = 100f / columnCount;
                ColumnFragment fragment = new ColumnFragment(item.getIndex());
                fragment.setRenderBodyOnly(true);
                fragment.getColumnContainer().add(AttributeModifier.replace("style", "width: " + columnPanelWidth + "%;"));
                item.add(fragment);

                columnFragments.add(fragment);
			}

		};
		add(columnsView);
	}

    public class ColumnFragment extends Fragment {

        private LayoutAjaxBehavior layoutAjaxBehavior;

        public ColumnFragment(int columnIndex) {
            super("column", "columnFragment", ColumnDashboardLayout.this);

            WebMarkupContainer columnContainer = new WebMarkupContainer("columnContainer");
            columnContainer.setOutputMarkupId(true);
            columnContainer.setMarkupId("column-" + columnIndex);

            final List<Widget> widgets = getModelObject().getWidgets(columnIndex);
            ListView<Widget> listView = new ListView<Widget>("widgetList", widgets) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<Widget> item) {
                    String widgetId = item.getModelObject().getId();
//                    item.add(createWidgetPanel("widget", widgetId));
                    item.add(new WidgetLoadingPanel("widget", item.getModel()));

                    item.setOutputMarkupId(true);
                    item.setMarkupId("widget-" + widgetId);
                }

            };

            columnContainer.add(listView);
            add(columnContainer);

            if (columnIndex == 0) {
                add(new AttributeModifier("style", "margin-left: 0px"));
            }
        }

        public WebMarkupContainer getColumnContainer() {
            return (WebMarkupContainer) get("columnContainer");
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            layoutAjaxBehavior = createLayoutAjaxBehavior("Dashboard.DefaultLayout.serialize");
            getColumnContainer().add(layoutAjaxBehavior);
        }

        @Override
        public void renderHead(IHeaderResponse response) {
            super.renderHead(response);

            CharSequence callbackFunctionBody = layoutAjaxBehavior.getCallbackFunctionBody();

            Map<String, String> vars = new HashMap<String, String>();
            vars.put("component", get("columnContainer").getMarkupId());
            vars.put("stopBehavior", callbackFunctionBody.toString());

            PackageTextTemplate template = new PackageTextTemplate(ColumnDashboardLayout.class, "res/behavior.template.js");
            template.interpolate(vars);

            response.render(OnDomReadyHeaderItem.forScript(template.getString()));
        }

    }

}
