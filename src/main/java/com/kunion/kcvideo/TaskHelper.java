package com.kunion.kcvideo;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskHelper {

	ExecutorService threadPool = Executors.newCachedThreadPool();
	
	
	public void addTask(TaskBean bean) {
		if(bean.isValid()) {
			threadPool.submit(new TaskThread(bean));
		} else {
			LogUtil.log(bean.getInFileName() + "is not exist");
		}
	}
	
	class TaskThread implements Runnable {

		private TaskBean mBean;
		public TaskThread(TaskBean bean) {
			mBean = bean;
		}
		
		public void run() {
			// TODO Auto-generated method stub
			if(mBean == null) {
				LogUtil.log("error bean is null");
				return ;
			}
			LogUtil.log(mBean.getInFileName() + "   starting");
			try {
				// first list out all the video files 
				
				// pick one video file to process - if file is still writing then do next 
				
				// calculate video file's length
				Integer videoLength = ProcessHander.getVideoLength(mBean.getInFilePath());
				LogUtil.log(mBean.getInFileName()+" videoLength is  "+videoLength);
				
				// generate video's audio file
				Boolean isSus = ProcessHander.generateAudio(mBean.getInFilePath(), mBean.getTmpAudioPath());
				if(!isSus) {
					LogUtil.log(mBean.getInFileName()+" generateAudio fail");
					return ;
				}
				// repeatly do following process
				
				
				// pick random x files for extend to length of video
				//一张图片5秒  时长/5+1 = 需要的图片数量
				int needNum = Float.valueOf(videoLength / Configure.oneImageSecond + 1).intValue();
				FileUtil.makeExportImage(needNum, mBean.getTmpDir());
				
				// generate video fo x files, 
				isSus = ProcessHander.generateVideoFormImage(mBean.getTmpImageFormat(), mBean.getTmpVideoPath());
				if(!isSus) {
					LogUtil.log(mBean.getInFileName()+" generateVideoFormImage fail");
					return ;
				}
				
				
				// combine video and audio file output final video
				isSus = ProcessHander.mergeVideoAImage(mBean.getTmpAudioPath(), mBean.getTmpVideoPath(), mBean.getOutFilePath());
				if(!isSus) {
					LogUtil.log(mBean.getInFileName()+" mergeVideoAImage fail");
					return ;
				}
				
				
				FileUtil.deleteDir(mBean.getTmpDir());
				// go sleep one second
				
				LogUtil.log(mBean.getInFileName() + "  finish");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
	}
}
