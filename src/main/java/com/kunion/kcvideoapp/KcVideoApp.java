
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
/**
 * This example, like all Swing examples, exists in a package:
 * in this case, the "start" package.
 * If you are using an IDE, such as NetBeans, this should work 
 * seamlessly.  If you are compiling and running the examples
 * from the command-line, this may be confusing if you aren't
 * used to using named packages.  In most cases,
 * the quick and dirty solution is to delete or comment out
 * the "package" line from all the source files and the code
 * should work as expected.  For an explanation of how to
 * use the Swing examples as-is from the command line, see
 * http://docs.oracle.com/javase/javatutorials/tutorial/uiswing/start/compile.html#package
 */
package com.kunion.kcvideoapp;
 
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/*
 * HelloWorldSwing.java requires no other files. 
 */
import javax.swing.*;

import com.kunion.kcvideo.Configure;
import com.kunion.kcvideo.LogUtil;
import com.kunion.kcvideo.TaskBean;
import com.kunion.kcvideo.TaskHelper;
import com.kunion.kcvideo.VideoManager;        
 
public class KcVideoApp implements ActionListener {
	
	public class mainWorker  extends SwingWorker<Integer, Integer> {
		public volatile boolean keepRunning = true;
		protected Integer doInBackground() throws Exception {
			System.out.println("MyRunnable running");
			// go do actions
			
			TaskHelper helper = new TaskHelper();
			
			while(keepRunning) {

				log("搜索未完成的视频");
				List<String> newVideos = videoManager.getUnHandlerVideo();
				
				LogUtil.log("增加新视频  " + newVideos.size() + "  " + newVideos.toString());
			
				log("增加新视频  " + newVideos.size() + "  " + newVideos.toString());
				
				for(String videoFileName : newVideos) {
					
					log(" 处理视频 " + videoFileName);

					TaskBean bean = new TaskBean(videoFileName);
					helper.addTask(bean);
					videoManager.handlerVideo(bean);
				}

				try {
					helper.poolLog();
					log("休息3秒");
					Thread.sleep(3000);
				} catch (InterruptedException ea) {
					// TODO Auto-generated catch block
					ea.printStackTrace();
				}
	    	}
			
			return 0;
	    }
	}
	
	public static VideoManager videoManager = new VideoManager();
	
	public boolean runningTask = false;
	
	JFrame frame;
	
	JPanel folderPanel;
	JButton srcVideoFolderSelectBtn;
	JButton srcImageFolderSelectBtn;
	JButton outputFolderSelectBtn;
	
	JFileChooser chooser;
	
	JPanel actionPanel;
	JButton runBtn;
	JButton stopBtn;
	
	JPanel infoPanel;
	JTextArea logCmp;
	
	
	// datas
	private String sourceVideoFolder = ".";
	private String sourceImageFolder = ".";
	private String outputFolder = ".";
	
	Thread mainJobThread;
	mainWorker mainLoop;
   
	public KcVideoApp() {
        //Create and set up the window.
		
        frame = new JFrame("KCVideo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        folderPanel = new JPanel(new FlowLayout());
     
        srcVideoFolderSelectBtn = new JButton("视频源目录");
        srcVideoFolderSelectBtn.addActionListener(this);
        srcVideoFolderSelectBtn.setName("svf");
	    folderPanel.add(srcVideoFolderSelectBtn);
	    
	    srcImageFolderSelectBtn = new JButton("图片源目录");
	    srcImageFolderSelectBtn.addActionListener(this);
	    srcImageFolderSelectBtn.setName("sif");
	    folderPanel.add(srcImageFolderSelectBtn);
	    
	    outputFolderSelectBtn = new JButton("输出目录");
	    outputFolderSelectBtn.addActionListener(this);
	    outputFolderSelectBtn.setName("of");
	    folderPanel.add(outputFolderSelectBtn);
	    
        frame.getContentPane().add(folderPanel, BorderLayout.PAGE_START);
	    
        actionPanel = new JPanel(new FlowLayout());
        
        srcVideoFolderSelectBtn = new JButton("启动");
        srcVideoFolderSelectBtn.addActionListener(this);
        srcVideoFolderSelectBtn.setName("act_start");
        actionPanel.add(srcVideoFolderSelectBtn);
	    
	    srcImageFolderSelectBtn = new JButton("停止");
	    srcImageFolderSelectBtn.addActionListener(this);
	    srcImageFolderSelectBtn.setName("act_end");
	    actionPanel.add(srcImageFolderSelectBtn);
	    
        frame.getContentPane().add(actionPanel, BorderLayout.CENTER);
        
        infoPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        logCmp = new JTextArea();
        logCmp.setPreferredSize(new Dimension(600, 400));
        logCmp.setEditable(false);

        logCmp.insert("Welcome to kc video tools \n" , 0);
        logCmp.insert("This tool is used to filter audio from original video, then generate video with images & audio \n", 0);
        logCmp.insert("请先选择视频目录，图片目录和输出目录 \n", 0);

        infoPanel.add(logCmp);
        		
        frame.getContentPane().add(infoPanel, BorderLayout.PAGE_END);
 
   
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        
        LogUtil.globalContext = this;
	}
	
	public void actionPerformed(ActionEvent e) {    
		
		JButton targetBtn = (JButton) e.getSource();
		String btnname = targetBtn.getName();
		
		System.out.println("Click btn " + btnname);

		if (btnname.endsWith("f")){
			// go folder chooser
			
			chooser = new JFileChooser(); 
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setDialogTitle("pick");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			chooser.setAcceptAllFileFilterUsed(false);
			//    
			if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
				System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
				
				String dstFolder = setFolderSelection(btnname, chooser.getSelectedFile().getPath());
				logCmp.insert("成功选择 " + dstFolder +" 目录为 " + chooser.getSelectedFile().getPath() +" \n", 0);
			}
			else {
				System.out.println("No Selection");
			}
			
		}
		else if(btnname.startsWith("act")){
			if (btnname.equalsIgnoreCase("act_start")){
				runningTask = true;
			}
			else if (btnname.equalsIgnoreCase("act_end")){
				runningTask = false;
			}
			
			if (runningTask){
				stopMainLoop();
				startMainLoop();
			}
			else{
				stopMainLoop();
			}			
		}
	}
	
	private void stopMainLoop() {
		if (mainLoop != null){
			log("中断正在运行的任务");
			mainLoop.keepRunning = false;
			log("等待中断完成");
			mainLoop.cancel(true);
			log("中断成功");
			
			mainLoop = null;
			mainJobThread = null;
		}
	}
	
	private void startMainLoop() {
		log("开始任务");
		mainLoop = new mainWorker();
		mainLoop.execute();
	}
	
	private  String setFolderSelection (String btnname, String folderPath) {
		if(btnname.equalsIgnoreCase("svf")){
			sourceVideoFolder = folderPath;
			Configure.videoFolderPath = folderPath;
			return "视频源目录";
		}
		else if(btnname.equalsIgnoreCase("sif")){
			sourceImageFolder = folderPath;
			Configure.imageFolderPath = folderPath;
			return "图片源目录";
		}
		else if(btnname.equalsIgnoreCase("of")){
			outputFolder = folderPath;
			Configure.outputFolderPath = folderPath;
			return "输出目录";
		}
		
		return "未知目录";
	}
	
	
	public void log ( String log){
		logCmp.insert(log +" \n" , 0);
	}

 
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
 
                KcVideoApp app = new KcVideoApp();
                
            }
        });
    }
}