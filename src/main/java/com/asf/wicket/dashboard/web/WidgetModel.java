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

import org.apache.wicket.model.LoadableDetachableModel;

import com.asf.wicket.dashboard.Widget;
import com.asf.wicket.dashboard.demo.DashboardApplication;

/**
 * @author Decebal Suiu
 */
public class WidgetModel extends LoadableDetachableModel<Widget> {

	private static final long serialVersionUID = 1L;

	private String id;
		
	public WidgetModel(String id) {
		this.id = id;
	}
	
	public String getWidgetId() {
		return id;
	}

	public void setWidgetId(String id) {
		this.id = id;
	}

	@Override
	protected Widget load() {
		return DashboardApplication.get().getDashboard().getWidget(id);
	}
	
}
