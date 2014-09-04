/*
 * Copyright 2014 Decebal Suiu
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
package ro.fortsoft.wicket.dashboard.web.layout;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.AjaxLazyLoadPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import ro.fortsoft.wicket.dashboard.Widget;

/**
 * @author Decebal Suiu
 */
public class WidgetLoadingPanel extends AjaxLazyLoadPanel {

    private static final long serialVersionUID = 1L;

    public WidgetLoadingPanel(String id, IModel<Widget> model) {
        super(id, model);
    }

    @Override
    public Component getLazyLoadComponent(String id) {
        // for test only
        /*
        try {
            Thread.sleep(40 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

        return findParent(DashboardLayout.class).createWidgetPanel(id, getWidgetModel());
    }

    @Override
    public Component getLoadingComponent(final String markupId) {
        return new Label(markupId, getLoadingText()).setEscapeModelStrings(false).setRenderBodyOnly(true);
    }

    private String getLoadingText() {
        String widgetTitle = getWidgetModel().getObject().getTitle();

        StringBuilder text = new StringBuilder();
//        text.append("<div class='dragbox widget-loading'>");
        text.append("<div class='widget-loading'>");
        text.append("<div class='widget-loading-message'>");
        text.append("<span class='widget-title'>" + widgetTitle + "</span> is loading ...");
        text.append("</div>");
        text.append("</div>");

        return text.toString();
    }

    private IModel<Widget> getWidgetModel() {
        return (IModel<Widget>) getDefaultModel();
    }

}
