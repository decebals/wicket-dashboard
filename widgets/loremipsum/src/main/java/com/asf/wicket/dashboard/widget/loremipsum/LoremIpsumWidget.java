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
package com.asf.wicket.dashboard.widget.loremipsum;

import org.apache.wicket.model.Model;

import com.asf.wicket.dashboard.AbstractWidget;
import com.asf.wicket.dashboard.Widget;
import com.asf.wicket.dashboard.web.WidgetView;

import de.svenjacobs.loremipsum.LoremIpsum;

/**
 * @author Decebal Suiu
 */
public class LoremIpsumWidget extends AbstractWidget {

	private static final long serialVersionUID = 1L;
	
	private transient LoremIpsum loremIpsum;
	
	public LoremIpsumWidget(String id) {
		super();
		
		this.id = id; 
		title = "Text";
	}

	public String getText() {
		if (loremIpsum == null) {
			loremIpsum = new LoremIpsum();
		}
				
		return loremIpsum.getWords();
	}

	public WidgetView createView(String viewId) {
		return new LoremIpsumWidgetView(viewId, new Model<Widget>(this));
	}

}