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
package com.asf.wicket.dashboard.web;

import java.io.File;

import com.asf.wicket.dashboard.DashboardPersiter;
import com.asf.wicket.dashboard.DefaultWidgetActionsFactory;
import com.asf.wicket.dashboard.DefaultWidgetFactory;
import com.asf.wicket.dashboard.DefaultWidgetRegistry;
import com.asf.wicket.dashboard.WidgetActionsFactory;
import com.asf.wicket.dashboard.WidgetFactory;
import com.asf.wicket.dashboard.WidgetRegistry;
import com.asf.wicket.dashboard.XStreamDashboardPersister;

/**
 * @author Decebal Suiu
 */
public class DashboardContext {
	
	private WidgetFactory widgetFactory;
	private WidgetRegistry widgetRegistry;
	private DashboardPersiter dashboardPersiter;
	private WidgetActionsFactory widgetActionsFactory;
	
	public DashboardContext() {
		setWidgetFactory(new DefaultWidgetFactory());
		setWidgetRegistry(new DefaultWidgetRegistry());
		setDashboardPersiter(new XStreamDashboardPersister(new File("dashboard.xml")));
		setWidgetActionsFactory(new DefaultWidgetActionsFactory());
	}
	
	public WidgetFactory getWidgetFactory() {
		return widgetFactory;
	}
	
	public void setWidgetFactory(WidgetFactory widgetFactory) {
		this.widgetFactory = widgetFactory;
	}
	
	public WidgetRegistry getWidgetRegistry() {
		return widgetRegistry;
	}
	
	public void setWidgetRegistry(WidgetRegistry widgetRegistry) {
		this.widgetRegistry = widgetRegistry;
	}

	public DashboardPersiter getDashboardPersiter() {
		return dashboardPersiter;
	}

	public void setDashboardPersiter(DashboardPersiter dashboardPersiter) {
		this.dashboardPersiter = dashboardPersiter;
	}

	public WidgetActionsFactory getWidgetActionsFactory() {
		return widgetActionsFactory;
	}

	public void setWidgetActionsFactory(WidgetActionsFactory widgetActionsFactory) {
		this.widgetActionsFactory = widgetActionsFactory;
	}

}
