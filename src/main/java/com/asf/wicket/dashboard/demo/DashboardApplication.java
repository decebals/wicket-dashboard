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
package com.asf.wicket.dashboard.demo;

import java.io.File;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import com.asf.wicket.dashboard.Dashboard;
import com.asf.wicket.dashboard.DashboardPersiter;
import com.asf.wicket.dashboard.DefaultDashboard;
import com.asf.wicket.dashboard.DefaultWidgetFactory;
import com.asf.wicket.dashboard.DefaultWidgetRegistry;
import com.asf.wicket.dashboard.WidgetFactory;
import com.asf.wicket.dashboard.WidgetRegistry;
import com.asf.wicket.dashboard.XStreamDashboardPersister;
import com.asf.wicket.dashboard.web.chart.ChartWidgetDescriptor;
import com.asf.wicket.dashboard.web.text.TextWidgetDescriptor;

/**
 * @author Decebal Suiu
 */
public class DashboardApplication extends WebApplication {

	private DashboardPersiter dashboardPersiter;
	private WidgetFactory widgetFactory;
	private WidgetRegistry widgetRegistry;
	private Dashboard dashboard;

	public DashboardApplication() {
		super();
		
		dashboardPersiter = new XStreamDashboardPersister(new File("dashboard.xml"));
		widgetFactory = new DefaultWidgetFactory();
		widgetRegistry = new DefaultWidgetRegistry();
		widgetRegistry.registerWidget(new TextWidgetDescriptor());
		widgetRegistry.registerWidget(new ChartWidgetDescriptor());
	}

	public static DashboardApplication get() {
		return (DashboardApplication) WebApplication.get();
	}

	@Override
	public void init() {
		super.init();
		
        // wiquery
//        addComponentInstantiationListener(new WiQueryInstantiationListener());

		// markup settings
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		
		// exception settings
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		
		// mounts
		mountPage("add-widget", AddWidgetPage.class);
		mountPage("widget", WidgetPage.class);
	}

	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	public Dashboard getDashboard() {
		if (dashboard == null) {
			dashboard = dashboardPersiter.load();
	    	if (dashboard == null) {
	    		dashboard = createDefaultDashboard();
	    	}
		}
		
		return dashboard;
	}
	
	public DashboardPersiter getDashboardPersiter() {
		return dashboardPersiter;
	}

	public WidgetFactory getWidgetFactory() {
		return widgetFactory;
	}

	public WidgetRegistry getWidgetRegistry() {
		return widgetRegistry;
	}
	
	private Dashboard createDefaultDashboard() {
		DefaultDashboard dashboard = new DefaultDashboard("demo", "Demo");
    	/*
	    	WidgetRegistry widgetRegistry = DashboardApplication.get().getWidgetRegistry();
	    	WidgetFactory widgetFactory = DashboardApplication.get().getWidgetFactory();
	    	WidgetDescriptor widgetDescriptor = widgetRegistry.getWidgetDescriptors().get(0); // text
	    	
	    	// left column
	   		List<Widget> leftColumnWidgets = new ArrayList<Widget>();
	    	for (int i = 0; i < 3; i++) {
	    		Widget widget = widgetFactory.createWidget(widgetDescriptor);
	    		widget.setTitle("Text " + (i +1));
	    		leftColumnWidgets.add(widget);
	    	}
	   		widgets.add(leftColumnWidgets);

	   		// right column
	   		List<Widget> rightColumnWidgets = new ArrayList<Widget>();
	   		widgetDescriptor = widgetRegistry.getWidgetDescriptors().get(1); // chart
	   		Widget widget = widgetFactory.createWidget(widgetDescriptor);
			widget.setTitle("Chart 1 (Bar)");
	   		rightColumnWidgets.add(widget);
	   		widget = widgetFactory.createWidget(widgetDescriptor);
			widget.setTitle("Chart 2 (Double Bar)");
	   		rightColumnWidgets.add(widget);
	   		widget = widgetFactory.createWidget(widgetDescriptor);
			widget.setTitle("Chart 3 (Line)");
	   		rightColumnWidgets.add(widget);
//	   		rightColumnWidgets.add(new ChartWidget(getNextId(), "Chart 4 (Demo Doted Line)"));
	   		widget = widgetFactory.createWidget(widgetDescriptor);
			widget.setTitle("Chart 5 (Pie)");
	   		rightColumnWidgets.add(widget);
	   		widget = widgetFactory.createWidget(widgetDescriptor);
			widget.setTitle("Chart 6 (Scatter)");
	   		rightColumnWidgets.add(widget);   		   		
	   		widgets.add(rightColumnWidgets);
	   		*/   		   	
   		
		return dashboard;
	}

}
