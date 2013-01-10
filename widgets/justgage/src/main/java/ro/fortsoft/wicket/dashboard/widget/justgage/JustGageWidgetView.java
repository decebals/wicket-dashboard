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
package ro.fortsoft.wicket.dashboard.widget.justgage;

import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import ro.fortsoft.wicket.dashboard.Widget;
import ro.fortsoft.wicket.dashboard.web.WidgetView;

/**
 * @author Decebal Suiu
 */
public class JustGageWidgetView extends WidgetView {

	private static final long serialVersionUID = 1L;

	private ResourceReference raphaelReference = new PackageResourceReference(
			JustGageWidgetView.class, "res/raphael.2.1.0.min.js");
	private ResourceReference justgageReference = new PackageResourceReference(
			JustGageWidgetView.class, "res/justgage.1.0.1.min.js");

	private String gaugeId;
	
	public JustGageWidgetView(String id, Model<Widget> model) {
		super(id, model);
		
		gaugeId = "gauge" + getSession().nextSequenceValue();
		add(new WebMarkupContainer("gauge").setMarkupId(gaugeId));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
		response.render(JavaScriptHeaderItem.forReference(raphaelReference));
		response.render(JavaScriptHeaderItem.forReference(justgageReference));
		
		response.render(OnDomReadyHeaderItem.forScript(getJustGageJavaScript()));
	}
	
	private CharSequence getJustGageJavaScript() {
		/*
		var g = new JustGage({
		    id: "gauge", 
		    value: 67, 
		    min: 0,
		    max: 100,
		    title: "Visitors"
		  }); 
		*/
		
		JustGageWidget widget = (JustGageWidget) getModelObject();		
		JustGage justGage = widget.getJustGage();
		
		StringBuilder function = new StringBuilder();
		function.append("var " + gaugeId + " = ");
		function.append("new JustGage({");
		appendOption("id", gaugeId, true, function);
		appendOption("value", justGage.getValue(), true, function);
		appendOption("min", justGage.getMin(), false, function);
		appendOption("max", justGage.getMax(), false, function);
		appendOption("gaugeColor", justGage.getGaugeColor(), false, function);
		appendOption("label", justGage.getLabel(), false, function);
		appendOption("title", justGage.getTitle(), false, function);
		function.deleteCharAt(function.length() - 1); // delete last ","
		function.append("});");
		
	 	return function;
	}
	
	private StringBuilder appendOption(String name, Object value, boolean required, StringBuilder function) {
		if ((value == null) && !required ) {
			return function;
		}
		
		String optionValue;
		if (value instanceof String) {
			optionValue = "\"" + value + "\"";
		} else {
			optionValue = value.toString();
		}
		
		return function.append(name + ':' + optionValue + ','); 
	}
	
	/*
	private String getGaugeId() {
//		return "gauge-" + getModelObject().getId();
		return getGaugeVarname();
	}
	
	private String getGaugeVarname() {
		return "gauge" + getSession().nextSequenceValue();
	}
	*/
	
}
