package com.buaa;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 
* @ProjectName ScheduledExecutorDemo
* @PackageName com.buaa
* @ClassName ScheduledExecutorTest
* @Description ʹ�� ScheduledExecutor�������񲢷�����
* @Author ������
* @Date 2016-02-22 21:00:21
*/
public class ScheduledExecutorTest implements Runnable{
	private String jobName = "";
	public ScheduledExecutorTest(String jobName) {
		super();
		this.jobName = jobName;
	}

	@Override
	public void run() {
		System.out.println("��ʼִ��" + jobName);
		try{
			Thread.sleep(3000);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("����ִ��" + jobName);
	}

	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		
		long initialDelay1 = 1;
		long period1 = 1;
        // �����ڿ�ʼ1����֮��ÿ��1����ִ��һ��
		service.scheduleAtFixedRate(new ScheduledExecutorTest("����1"), initialDelay1,period1,TimeUnit.SECONDS);

		long initialDelay2 = 1;
		long delay2 = 1;
        // �����ڿ�ʼ2����֮��ÿ��2����ִ��һ��
		service.scheduleWithFixedDelay(new ScheduledExecutorTest("����2"), initialDelay2,delay2,TimeUnit.SECONDS);
	}

}
