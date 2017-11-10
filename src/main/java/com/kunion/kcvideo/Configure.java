package com.kunion.kcvideo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Configure {


	public static String bootFolderPath = ".";
	
	public static String ffprobe = "/usr/local/bin/ffprobe";
	public static String ffmpeg = "/usr/local/bin/ffmpeg";
	
	
//	public static String ffprobe = bootFolderPath + "/tools/ffprobe";
//	public static String ffmpeg = bootFolderPath + "/tools/ffmpeg";
	public static String videoFolderPath = bootFolderPath + "/videos";
	public static String imageFolderPath = bootFolderPath + "/images";
	public static String tmpFolderPath = bootFolderPath + "/tmp";
	
	public static String tmpImageFormat = "img%03d.jpg";
	public static int oneImageSecond = 5;
	
	
	public static String getVideoFullPath(String fileName) {
		return videoFolderPath + "/" + fileName;
	}
	
	public static String getTmpFolder() {
		return tmpFolderPath;
	}
	
	public static String getTmpFullPath(String fileName) {
		return tmpFolderPath +"/" + fileName;
	}
	
	public static List<String> imagePaths;
	public static List<String> generateImages() {
		if(imagePaths == null) {
			imagePaths = new ArrayList<String>();
			File imgPath = new File(imageFolderPath);
			File [] imgDirList=imgPath.listFiles();
			
			for(File imgDir : imgDirList) {
				LogUtil.log(imgDir.getAbsolutePath());
				imagePaths.add(imgDir.getAbsolutePath());
			}
			
		}
		return imagePaths;
	}
}
