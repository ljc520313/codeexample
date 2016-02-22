package com.buaa;

import java.util.Timer;
import java.util.TimerTask;

/** 
* @ProjectName TimerDemo
* @PackageName com.buaa
* @ClassName TimerDemo
* @Description 使用Timer进行任务调度
* @Author 刘吉超
* @Date 2016-02-19 10:22:11
*/
public class TimerDemo extends TimerTask{
	 private String jobName = "";
	 
	 public TimerDemo(String jobName) { 
		 super(); 
		 this.jobName = jobName; 
	 }
	 
	 @Override 
	 public void run() {
		 System.out.println("执行：" + jobName); 
	 }
	 
	 public static void main(String[] args) { 
		 Timer timer = new Timer(); 
		 
		 long delay1 = 1 * 1000; 
		 long period1 = 1000; 
		 // 从现在开始 1 秒钟之后，每隔 1 秒钟执行一次
		 timer.schedule(new TimerDemo("测试1"), delay1, period1); 
		 
		 long delay2 = 2 * 1000; 
		 long period2 = 2000; 
		 // 从现在开始 2 秒钟之后，每隔 2 秒钟执行一次
		 timer.schedule(new TimerDemo("测试2"), delay2, period2); 
	 } 
}