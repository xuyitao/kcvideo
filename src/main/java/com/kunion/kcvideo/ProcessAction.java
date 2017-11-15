package com.kunion.kcvideo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

//get video length -> result in seconds
//
//ffprobe -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 input.mp4

//filter audio from video
//ffmpeg -i Sample.avi -vn -ar 44100 -ac 2 -ab 192k -f mp3 Sample.mp3

//merge images to video
//ffmpeg -f image2  -r 1/5  -i img%02d.jpg -r 6   -c:v libx264     -y out.mp4

//image to video with fade in & out
//ffmpeg -f image2  -r 1/5 -i img%02d.jpg  -vf zoompan=d=2:fps=1/4,framerate=25:interp_start=0:interp_end=255  -r 6  -y out.mp4


//merge audio video
//ffmpeg -i video.mp4 -i audio.wav -c:v copy -c:a aac -strict experimental output.mp4


// add subtitle to default
// ffmpeg -i 5.mp4 -f srt -i 5.srt -c:v copy -c:a copy -c:s mov_text -disposition:s:0 default  outfile.mp4
// hard burn subtitle 
// ffmpeg -i 5.mp4 -filter:v subtitles=5.srt output.mp4

//图片转视频 
//ffmpeg -i %d.jpg -vf "zoompan=z='if(lte(zoom,1.0),1.5,max(1.001,zoom-0.008))':d=125:s=200x200,fade=t=in:d=1" -c:v libx264 -t 15 -s 200x200 -y zoomout.mp

//视频抽出音频
//ffmpeg -i toutiao.mp4 -vn -acodec copy output-audio.aac

//视频音频合并
//ffmpeg -i video.mp4 -i audio.wav -c copy output.mkv


public class ProcessAction {

	public static Integer getVideoLength(String videoPath) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String cmd = Configure.ffprobe + " -v error -show_entries format=duration -of default=noprint_wrappers=1:nokey=1 " + getRealPath(videoPath);
//		LogUtil.log(cmd);
		Process pr = rt.exec(cmd);
		
		String result = readerResult(pr.getInputStream());
		LogUtil.log("getVideoLength  " + result);
	    Float time = Float.parseFloat(result);
//	    LogUtil.log(time.intValue()+"");
	    pr.destroy();
	    return time.intValue();
	
	}
	
	public static Boolean generateAudio(String videoPath, String audioPath) throws IOException {
		Runtime rt = Runtime.getRuntime();
		String cmd = Configure.ffmpeg + " -i " + getRealPath(videoPath)+ " -vn -acodec copy -y " + getRealPath(audioPath);
//		LogUtil.log(cmd);
		Process pr = rt.exec(cmd);
		
		String result = readerResult(pr.getErrorStream());
		pr.destroy();
	    return Boolean.TRUE;
	
	}
	
	//ffmpeg -i %d.jpg -vf "zoompan=z='if(lte(zoom,1.0),1.5,max(1.001,zoom-0.008))':d=125:s=200x200,fade=t=in:d=1" -c:v libx264 -t 15 -s 200x200 -y zoomout.mp
	public static Boolean generateVideoFormImage(String imgPath, String videoPath) throws IOException, TimeoutException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(Configure.ffmpeg);
		sBuilder.append(" -f image2 -r 1/"+Configure.oneImageSecond+" -i ");
		sBuilder.append(getRealPath(imgPath));
		sBuilder.append(" -pix_fmt yuvj420p -y ");
		sBuilder.append(getRealPath(videoPath));
		String cmd = sBuilder.toString();
//		LogUtil.log(cmd);
		Process pr = rt.exec(cmd);

		String result = readerResult(pr.getErrorStream());
		pr.destroy();
	    return Boolean.TRUE;
	
	}
	
	//ffmpeg -i %d.jpg -vf "zoompan=z='if(lte(zoom,1.0),1.5,max(1.001,zoom-0.008))':d=125:s=200x200,fade=t=in:d=1" -c:v libx264 -t 15 -s 200x200 -y zoomout.mp
	public static Boolean mergeVideoAImage(String audioPath, String videoPath, String outVideoPath) throws IOException {
		Runtime rt = Runtime.getRuntime();
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(Configure.ffmpeg);
		sBuilder.append(" -i ");
		sBuilder.append(getRealPath(videoPath));
		sBuilder.append(" -i ");
		sBuilder.append(getRealPath(audioPath));
		sBuilder.append(" -c:v copy -c:a aac -strict experimental -y ");
		sBuilder.append(getRealPath(outVideoPath));
		String cmd = sBuilder.toString();
//		LogUtil.log(cmd);
		Process pr = rt.exec(cmd);

		String result = readerResult(pr.getErrorStream());
		
		pr.destroy();
	    return Boolean.TRUE;
	
	}
	
	
	public static String readerResult(InputStream is) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(is));
		StringBuilder sbBuilder = new StringBuilder();
		String line;
		while (( line = input.readLine()) != null) {
//	    	LogUtil.log("readerResult"+line);
	    	sbBuilder.append(line);
	    }
		input.close();
		return sbBuilder.toString();
	}
	
	public static String getRealPath(String file) {
		return "\""+new File(file).getAbsolutePath()+"\"";
	}
}
