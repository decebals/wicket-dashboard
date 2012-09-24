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
package com.asf.wicket.dashboard.web.chart;

import java.util.Arrays;
import java.util.List;

import jofc2.model.Chart;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.asf.wicket.dashboard.AbstractWidget;
import com.asf.wicket.dashboard.Widget;
import com.asf.wicket.dashboard.demo.chart.ChartDemoFactory;
import com.asf.wicket.dashboard.web.WidgetView;

/**
 * @author Decebal Suiu
 */
public class ChartWidget extends AbstractWidget {

	private static final long serialVersionUID = 1L;
	
	public static final String BAR_TYPE = "Bar";
	public static final String DOUBLE_BAR_TYPE = "DoubleBar";
	public static final String LINE_TYPE = "Line";
	public static final String DOTED_LINE_TYPE = "DotedLine";
	public static final String PIE_TYPE = "Pie";
	public static final String SCATTER_TYPE = "Scatter";

	public static final List<String> TYPES = Arrays.asList(new String[] {
			BAR_TYPE,
			DOUBLE_BAR_TYPE,
			LINE_TYPE,
			DOTED_LINE_TYPE,
			PIE_TYPE,
			SCATTER_TYPE
	});
	
	private transient Chart chart;
	
	public ChartWidget(String id) {
		super();
		
		this.id = id;
		title = "Chart";
	}

	@Override
	public void init() {
		if (!settings.containsKey("chartType")) {
			settings.put("chartType", BAR_TYPE);
		}
	}

	public Chart getChart() {
		String chartType = getSettings().get("chartType");
		if (BAR_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoBarChart();
		} else if (DOUBLE_BAR_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoDoubleBarChart();
		} else if (LINE_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoLineChart();
		} else if (DOTED_LINE_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoDotedLineChart();			
		} else if (PIE_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoPieChart();
		} else if (SCATTER_TYPE.equals(chartType)) {
			chart = ChartDemoFactory.createDemoScatterChart();			
		}
		
		return chart;
	}

	public WidgetView createView(String viewId) {
		return new ChartWidgetView(viewId, new Model<Widget>(this));
	}

	@Override
	public boolean hasSettings() {
		return true;
	}

	@Override
	public Panel createSettingsPanel(String settingsPanelId) {
		return new ChartSettingsPanel(settingsPanelId, this);
	}

}
