package com.kunion.kcvideo;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ProcessHander {

	public static ExecutorService getVideoInfoPool = Executors.newFixedThreadPool(8);
	
	public static int getVideoLength(final String videoPath) throws InterruptedException, ExecutionException {
		
		LogUtil.log("getVideoInfoPool used " + ((ThreadPoolExecutor)getVideoInfoPool).getActiveCount());
		
		Future<Integer> future = getVideoInfoPool.submit(new Callable<Integer>() {

            public Integer call() throws Exception {
                return ProcessAction.getVideoLength(videoPath);
            }

        });
		
		return future.get();
	}
	
	
	public static ExecutorService generateAudioPool = Executors.newFixedThreadPool(8);
	
	public static boolean generateAudio(final String videoPath, final String audioPath) throws InterruptedException, ExecutionException {
		Future<Boolean> future = getVideoInfoPool.submit(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return ProcessAction.generateAudio(videoPath, audioPath);
            }

        });
		
		if(future.get()) {
			File file = new File(audioPath);
			if(file.exists()) {
				return true;
			}
		}
		return false;
	}
	
	public static ExecutorService generateVideoFormImagePool = Executors.newFixedThreadPool(8);
	
	public static boolean generateVideoFormImage(final String imgPath, final String videoPath) throws InterruptedException, ExecutionException {
		Future<Boolean> future = generateVideoFormImagePool.submit(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return ProcessAction.generateVideoFormImage(imgPath, videoPath);
            }

        });
		
		if(future.get()) {
			File file = new File(videoPath);
			if(file.exists()) {
				return true;
			}
		}
		return false;
	}
	
	
	public static ExecutorService mergeVideoAImagePool = Executors.newFixedThreadPool(8);
	
	public static boolean mergeVideoAImage(final String audioPath, final String videoPath, final String outVideoPath) throws InterruptedException, ExecutionException {
		Future<Boolean> future = mergeVideoAImagePool.submit(new Callable<Boolean>() {

            public Boolean call() throws Exception {
                return ProcessAction.mergeVideoAImage(audioPath, videoPath, outVideoPath);
            }

        });
		
		if(future.get()) {
			File file = new File(outVideoPath);
			if(file.exists()) {
				return true;
			}
		}
		return false;
	}
}
