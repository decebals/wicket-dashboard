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

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.asf.wicket.dashboard.Widget;

/**
 * @author Decebal Suiu
 */
public class WidgetView extends Panel {

	private static final long serialVersionUID = 1L;
	
	protected State state;
	
	public WidgetView(String id, IModel<Widget> model) {
		super(id, model);
		
		setOutputMarkupId(true);
		
		if (getWidget().isCollapsed()) {
			add(AttributeModifier.append("style", "display: none"));
		}
		
		state = new State(model.getObject().getId());
	}
	
	public Widget getWidget() {
		return (Widget) getDefaultModelObject();
	}	

	public State getState() {
		return state;
	}

	class State implements Serializable {

		private static final long serialVersionUID = 1L;
		
		private String widgetId;
		private int columnIndex;
		private int rowIndex;
		
		public State(String widgetId) {
			this.widgetId = widgetId;
		}

		public String getWidgetId() {
			return widgetId;
		}
		
		public void setWidgetId(String widgetId) {
			this.widgetId = widgetId;
		}
		
		public int getColumnIndex() {
			return columnIndex;
		}
		
		public void setColumnIndex(int columnIndex) {
			this.columnIndex = columnIndex;
		}
		
		public int getRowIndex() {
			return rowIndex;
		}
		
		public void setRowIndex(int index) {
			this.rowIndex = index;
		}

		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("State[");
			buffer.append("columnIndex = ").append(columnIndex);
			buffer.append(" rowIndex = ").append(rowIndex);
			buffer.append(" widgetId = ").append(widgetId);
			buffer.append("]");
			
			return buffer.toString();
		}

	}
	
}
