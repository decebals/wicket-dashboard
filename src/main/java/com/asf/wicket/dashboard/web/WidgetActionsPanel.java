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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import com.asf.wicket.dashboard.Dashboard;
import com.asf.wicket.dashboard.Widget;
import com.asf.wicket.dashboard.demo.DashboardApplication;
import com.asf.wicket.dashboard.web.common.behavior.ConfirmAjaxDecoratorDelegate;
import com.asf.wicket.dashboard.web.common.menu.MenuItem;
import com.asf.wicket.dashboard.web.common.menu.MenuPanel;

/**
 * @author Decebal Suiu
 */
public class WidgetActionsPanel extends GenericPanel<Widget> {

	private static final long serialVersionUID = 1L;
	
	public WidgetActionsPanel(String id, IModel<Widget> model) {
		super(id, model);
		
		add(new MenuPanel("menuPanel", new ActionsModel()).setRenderBodyOnly(true));
	}
	
	private Widget getWidget() {
		return getModelObject();
	}
	
	private List<MenuItem> createActions() {
		List<MenuItem> actions = new ArrayList<MenuItem>();
//		actions.add(new Action("refresh", "images/refresh.gif"));
		if (getModelObject().hasSettings()) {
			actions.add(new MenuItem(createSettingsLink(), null, "images/edit.png", "Setari"));
		}
		actions.add(new MenuItem(createDeleteLink(), null, "images/delete.gif", "Sterge"));
		
		return actions;
	}
	
	private AbstractLink createSettingsLink() {
		return new AjaxLink<Void>(MenuPanel.LINK_ID) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				WidgetPanel widgetPanel = findParent(WidgetPanel.class);
				Panel settingsPanel = widgetPanel.getSettingsPanel();
				settingsPanel.setVisible(true);
				target.add(settingsPanel);				
			}
			
		};
	}

	private AbstractLink createDeleteLink() {
		return new AjaxLink<Void>(MenuPanel.LINK_ID) {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				// TODO
				Dashboard dashboard = DashboardApplication.get().getDashboard();
				dashboard.deleteWidget(getWidget().getId());
				DashboardApplication.get().getDashboardPersiter().save(dashboard);
				// the widget is removed from ui with javascript (with a IAjaxCallDecorator) -> see getAjaxCallDecorator()
			}
			
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				AjaxCallDecorator ajaxDecorator = new AjaxCallDecorator() {
					
					private static final long serialVersionUID = 1L;
					
					@Override
					public CharSequence decorateOnSuccessScript(Component c, CharSequence script) {
						return "$('#widget-" + getWidget().getId() + "').remove();";
					}
					
				};
				
				return new ConfirmAjaxDecoratorDelegate(ajaxDecorator, "Delete widget " + getWidget().getTitle() + "?");
			}
			
		};
	}
	
	private class ActionsModel extends LoadableDetachableModel<List<MenuItem>> {

		private static final long serialVersionUID = 1L;

		@Override
		protected List<MenuItem> load() {
			return createActions();
		}
		
	}

}