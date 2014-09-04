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
package ro.fortsoft.wicket.dashboard.web;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.DashboardUtils;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.web.layout.DashboardLayout;
import ro.fortsoft.wicket.dashboard.web.layout.column.ColumnDashboardLayout;
import ro.fortsoft.wicket.dashboard.web.layout.grid.GridDashboardLayout;

/**
 * @author Decebal Suiu
 */
public class DashboardPanel extends GenericPanel<Dashboard> implements DashboardContextAware {

	private static final long serialVersionUID = 1L;

	private transient DashboardContext dashboardContext;

	public DashboardPanel(String id, IModel<Dashboard> model) {
		super(id, model);
	}

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(createDashboardLayout("layout", getModel()));
        add(new DashboardResourcesBehavior());
    }

    public Dashboard getDashboard() {
		return getModelObject();
	}

	@Override
	public void setDashboardContext(DashboardContext dashboardContext) {
		this.dashboardContext = dashboardContext;
	}

	/**
	 * Used by children.
	 */
	public DashboardContext getDashboardContext() {
		return dashboardContext;
	}

	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);

		if (event.getPayload() instanceof DashboardEvent) {
            onDashboardEvent((DashboardEvent) event.getPayload());
		}
	}

    protected DashboardLayout createDashboardLayout(String id, IModel<Dashboard> model) {
        return new ColumnDashboardLayout(id, model);
//        return new GridDashboardLayout(id, model);
    }

    protected void onWidgetsSorted(DashboardEvent event) {
		Dashboard dashboard = getDashboard();
		DashboardUtils.updateWidgetLocations(dashboard, event);
		dashboardContext.getDashboardPersiter().save(dashboard);
	}

    protected void onWidgetAdded(DashboardEvent event) {
        Widget addedWidget = (Widget) event.getDetail();
        Dashboard dashboard = getDashboard();
        DashboardUtils.updateWidgetLocations(dashboard, event);
        dashboard.addWidget(addedWidget);
        dashboardContext.getDashboardPersiter().save(dashboard);
    }

    protected void onWidgetRemoved(DashboardEvent event) {
        Widget removedWidget = (Widget) event.getDetail();
        Dashboard dashboard = getDashboard();
        DashboardUtils.updateWidgetLocations(dashboard, event);
        dashboard.deleteWidget(removedWidget.getId());
        dashboardContext.getDashboardPersiter().save(dashboard);
    }

    private void onDashboardEvent(DashboardEvent event) {
        DashboardEvent.EventType eventType = event.getType();
        if (DashboardEvent.EventType.WIDGET_ADDED == eventType) {
            onWidgetAdded(event);
        } else if (DashboardEvent.EventType.WIDGET_REMOVED == eventType) {
            onWidgetRemoved(event);
        } else if (DashboardEvent.EventType.WIDGETS_SORTED == eventType) {
            onWidgetsSorted(event);
        }
    }

}
