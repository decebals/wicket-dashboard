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
package com.asf.wicket.dashboard.web.text;

import com.asf.wicket.dashboard.WidgetDescriptor;

/**
 * @author Decebal Suiu
 */
public class TextWidgetDescriptor implements WidgetDescriptor {

	private static final long serialVersionUID = 1L;

	public String getDescription() {
		return "A simple text widget.";
	}

	public String getName() {
		return "Text";
	}

	public String getProvider() {
		return "Decebal Suiu";
	}

	public String getWidgetClassName() {
		return TextWidget.class.getName();
	}

}
