package com.kunion.kcvideo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
	public static String outputFolderPath = bootFolderPath + "/output";
	
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
	
	public static String getOutputFullPath(String fileName) {
		return outputFolderPath +"/" + fileName;
	}
	
	public static void scanImageFolder(){
		imagePaths = new ArrayList<String>();
		File imgPath = new File(imageFolderPath);
		File [] imgDirList=imgPath.listFiles();
		
		// first get qualified files list
		for(File imgDir : imgDirList) {
			//LogUtil.log(imgDir.getAbsolutePath());
			String fileExt = FileUtil.getExtension(imgDir);
			if (fileExt.equals("png")  || fileExt.equals("jpg")  || fileExt.equals("jpeg") ){
				imagePaths.add(imgDir.getAbsolutePath());					
			}
		}
	}
	
	public static List<String> imagePaths;
	public static List<String> generateImages(int needNum) {
		if(imagePaths == null) {
			scanImageFolder();
		}
		
		List<String> pickedImgs = null;
		if (needNum < imagePaths.size()){
			// randomly pick 
			Collections.shuffle(imagePaths);
			pickedImgs = imagePaths.subList(0, needNum);
		}
		else{
			pickedImgs = imagePaths;
		}
		
		return pickedImgs;
	}
	
	public static int calculateGenerationTime(int needNum){
		if(imagePaths == null) {
			scanImageFolder();
		}
		
		// this is adjustable here
		int workTime = imagePaths.size() / (4 * needNum);
		if (workTime < 1)
			workTime = 1;
		
		return workTime;
	}
}
