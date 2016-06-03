package demo;

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;

public class BarDemo2 {
	public static void main(String[] args) {
		//构造数据集合
		DefaultCategoryDataset dataset=new DefaultCategoryDataset();
		dataset.addValue(12, "所属单位", "北京");
		dataset.addValue(2, "所属单位", "上海");
		dataset.addValue(6, "所属单位", "深圳");
		
		dataset.addValue(12, "美国", "纽约");
		dataset.addValue(6, "美国", "西版图");
		dataset.addValue(4, "美国", "华盛顿");
		
		JFreeChart chart=ChartFactory.createBarChart3D(
										"用户统计表（所属单位）",    //图形的主标题
									    "所属单位名称",        //种类轴的标签 
									    "数量", 			  //图形的数据集合
									    dataset, 		 //图形的数据
									    PlotOrientation.VERTICAL, //图表的显示形式（水平和垂直）
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
		CategoryPlot categoryPlot=(CategoryPlot) chart.getPlot();
		//获取X轴对象
		CategoryAxis3D categoryAxis3D=(CategoryAxis3D) categoryPlot.getDomainAxis();
		//获取Y轴对象
		NumberAxis3D numberAxis3D=(NumberAxis3D) categoryPlot.getRangeAxis();
		//处理X轴上的乱码
		categoryAxis3D.setLabelFont(new Font("宋体",Font.BOLD,15));
		//处理X轴外的乱码
		categoryAxis3D.setTickLabelFont(new Font("宋体",Font.BOLD,15));
		//处理Y轴上的乱码
		numberAxis3D.setLabelFont(new Font("宋体",Font.BOLD,15));
		//处理Y轴外的乱码
		numberAxis3D.setTickLabelFont(new Font("宋体",Font.BOLD,15));
		
		//设置刻度为1的单位
		numberAxis3D.setAutoTickUnitSelection(false);//手动设置
		NumberTickUnit unit=new NumberTickUnit(1);//以1位单位
		numberAxis3D.setTickUnit(unit);
		
		//获取绘图区域对象
		BarRenderer3D barRenderer3D=(BarRenderer3D) categoryPlot.getRenderer();
		//让图形变的秒苗条
		barRenderer3D.setMaximumBarWidth(0.08);
		//在图形上生成数字
		barRenderer3D.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		barRenderer3D.setBaseItemLabelsVisible(true);
		barRenderer3D.setBaseItemLabelFont(new Font("宋体",Font.BOLD,15));
		
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
