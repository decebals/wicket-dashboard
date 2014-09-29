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
package ro.fortsoft.wicket.dashboard.demo;

import java.util.List;

import ro.fortsoft.wicket.dashboard.DefaultWidgetActionsFactory;
import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.WidgetAction;
import ro.fortsoft.wicket.dashboard.web.AbstractWidgetAction;
import ro.fortsoft.wicket.dashboard.web.DefaultWidgetActions;

/**
 * @author Decebal Suiu
 */
public class DemoWidgetActionsFactory extends DefaultWidgetActionsFactory {

	@Override
	public List<WidgetAction> createWidgetActions(Widget widget) {
		List<WidgetAction> widgetActions = super.createWidgetActions(widget);
		widgetActions.add(0, new DetachWidgetAction(widget));

        /*
        for (WidgetAction action : widgetActions) {
            if (action instanceof DefaultWidgetActions.Settings) {
                // change the icon (relative to context)
                ((AbstractWidgetAction) action).setImage("/images/edit.png");
                break;
            }
        }
        */

		return widgetActions;
	}

}
