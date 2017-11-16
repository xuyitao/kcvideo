package com.kunion.kcvideo;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoManager {
	
	private File mVideoDir;
	private List<String> videoHandleringList;
	
	public static String DONE = "done";
	
	public VideoManager() {
		mVideoDir = new File(Configure.videoFolderPath);
		videoHandleringList = new ArrayList<String>();
		
	}
	
	
    public List<String> getUnHandlerVideo() {
    	
  
   	
    	List<String> newVideoList=new ArrayList<String>();
    	if(mVideoDir.isDirectory()) {
    		String [] newVideos = mVideoDir.list(new FilenameFilter() {
				
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					String prefix=name.substring(0, DONE.length());
					
					return !prefix.equalsIgnoreCase(DONE);
				}
			});
 
    		List<String> tmpVideoList = new ArrayList<String>(Arrays.asList(newVideos));
    		for(String filename : tmpVideoList) {
    			boolean isContains=false;
    			for(String filenameHandler : videoHandleringList) {
    				if(filenameHandler.equalsIgnoreCase(filename)){
    					isContains = true;
    				}
    			}
    			// for mac os .DS_Store file case - actually needs more for image types
				if (filename.startsWith(".")){
					isContains = true;
				}
    			if(!isContains) {
    				newVideoList.add(filename);
    			}
    		}
    	}
    	
    	return newVideoList;
    }
    
    public void handlerVideo(String filename) {
    	for(String filenameHandler : videoHandleringList) {
			if(filenameHandler.equalsIgnoreCase(filename)){
				return ;
			}
		}
    	videoHandleringList.add(filename);
    }
    
    public void handlerVideo(TaskBean bean) {
    	handlerVideo(bean.getInFileName());
    }
    
    public boolean finishVideo(String filename) {
    	File filepath = new File(Configure.videoFolderPath,filename);
    	if(filepath.exists()) {
    		filepath.renameTo(new File(Configure.videoFolderPath, DONE+filename));
    		
    	}
    	
    	for(String filenameHandler : videoHandleringList) {
			if(filenameHandler.equalsIgnoreCase(filename)){
				videoHandleringList.remove(filename);
				return true;
			}
		}
    	return false;
    }
    
    public boolean finishVideo(TaskBean bean) {
    	return finishVideo(bean.getInFileName());
    }
}
