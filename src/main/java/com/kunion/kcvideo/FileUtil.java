package com.kunion.kcvideo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileUtil {

	
	public static void makeExportImage(int needNum, String filePath) throws Exception {
		List<String> imagePaths = Configure.generateImages(needNum);
		//筛选出几张需要的图片
		
		//蒋筛选出的图片拷贝到临时目录下
		for(int i=0; i<imagePaths.size();i++) {
			
			String dstFileName = String.format(Configure.tmpImageFormat, i+1);
			copyChannel(new File(imagePaths.get(i)), new File(filePath, dstFileName));
			
		}
	}
	
	public static void copyChannel(File f1,File f2) throws Exception{
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        ByteBuffer b=null;
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return ;
            }
            if((inC.size()-inC.position())<length){
                length=(int)(inC.size()-inC.position());
            }else
                length=2097152;
            b=ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }
	
	public static boolean deleteDir(String dir) {
		return deleteDir(new File(dir));
	}
	
	public static boolean pruneDir(String dir) {
		File currentDir = new File(dir);
		if (currentDir.isDirectory()) {
            String[] children = currentDir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return true;
            
        }
		else{
			return false;
		}
	}
	
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
    
    
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
    
	public boolean isCompletelyWritten(File file) {
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
