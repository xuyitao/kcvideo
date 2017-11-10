package com.kunion.kcvideo;

import java.io.File;

public class TaskBean {

	private String inFileName;
	
	private String outFileName;

	private String inFilePath;
	
	private String outFilePath;
	
	private String tmpDir;
	
	//acc
	private String tmpAudioPath;
	
	private String tmpVideoPath;
	
	private String tmpImageFormat;
	
	public TaskBean(String inFileName) {
		// TODO Auto-generated constructor stub
		this.inFileName = inFileName;
		this.inFilePath = Configure.getVideoFullPath(inFileName);
    	
		int separatorIndex = inFileName.lastIndexOf(".");
    	this.outFileName =  inFileName.substring(0, separatorIndex) + ".out"+inFileName.substring(separatorIndex);
    	this.outFilePath = this.inFilePath.replace(inFileName, this.outFileName);
    	
    	this.tmpDir = Configure.getTmpFullPath(inFileName);
    	File tmpDirFile = new File(tmpDir);
    	tmpDirFile.mkdirs();
    	
    	this.tmpAudioPath = this.tmpDir+"/"+inFileName.substring(0, separatorIndex)+".aac";
    	this.tmpVideoPath = this.tmpDir+"/"+this.outFileName;
    	
    	this.tmpImageFormat = this.tmpDir+"/"+Configure.tmpImageFormat;
	}
	
	public String getInFileName() {
		return inFileName;
	}

	public void setInFileName(String inFileName) {
		this.inFileName = inFileName;
		
    
	}

	public String getOutFileName() {
		return outFileName;
	}

	public void setOutFileName(String outFileName) {
		this.outFileName = outFileName;
	}

	public String getInFilePath() {
		return inFilePath;
	}

	public void setInFilePath(String inFilePath) {
		this.inFilePath = inFilePath;
	}

	public String getOutFilePath() {
		return outFilePath;
	}

	public void setOutFilePath(String outFilePath) {
		this.outFilePath = outFilePath;
	}
	
	public boolean isValid() {
		File file = new File(inFilePath);
		return file.exists();
	}

	public String getTmpDir() {
		return tmpDir;
	}

	public void setTmpDir(String tmpDir) {
		this.tmpDir = tmpDir;
	}

	public String getTmpAudioPath() {
		return tmpAudioPath;
	}

	public void setTmpAudioPath(String tmpAudioPath) {
		this.tmpAudioPath = tmpAudioPath;
	}

	public String getTmpVideoPath() {
		return tmpVideoPath;
	}

	public void setTmpVideoPath(String tmpVideoPath) {
		this.tmpVideoPath = tmpVideoPath;
	}

	public String getTmpImageFormat() {
		return tmpImageFormat;
	}

	public void setTmpImageFormat(String tmpImageFormat) {
		this.tmpImageFormat = tmpImageFormat;
	}
	
}
