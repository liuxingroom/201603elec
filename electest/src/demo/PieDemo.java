package demo;

import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class PieDemo {
	public static void main(String[] args) {
		//构造数据集合
		DefaultPieDataset dataset=new DefaultPieDataset();
		dataset.setValue("北京", 12);
		dataset.setValue("南京", 2);
		dataset.setValue("上海", 4);
		
		JFreeChart chart=ChartFactory.createPieChart3D(
										"用户统计表（所属单位）",    //图形的主标题
								        dataset, 		 //图形的数据
							    	    true, 					  //是否显示子标题
									    true,                     //是否生成数据显示
									    true                      //是否生成URL连接
	    );
		
		//处理主标题乱码
		chart.getTitle().setFont(new Font("宋体",Font.BOLD,18));
		//处理子标题乱码
		chart.getLegend().setItemFont(new Font("宋体",Font.BOLD,15));
		/**
		 * 3种方式获取对象
		 *   * 方式一：断点
		 *   * 方式二：使用System.out.println();
		 *   * 方案三：使用API
		 */
		//获取图表区域对象
		PiePlot3D piePlot3D=(PiePlot3D) chart.getPlot();
		
		//处理图形上显示的乱码
		piePlot3D.setLabelFont(new Font("宋体",Font.BOLD,15));
		//在图形上显示数值（格式：北京 12 （60%））
		String labelFormat="{0} {1} （{2}）";
		piePlot3D.setLabelGenerator(new StandardPieSectionLabelGenerator(labelFormat));
		
		
		/**在D盘目录下生成图片*/
//		File file = new File("D:/chart.png");
//		try {
//			ChartUtilities.saveChartAsPNG(file, chart, 600, 500);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	
		
		
		//使用Rrame加载图形
		ChartFrame chartFrame=new ChartFrame("xyz", chart);
		chartFrame.setVisible(true);
		//输出图形
		chartFrame.pack();
	}
}
