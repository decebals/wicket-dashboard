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

/**
 * @author Decebal Suiu
 */
public class JustGage {

	private String title;
	private int value;
	private int min;
	private int max;
	private String gaugeColor;
	
	public JustGage setValue(int value) {
		this.value = value;
		return this;
	}
	
	public int getValue() {
		return value;
	}

	public JustGage setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public String getTitle() {
		return title;
	}

	public JustGage setMin(int min) {
		this.min = min;
		return this;
	}

	public int getMin() {
		return min;
	}

	public JustGage setMax(int max) {
		this.max = max;
		return this;
	}
	
	public int getMax() {
		return max;
	}

	public String getGaugeColor() {
		return gaugeColor;
	}

	public JustGage setGaugeColor(String gaugeColor) {
		this.gaugeColor = gaugeColor;
		return this;
	}

}
