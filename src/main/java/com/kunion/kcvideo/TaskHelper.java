package com.kunion.kcvideo;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
				// hold
				if(!FileUtil.isCompletelyFile(mBean.getInFilePath())) {
					LogUtil.log(mBean.getInFileName()+" file isnot complete time out");
					return ;
				}
				
				// calculate video file's length
				Integer videoLength = ProcessHander.getVideoLength(mBean.getInFilePath());
				LogUtil.log(mBean.getInFileName()+" videoLength is  "+videoLength);
				
				// pick random x files for extend to length of video
				//一张图片5秒  时长/5+1 = 需要的图片数量
				int needNum = Float.valueOf(videoLength / Configure.oneImageSecond + 1).intValue();
				
				// 根据现有图片的数量，计算出需要生成几个视频
				
				int workTime = Configure.calculateGenerationTime(needNum);
				
				LogUtil.log("Supposed to generate " + workTime +" videos  ");
				
				for (int workIdx = 0; workIdx< workTime; workIdx++){
					
					LogUtil.log("Generating  " + workIdx  + " video  ");
					
					// generate video's audio file
					Boolean isSus = ProcessHander.generateAudio(mBean.getInFilePath(), mBean.getTmpAudioPath());
					if(!isSus) {
						LogUtil.log(mBean.getInFileName()+" generateAudio fail");
						return ;
					}
					
					// 生成新一轮的图片
					FileUtil.makeExportImage(needNum, mBean.getTmpDir());
					
					// generate video fo x files, 
					isSus = ProcessHander.generateVideoFormImage(mBean.getTmpImageFormat(), mBean.getTmpVideoPath());
					if(!isSus) {
						LogUtil.log(mBean.getInFileName()+" generateVideoFormImage fail");
						return ;
					}
					
					// combine video and audio file output final video
					isSus = ProcessHander.mergeVideoAImage(mBean.getTmpAudioPath(), mBean.getTmpVideoPath(), mBean.getWorkingOutFileName(workIdx+1));
					if(!isSus) {
						LogUtil.log(mBean.getInFileName()+" mergeVideoAImage fail");
						return;
					}
					
					// go sleep one second
					
					LogUtil.log(mBean.getInFileName() + "  finish");
					FileUtil.pruneDir(mBean.getTmpDir());
				}
				App.videoManager.finishVideo(mBean);
				FileUtil.deleteDir(mBean.getTmpDir());

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		
	}
}
