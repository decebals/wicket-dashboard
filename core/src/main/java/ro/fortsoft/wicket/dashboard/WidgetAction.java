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
package ro.fortsoft.wicket.dashboard;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.*;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import ro.fortsoft.wicket.dashboard.web.DashboardEvent;
import ro.fortsoft.wicket.dashboard.web.WidgetPanel;
import ro.fortsoft.wicket.dashboard.web.WidgetView;
import ro.fortsoft.wicket.dashboard.web.util.AjaxConfirmLink;

/**
 * @author Decebal Suiu
 */
public abstract class WidgetAction implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Widget widget;	
	protected ResourceReference image;
	protected IModel<String> tooltip;

	public WidgetAction(Widget widget, ResourceReference image) {
		this.widget = widget;
		this.image = image;
	}
	
	public abstract AbstractLink getLink(String id);
 
	public ResourceReference getImage() {
		return image;
	}

	public IModel<String> getTooltip() {
		return tooltip;
	}

	public static class Refresh extends WidgetAction {

		private static final long serialVersionUID = 1L;

		public Refresh(Widget widget) {
			super(widget, new PackageResourceReference(WidgetAction.class, "refresh.gif"));
			
			tooltip = new ResourceModel("refresh");
		}

		@Override
		public AbstractLink getLink(String id) {
			return new AjaxLink<Void>(id) {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {				
					WidgetView widgetView = findParent(WidgetPanel.class).getWidgetView();
					target.add(widgetView);
				}
				
			};			
		}
		
	}
	
	public static class Delete extends WidgetAction {

		private static final long serialVersionUID = 1L;

		public Delete(Widget widget) {
			super(widget, new PackageResourceReference(WidgetAction.class, "delete.gif"));
			
			tooltip = new ResourceModel("delete");
		}

		@Override
		public AbstractLink getLink(String id) {
			AjaxConfirmLink<Void> deleteLink = new AjaxConfirmLink<Void>(id) {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					send(getPage(), Broadcast.BREADTH, new DashboardEvent(target, DashboardEvent.EventType.WIDGET_REMOVED, widget));
					// the widget is removed from ui with javascript (with a IAjaxCallListener) -> see getAjaxCallListener()
				}

	            @Override
	            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
	                super.updateAjaxAttributes(attributes);

	                attributes.getAjaxCallListeners().add(new AjaxCallListener() {
	                	
	                    private static final long serialVersionUID = 1L;

						@Override
	                    public CharSequence getSuccessHandler(Component component) {
	                        return "$('#widget-" + widget.getId() + "').remove();";
	                    }
	                    
	                });
	            }
				
			};
			IModel<String> resourceModel = new StringResourceModel("deleteAsk", deleteLink, Model.of(widget.getTitle()));
			deleteLink.setConfirmMessage(resourceModel.getObject());
			
			return deleteLink;
		}
		
	}

	public static class Settings extends WidgetAction {

		private static final long serialVersionUID = 1L;

		public Settings(Widget widget) {
			super(widget, new PackageResourceReference(WidgetAction.class, "edit.png"));
			
			tooltip = new ResourceModel("settings");
		}

		@Override
		public AbstractLink getLink(String id) {
			return new AjaxLink<Void>(id) {

				private static final long serialVersionUID = 1L;

				@Override
				public void onClick(AjaxRequestTarget target) {
					if (widget.hasSettings()) {
						WidgetPanel widgetPanel = findParent(WidgetPanel.class);
						Panel settingsPanel = widgetPanel.getSettingsPanel();
						settingsPanel.setVisible(true);
						target.add(settingsPanel);
					}
				}
				
			};
		}
		
	}

}
