Simple wicket dashboard
=====================

A dashboard is a panel with quick access to information or common tasks.

Components
-------------------
- **Dashboard** is the object that contains the widgets. It has one or more columns.
- **Widget** is a fancy word for tools or content that you can add, arrange, and remove from the dashboard.
Widgets make it easy to customize the content of your dashboard.
Widget has an id, a title, some settings (optional), a location (the column and row in dashboard),
and more important it has a view.
- **WidgetView** is the component that displays the widget content (it extends the wicket Panel class). 
A view can be for example a chart or a table.
- **WidgetDescriptor** contains widget meta data: name, description, provider and the widget class name.
- **WidgetFactory** is the object that creates widgets using the widget descriptors.
- **WidgetRegistry** is the object that stores all active widget descriptors. You can register a new widget 
using a widget descriptor.
- **DashboardPersiter** is responsible for dashboard load and save. XStreamDashboardPersister is a concrete implementation
that save/load a dashboard to/from a file.
- **DashboardPanel** is a wicket panel that displays a dashboard.
- **WidgetPanel** is a wicket panel that displays a widget. It contains a header panel, a settings panel (if the
widget has settings) and the widget view. It can be moved with drag and drop.
The header panel contains the widget title, an icon that display the collapsed state and some actions (refresh, delete, settings).

How to use
-------------------
It's very simple to add a dashboard panel in your wicket application.

In your application class make some initializations:

    public void init() {
        ...

    	// dashboard settings
		DashboardContextInjector dashboardContextInjector = new DashboardContextInjector();
		DashboardContext dashboardContext = dashboardContextInjector.getDashboardContext();
		WidgetRegistry widgetRegistry = dashboardContext.getWidgetRegistry();
		widgetRegistry.registerWidget(new LoremIpsumWidgetDescriptor());
		widgetRegistry.registerWidget(new ChartWidgetDescriptor());
        getComponentInstantiationListeners().add(dashboardContextInjector);
                
        initDashboard(dashboardContext);
        
        ChartWidget.setChartDataFactory(new DemoChartDataFactory());
    }

	private void initDashboard(DashboardContext dashboardContext) {
		dashboard = dashboardContext.getDashboardPersiter().load();
    	if (dashboard == null) {
    		dashboard = new DefaultDashboard("default", "Default");
    	}
	}


In your web page add the dashboard panel:

    Dashboard dashboard = ...;
    add(new DashboardPanel("dashboard", new Model<Dashboard>(dashboard)));
    
For more information please see the demo sources.

Demo
-------------------

I have a tiny demo application. In this demo I have implemented two widgets types:
a chart widget (using open flash chart) and a text widget (display a Lorem Ipsum).
You can drag and drop widgets, perform some actions on each widget, add or remove new
widgets, change widget settings, collapse widgets.

The demo application is in demo package.
To run the demo application use:  
 
    mvn install
    cd demo
    mvn jetty:run

In the internet browser type http://localhost:8081/.

You can see a screenshot from demo application in [wiki page] (https://github.com/decebals/wicket-dashboard/wiki).

License
--------------
  
Copyright 2012 Decebal Suiu
 
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with
the License. You may obtain a copy of the License in the LICENSE file, or at:
 
http://www.apache.org/licenses/LICENSE-2.0
 
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
