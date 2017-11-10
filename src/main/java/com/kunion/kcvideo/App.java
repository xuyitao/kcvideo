package com.kunion.kcvideo;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	TaskHelper helper = new TaskHelper();
    	
    	TaskBean bean = new TaskBean("v2.mp4");
    	
    	helper.addTask(bean);
    }
}
