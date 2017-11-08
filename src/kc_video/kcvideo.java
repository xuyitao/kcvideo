package kc_video;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// get video length -> result in seconds
// 
// ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 input.mp4

// filter audio from video
// ffmpeg -i Sample.avi -vn -ar 44100 -ac 2 -ab 192k -f mp3 Sample.mp3

// merge images to video
// ffmpeg -f image2  -r 1/5  -i img%02d.jpg -r 6   -c:v libx264     -y out.mp4

// image to video with fade in & out
// ffmpeg -f image2  -r 1/5 -i img%02d.jpg  -vf zoompan=d=2:fps=1/4,framerate=25:interp_start=0:interp_end=255  -r 6  -y out.mp4


// merge audio video
// ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac -strict experimental output.mp4



// 图片转视频 
// ffmpeg -i %d.jpg -vf "zoompan=z='if(lte(zoom,1.0),1.5,max(1.001,zoom-0.008))':d=125:s=200x200,fade=t=in:d=1" -c:v libx264 -t 15 -s 200x200 -y zoomout.mp

// 视频抽出音频
// ffmpeg -i toutiao.mp4 -vn -acodec copy output-audio.aac

// 视频音频合并
// ffmpeg -i video.mp4 -i audio.wav -c copy output.mkv


public class kcvideo {
	
	interface Callback {
	    void onResult(boolean result, int value);
	}

	private static String ffprobe = "/usr/local/bin/ffprobe";
	private static String ffmpeg = "/usr/local/bin/ffmpeg";
	
	private static boolean continueRunning = true;
	
	private String videoFolderPath = "./video";
	private String imageFolderPath = "./image";
	private String tmpFolderPath = "./tmp";
	
	public kcvideo() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("i'm running");
		
		while (continueRunning) {
			
			System.out.print("round start \n");
			

			getFileDuration("v1.mp4", new Callback() {
	            @Override
	            public void onResult(boolean result, int value) {
	                // do some stuff
	            	if (result){
	            		System.out.print("getFileDuration \n" + value);
	            	}
	            	else{
	            		
	            	}
	            	
	            }
	        });
			
			
			// first list out all the video files 
			
			
			// pick one video file to process - if file is still writing then do next 
			
			// calculate video file's length
			
			// generate video's audio file
			
			
			// repeatly do following process
			
			
				// pick random x files for extend to length of video
			
				// generate video fo x files, 
			
				// combine video and audio file output final video
			
			// go sleep one second
				
			
			try {
				TimeUnit.SECONDS.sleep(100);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		
	}
	
	public String doConvert( String videopath, String[] images){
		
		return "";
	}
	
	
	public static void getFileDuration (String videopath, final Callback callback){
		Runtime rt = Runtime.getRuntime();
		try {
			String cmd = ffprobe + " -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 ./videos/" + videopath;
			System.out.println(cmd);
			Process pr = rt.exec(cmd);
			new Thread(new Runnable() {
				public void run() {
					BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
					String line = null; 
					String tline = null; 
					try {
					    while ((line = input.readLine()) != null)
					    	if (tline != null)
					    		tline = tline + line;
					    	else 
					    		tline = line;
					   
					    callback.onResult(true, (int)Float.parseFloat(tline));
					} catch (IOException e) {
						e.printStackTrace();
						callback.onResult(false, 0);
					}
				}
			}).start();
			pr.waitFor();
		} catch (IOException | InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			callback.onResult(false, 0);
		}
		return;
	}
	
	
	private boolean isCompletelyWritten(File file) {
	    Long fileSizeBefore = file.length();
	    try {
			TimeUnit.SECONDS.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	    
	    Long fileSizeAfter = file.length();

	    System.out.println("comparing file size " + fileSizeBefore + " with " + fileSizeAfter);

	    if (fileSizeBefore.equals(fileSizeAfter)) {
	        return true;
	    }
	    return false;
	}

}
