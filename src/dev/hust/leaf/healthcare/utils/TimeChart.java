/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.hust.leaf.healthcare.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

/**
 * Project status demo chart.
 */
public class TimeChart extends AbstractDemoChart {

	private Context context;

	public TimeChart(Context context) {
		this.context = context;
	}

	/**
	 * Executes the chart demo.
	 * 
	 * @param context
	 *            the context
	 * @return the built intent
	 */
	public View getChart(double[] x, double[] value) {
		String[] titles = new String[] { "心电图" };
		// List<Date[]> dates = new ArrayList<Date[]>();
		List<double[]> xsia = new ArrayList<double[]>();
		List<double[]> values = new ArrayList<double[]>();
		// int length = titles.length;
		// dates.add(date);
		xsia.add(x);
		values.add(value);

		int length = value.length;
		double sum = 0;
		for (int i = 0; i < length; i++) {
			sum += value[i];
		}
		double average = sum / length;

		int[] colors = new int[] { Color.GREEN };
		PointStyle[] styles = new PointStyle[] { PointStyle.POINT };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, "心电数据统计图", "Time(ms)", "Voltage(mv)", x[0],
				x[length - 1], 0, average * 2, Color.GRAY, Color.LTGRAY,
				Color.BLACK, Color.BLACK);

		// renderer.addYTextLabel(100, "test");
		length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			SimpleSeriesRenderer seriesRenderer = renderer
					.getSeriesRendererAt(i);
			seriesRenderer.setDisplayChartValues(true);
		}
		renderer.setInScroll(true);
		renderer.setXLabels(5);
		// renderer.setYLabels(10);
		renderer.setXRoundedLabels(true);
		renderer.setShowLabels(true);
		renderer.setShowLegend(false);

		XYMultipleSeriesDataset dataset = buildDataset(titles, xsia, values);

		/*
		 * ChartFactory .getTimeChartView(context, buildDateDataset(titles,
		 * dates, values), renderer, "MM/dd/yyyy");
		 */

		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

}
