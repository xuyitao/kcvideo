package com.kunion.kcvideo;

import java.io.File;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
	public static VideoManager videoManager = new VideoManager();
	
    public static void main( String[] args )
    {
    	TaskHelper helper = new TaskHelper();
    	
    	while(true) {
    		
    		List<String> newVideos = videoManager.getUnHandlerVideo();
    		
    		LogUtil.log("增加新视频  " + newVideos.size() + "  " + newVideos.toString());
    		
    		for(String videoFileName : newVideos) {
    			
    	    	TaskBean bean = new TaskBean(videoFileName);
    	    	helper.addTask(bean);
    	    	videoManager.handlerVideo(bean);
    		}
	    	
    		try {
    			helper.poolLog();
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
}
