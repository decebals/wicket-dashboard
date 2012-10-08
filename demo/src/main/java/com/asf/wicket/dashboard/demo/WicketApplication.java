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

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import com.asf.wicket.dashboard.Dashboard;
import com.asf.wicket.dashboard.DefaultDashboard;
import com.asf.wicket.dashboard.WidgetRegistry;
import com.asf.wicket.dashboard.web.DashboardContext;
import com.asf.wicket.dashboard.web.DashboardContextInjector;
import com.asf.wicket.dashboard.widget.loremipsum.LoremIpsumWidgetDescriptor;
import com.asf.wicket.dashboard.widget.ofchart.ChartWidget;
import com.asf.wicket.dashboard.widget.ofchart.ChartWidgetDescriptor;

/**
 * @author Decebal Suiu
 */
public class WicketApplication extends WebApplication {

	private Dashboard dashboard;
	
	public static WicketApplication get() {
		return (WicketApplication) WebApplication.get();
	}

	@Override
	public void init() {
		super.init();		

		// markup settings
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
		
		// exception settings
		getResourceSettings().setThrowExceptionOnMissingResource(false);
		
		// mounts
		mountPage("add-widget", AddWidgetPage.class);
		mountPage("widget", WidgetPage.class);

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

	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

	public Dashboard getDashboard() {
		return dashboard;
	}

	private void initDashboard(DashboardContext dashboardContext) {
		dashboard = dashboardContext.getDashboardPersiter().load();
    	if (dashboard == null) {
    		dashboard = new DefaultDashboard("default", "Default");
    	}
	}

}
