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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.odlabs.wiquery.core.events.Event;
import org.odlabs.wiquery.core.events.MouseEvent;
import org.odlabs.wiquery.core.events.WiQueryAjaxEventBehavior;
import org.odlabs.wiquery.core.events.WiQueryEventBehavior;
import org.odlabs.wiquery.core.javascript.JsScope;
import org.odlabs.wiquery.core.javascript.JsStatement;

import ro.fortsoft.wicket.dashboard.Dashboard;
import ro.fortsoft.wicket.dashboard.Widget;


/**
 * @author Decebal Suiu
 */
class WidgetHeaderPanel extends GenericPanel<Widget> implements DashboardContextAware {

	private static final long serialVersionUID = 1L;	
	
	private transient DashboardContext dashboardContext;
	
	public WidgetHeaderPanel(String id, IModel<Widget> model) {
		super(id, model);		
		
		String imagePath = "images/down.png";
		if (getWidget().isCollapsed()) {
			imagePath = "images/up.png";
		}
		Image toogle = new Image("toggle", new ContextRelativeResource(imagePath));
		toogle.add(new WiQueryEventBehavior(new Event(MouseEvent.CLICK) {

			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope(getJsCode());
			}
			
			private CharSequence getJsCode() {
				/*
				var content = $(this).parent().siblings('.dragbox-content'); 
				if (content.css('display') == 'none') {
					content.slideDown(400);
					$(this).attr("src",  "../images/down.png");
				} else {
					content.slideUp(200);
					buffer.append("$(this).attr("src", "../images/up.png");");
				}
				*/

				StringBuilder buffer = new StringBuilder();
				buffer.append("var content = $(this).parent().siblings('.dragbox-content');");
				buffer.append("if (content.css('display') == 'none') {");
				buffer.append("content.slideDown(400);");
				buffer.append("$(this).attr(\"src\",  \"../images/down.png\");");
				buffer.append("} else {");
				buffer.append("content.slideUp(200);");
				buffer.append("$(this).attr(\"src\", \"../images/up.png\");");
				buffer.append("}");
								
				return buffer.toString();
			}
			
		}));
		toogle.add(new WiQueryAjaxEventBehavior(MouseEvent.CLICK) {
		
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				Widget widget = getWidget();
				widget.setCollapsed(!widget.isCollapsed());
				
				Dashboard dashboard = findParent(DashboardPanel.class).getDashboard();
				dashboardContext.getDashboardPersiter().save(dashboard);
			}

			@Override
			public JsStatement statement() {
				return null;
			}
			
		});
		add(toogle);
		
		add(new Label("title", getModelObject().getTitle()));
		
		WidgetActionsPanel actionsPanel = new WidgetActionsPanel("actions", model);
		add(actionsPanel);
		
		add(new WiQueryEventBehavior(new Event(MouseEvent.MOUSEOVER) {

			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope("$(this).find('.dragbox-actions').show()");
			}
			
		}));
		add(new WiQueryEventBehavior(new Event(MouseEvent.MOUSEOUT) {

			private static final long serialVersionUID = 1L;

			@Override
			public JsScope callback() {
				return JsScope.quickScope("$(this).find('.dragbox-actions').hide()");
			}
						
		}));				
	}

	public Widget getWidget() {
		return getModelObject();
	}
	
	@Override
	public void setDashboardContext(DashboardContext dashboardContext) {
		this.dashboardContext = dashboardContext;
	}

}
