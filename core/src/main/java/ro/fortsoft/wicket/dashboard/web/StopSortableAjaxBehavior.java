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

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.odlabs.wiquery.core.javascript.JsScopeContext;
import org.odlabs.wiquery.ui.core.JsScopeUiEvent;
import org.odlabs.wiquery.ui.sortable.SortableBehavior;

import ro.fortsoft.wicket.dashboard.WidgetLocation;

import com.google.gson.Gson;

/**
 * @author Decebal Suiu
 */
public abstract class StopSortableAjaxBehavior extends AbstractDefaultAjaxBehavior {

	private static final long serialVersionUID = 1L;

	/** Sorted identifiant into the request */
	private static final String JSON_DATA = "data";
	
	private SortableBehavior sortableBehavior;
	
	public StopSortableAjaxBehavior() {
		super();
		
		this.sortableBehavior = new SortableBehavior();
	}

	public SortableBehavior getSortableBehavior() {
		return sortableBehavior;
	}

	public abstract void saveLayout(Map<String, WidgetLocation> widgetLocations, AjaxRequestTarget target);
	
	@Override
	protected void onBind() {
		getComponent().add(sortableBehavior);
		sortableBehavior.setStopEvent(new JsScopeUiEvent() {

			private static final long serialVersionUID = 1L;

			@Override
			protected void execute(JsScopeContext scopeContext) {
				scopeContext.append("var data = onStopWidgetMove();");
				scopeContext.append("wicketAjaxGet('" + getCallbackUrl() 
						+ "&" + JSON_DATA + "='+ data" 
						+ ", null, null, function() { return true; })");
			}
			
		});
	}

	@Override
	protected void respond(AjaxRequestTarget target) {
		String jsonData = this.getComponent().getRequest().getRequestParameters().getParameterValue(JSON_DATA).toString();
		Item[] items = getItems(jsonData);
		Map<String, WidgetLocation> locations = new HashMap<String, WidgetLocation>();
		for (Item item : items) {
			WidgetLocation location = new WidgetLocation(item.column, item.sortIndex);
			locations.put(item.widget, location);
		}
		
		saveLayout(locations, target);
	}

	private Item[] getItems(String jsonData) {
		Gson gson = new Gson();
		Item[] items = gson.fromJson(jsonData, Item[].class);
		/*
		System.out.println(items.length);
		for (Item item : items) {
			System.out.println(item);
		}
		*/
		
		return items;
	}
		
	static class Item {
				
		public int column;
		public String widget;
		public int sortIndex;
		
		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Item[");
			buffer.append("column = ").append(column);
			buffer.append(" widget = ").append(widget);
			buffer.append(" sortIndex = ").append(sortIndex);
			buffer.append("]");

			return buffer.toString();
		}

	}
	
}
