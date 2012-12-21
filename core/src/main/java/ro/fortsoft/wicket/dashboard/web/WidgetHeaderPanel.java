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

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxCallListener;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;

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
		
        setMarkupId("header-" + getModelObject().getId());
        
		// TODO css class
		String imagePath = "images/down.png";
		if (getWidget().isCollapsed()) {
			imagePath = "images/up.png";
		}
		Image toogle = new Image("toggle", new ContextRelativeResource(imagePath));
        toogle.setOutputMarkupId(true);
		toogle.add(new AjaxEventBehavior("onclick") {
		
			private static final long serialVersionUID = 1L;

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				Widget widget = getWidget();
				widget.setCollapsed(!widget.isCollapsed());
				
				Dashboard dashboard = findParent(DashboardPanel.class).getDashboard();
				dashboardContext.getDashboardPersiter().save(dashboard);
			}

			@Override
            protected void updateAjaxAttributes(AjaxRequestAttributes attributes) {
                super.updateAjaxAttributes(attributes);

                attributes.getAjaxCallListeners().add(new AjaxCallListener() {
                	
					private static final long serialVersionUID = 1L;

					@Override
                    public CharSequence getSuccessHandler(Component component) {
                        StringBuilder buffer = new StringBuilder();
                        buffer.append("var content = $('#").append(component.getMarkupId()).append("').parent().siblings('.dragbox-content');");
                        buffer.append("if (content.css('display') == 'none') {");
                        buffer.append("content.slideDown(400);");
                        buffer.append("$(this).attr('src',  '../images/down.png');"); //TODO css class
                        buffer.append("$(this).attr('title', 'Minimize');");
                        buffer.append("} else {");
                        buffer.append("content.slideUp(200);");
                        buffer.append("$(this).attr('src', '../images/up.png');");
                        buffer.append("$(this).attr('title', 'Show');");
                        buffer.append("};");

                        return buffer.append(super.getSuccessHandler(component));
					}
					
				});
			}
			
		});
		toogle.add(new AttributeModifier("title", new AbstractReadOnlyModel<String>() {
			
            private static final long serialVersionUID = 1L;

			@Override
            public String getObject() {
                return getWidget().isCollapsed() ? "Show" : "Minimize";
            }
            
        }));
		add(toogle);
		
		add(new Label("title", getModelObject().getTitle()));
		
		WidgetActionsPanel actionsPanel = new WidgetActionsPanel("actions", model);
		add(actionsPanel);		
	}

	public Widget getWidget() {
		return getModelObject();
	}
	
	@Override
	public void setDashboardContext(DashboardContext dashboardContext) {
		this.dashboardContext = dashboardContext;
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        StringBuilder statement = new StringBuilder("$('#").append(getMarkupId()).append("').on('mouseover', function(ev) {");
        statement.append(" $(this).find('.dragbox-actions').show();").
                  append("}).on('mouseout', function(ev) {").
                  append(" $(this).find('.dragbox-actions').hide();").
                  append("});");

        response.render(OnDomReadyHeaderItem.forScript(statement.toString()));
    }

}
