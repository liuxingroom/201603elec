package com.xing.elec.utils;

import java.io.File;
import java.util.Date;
import java.util.UUID;

import org.apache.struts2.ServletActionContext;

public class FileUtils {

	/**
	 * 完成文件上传的同时，返回路径path
	 * @param fileNames 上传的文件名
	 * @param uploads 上传的文件
	 * @param model：模块名称
	 * @return：文件路径
	 * 
	 * 1：完成文件上传的要求
		  1：将上传的文件统一放置到upload的文件夹下
		  2：将每天上传的文件，使用日期格式的文件夹分开，将每个业务的模块放置统一文件夹下
		  3：上传的文件名要指定唯一，可以使用UUID的方式，也可以使用日期作为文件名
		  4：封装一个文件上传的方法，该方法可以支持多文件的上传，即支持各种格式文件的上传
		  5：保存路径path的时候，使用相对路径进行保存，这样便于项目的可移植性
	 */
	public static String findUploadReturnPath(File uploads, String fileNames, String model) {
		// TODO Auto-generated method stub
		//1:获取需要上传文件统一的路径path（即upload）
		String basepath=ServletActionContext.getServletContext().getRealPath("/upload");
		//2:获取日期文件夹(格式：/yyyy/MM/dd/)
		String datepath=DateUtils.dateToStringByFile(new Date());
		//格式（upload\2014\12\01\用户管理）
		String filePath=basepath+datepath+model;
		//3：判断该文件夹是否存在，如果不存在，创建
		File dateFile=new File(filePath);
		//判断该文件夹是否存在，如果不存在，创建
		if(!dateFile.exists()){
			dateFile.mkdirs();//创建文件夹
		}
		//4：指定对应的文件名
		//文件的后缀
		String prifix=fileNames.substring(fileNames.lastIndexOf("."));
		//UUID的文件名
		String uuidFileName=UUID.randomUUID().toString()+prifix;
		//最终上传的文件（目标文件）
		File destFile=new File(dateFile, uuidFileName);
		//上传文件
		uploads.renameTo(destFile);
		
		return "/upload"+datepath+model+"/"+uuidFileName;
	}
	
	//write by me
	public static String findUploadReturnPath2(File upload,
			String fileName, String model) {
		//1.获取需要上传文件统一的路径path
		String basepath=ServletActionContext.getServletContext().getRealPath("/upload");
		//获取日期文件夹(格式：/yyyy/MM/dd/)
		String datepath=DateUtils.dateToStringByFile(new Date());
		//整理上传文件的目录格式
		String filepath=basepath+datepath+model;
		//判断该文件夹是否存在
		File datefile=new File(filepath);
		if(!datefile.exists()){
			datefile.mkdirs();//创建文件夹
		}
		//指定对应的文件名
		//文件的后缀
		String prifix=fileName.substring(fileName.lastIndexOf("."));
		//UUID的文件名
		String uuidFileName=UUID.randomUUID().toString()+prifix;
		//最终上传的文件（目标文件）
		File destFile=new File(datefile,uuidFileName);
		//文件上传
		upload.renameTo(destFile);
		return "/upload"+datepath+model+"/"+uuidFileName;
	}
	
}
