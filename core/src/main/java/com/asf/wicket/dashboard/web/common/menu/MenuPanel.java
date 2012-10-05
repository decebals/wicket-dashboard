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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * @author Decebal Suiu
 */
public class MenuPanel extends GenericPanel<List<MenuItem>> {

	private static final long serialVersionUID = 1L;
	
	public static final String LINK_ID = "linkId";
	public static final String LINK_IMAGE_ID = "linkImage";
	public static final String LINK_TEXT_ID = "linkText";	
	
	private List<MenuItem> topMenuItems = new ArrayList<MenuItem>();
	
	public MenuPanel(String id) {
		super(id);
		
		setModel(new PropertyModel<List<MenuItem>>(this, "topMenuItems"));
		
		add(new SubMenuListView("topMenuItems", getModel()));
	}
	
	public MenuPanel(String id, IModel<List<MenuItem>> model) {
		super(id, model);

		add(new SubMenuListView("topMenuItems", model));
	}

	public void addMenuItem(MenuItem menu) {
		topMenuItems.add(menu);
	}

	public void setMenuItems(List<MenuItem> menuItems) {
		clear();
		topMenuItems.addAll(menuItems);
	}
	
	public void clear() {
		topMenuItems.clear();		
	}

	private class SubMenuListView extends ListView<MenuItem> {

		private static final long serialVersionUID = 1L;

		private SubMenuListView(String id, IModel<List<MenuItem>> model) {
			super(id, model);
		}

		private SubMenuListView(String id, List<MenuItem> list) {
			super(id, list);
		}

		@Override
		protected void populateItem(ListItem<MenuItem> item) {
			MenuItem menuItem = item.getModelObject();
			item.add(new MenuItemFragment(menuItem));
		}
		
	}

	private class MenuItemFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public MenuItemFragment(MenuItem menuItem) {
			super("menuItemFragment", "MENU_ITEM_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			// add the menu's label (hyperlinked if a link is provided)
			if (menuItem.getLink() != null) {
				if (menuItem.getImage() != null) {
					if (menuItem.getLabel() != null) {
						add(new LinkImageTextFragment(menuItem));
					} else {
						add(new LinkImageFragment(menuItem));
					}
				} else {
					add(new LinkFragment(menuItem));
				}
			} else { // TODO make sense a menu item without link ?
				if (menuItem.getImage() != null) {
					if (menuItem.getLabel() == null) {
						add(new ImageFragment(menuItem));
					}
				} else {
					add(new TextFragment(menuItem));
				}
			}
			WebMarkupContainer menuItemList = new WebMarkupContainer("menuItemList");
			add(menuItemList);
			
			// hide the <ul> tag if there are no submenus
			menuItemList.setVisible(menuItem.getChildren().size() > 0);
			
			/*
			// add a down or right arrow icon if there are children
			if (menuItem.getChildren().size() > 0) {
				menuItem.getLabel().add(new AttributeAppender("class", new Model("menu-has-submenu"), " ");
			}
			*/
			
			// add the submenus
			menuItemList.add(new SubMenuListView("menuItemLinks", menuItem.getChildren()));
		}
	}

	private class LinkFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public LinkFragment(MenuItem menuItem) {
			super("linkFragment", "LINK_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			AbstractLink link = menuItem.getLink();
			link.add(new Label(LINK_TEXT_ID, menuItem.getLabel()));
			add(link);
		}
		
	}

	private class LinkImageTextFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public LinkImageTextFragment(MenuItem menuItem) {
			super("linkFragment", "LINK_IMAGE_TEXT_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			AbstractLink link = menuItem.getLink();
			link.add(new ContextImage(LINK_IMAGE_ID, menuItem.getImage()));
			link.add(new Label(LINK_TEXT_ID, menuItem.getLabel()));
			link.add(AttributeModifier.replace("title", menuItem.getTooltip()));
//			link.add(new TipTipBehavior(new TipTipOptions().maxWidth("auto")));
			add(link);
		}
		
	}

	private class LinkImageFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public LinkImageFragment(MenuItem menuItem) {
			super("linkFragment", "LINK_IMAGE_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			AbstractLink link = menuItem.getLink();
			link.add(new ContextImage(LINK_IMAGE_ID, menuItem.getImage()));
			link.add(AttributeModifier.replace("title", menuItem.getTooltip()));
//			link.add(new TipTipBehavior(new TipTipOptions().maxWidth("auto")));
			add(link);
		}
		
	}

	private class ImageFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public ImageFragment(MenuItem menuItem) {
			super("linkFragment", "IMAGE_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			add(new ContextImage(LINK_IMAGE_ID, menuItem.getImage()));
		}
		
	}
	
	private class TextFragment extends Fragment {

		private static final long serialVersionUID = 1L;

		public TextFragment(MenuItem menuItem) {
			super("linkFragment", "TEXT_FRAGMENT", MenuPanel.this);
			
			setRenderBodyOnly(true);
			
			add(new Label(LINK_TEXT_ID, menuItem.getLabel()));
		}
		
	}
	
}
