package com.buaa;

import java.util.Timer;
import java.util.TimerTask;

/** 
* @ProjectName TimerDemo
* @PackageName com.buaa
* @ClassName TimerDemo
* @Description ʹ��Timer�����������
* @Author ������
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
		 System.out.println("ִ�У�" + jobName); 
	 }
	 
	 public static void main(String[] args) { 
		 Timer timer = new Timer(); 
		 
		 long delay1 = 1 * 1000; 
		 long period1 = 1000; 
		 // �����ڿ�ʼ 1 ����֮��ÿ�� 1 ����ִ��һ��
		 timer.schedule(new TimerDemo("����1"), delay1, period1); 
		 
		 long delay2 = 2 * 1000; 
		 long period2 = 2000; 
		 // �����ڿ�ʼ 2 ����֮��ÿ�� 2 ����ִ��һ��
		 timer.schedule(new TimerDemo("����2"), delay2, period2); 
	 } 
}