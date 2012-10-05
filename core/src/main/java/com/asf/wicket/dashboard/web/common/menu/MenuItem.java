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
package com.asf.wicket.dashboard.web.common.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.link.AbstractLink;

/**
 * @author Decebal Suiu
 */
public class MenuItem implements Serializable {				

	private static final long serialVersionUID = 1L;
	
	private AbstractLink link;
	private String label;
	private String image;
	private String tooltip;
	private List<MenuItem> subMenuItems = new ArrayList<MenuItem>();

	public MenuItem(AbstractLink link, String label) {
		this(link, label, null, null);
	}
	
	public MenuItem(AbstractLink link, String label, String image, String tooltip) {
		if ((link != null) && !link.getId().equals(MenuPanel.LINK_ID)) {
			throw new IllegalArgumentException("The id must be MenuPanel.LINK_ID");
		}
		
		this.link = link;
		this.label = label;
		this.image = image;
		this.tooltip = tooltip;
	}

	public MenuItem(String label) {
		this((String) null, label);
	}

	public MenuItem(String image, String label) {
		this.image = image;
		this.label = label;
	}
	
	public void addMenuItem(MenuItem menuItem) {
		subMenuItems.add(menuItem);
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		subMenuItems.clear();
		for (MenuItem menuItem : menuItems) {
			addMenuItem(menuItem);
		}
	}

	public AbstractLink getLink() {
		return link;
	}

	public String getLabel() {
		return label;
	}

	public String getImage() {
		return image;
	}
	
	public List<MenuItem> getChildren() {
		return subMenuItems;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("MenuItem[");
		buffer.append("label = ").append(label);
		buffer.append("]");

		return buffer.toString();
	}

}
