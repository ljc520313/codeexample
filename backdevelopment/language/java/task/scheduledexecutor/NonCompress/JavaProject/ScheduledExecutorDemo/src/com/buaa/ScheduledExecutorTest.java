package com.buaa;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 
* @ProjectName ScheduledExecutorDemo
* @PackageName com.buaa
* @ClassName ScheduledExecutorTest
* @Description 使用 ScheduledExecutor进行任务并发调度
* @Author 刘吉超
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
		System.out.println("开始执行" + jobName);
		try{
			Thread.sleep(3000);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("结束执行" + jobName);
	}

	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		
		long initialDelay1 = 1;
		long period1 = 1;
        // 从现在开始1秒钟之后，每隔1秒钟执行一次
		service.scheduleAtFixedRate(new ScheduledExecutorTest("测试1"), initialDelay1,period1,TimeUnit.SECONDS);

		long initialDelay2 = 1;
		long delay2 = 1;
        // 从现在开始2秒钟之后，每隔2秒钟执行一次
		service.scheduleWithFixedDelay(new ScheduledExecutorTest("测试2"), initialDelay2,delay2,TimeUnit.SECONDS);
	}

}
