package com.buaa;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.helpers.TriggerUtils;

/** 
* @ProjectName QuartzDemo
* @PackageName com.buaa
* @ClassName QuartzTest
* @Description ʹ�� Quartz �����������
* @Author ������
* @Date 2016-02-22 21:59:49
*/
public class QuartzTest implements Job{
	@Override
	//�÷���ʵ����Ҫִ�е�����
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Generating report - "+ arg0.getJobDetail().getFullName() + ", type ="
				+ arg0.getJobDetail().getJobDataMap().get("type"));
		System.out.println(new Date().toString());
	}
	public static void main(String[] args) {
		try {
			// ����һ��Scheduler
			SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();
			Scheduler sched = schedFact.getScheduler();
			sched.start();
			// ����һ��JobDetail��ָ��name��groupname���Լ������Job������
			//��Job��������Ҫִ������
			JobDetail jobDetail = new JobDetail("myJob", "myJobGroup",
					QuartzTest.class);
			jobDetail.getJobDataMap().put("type", "FULL");
            // ����һ��ÿ�ܴ�����Trigger��ָ�����ڼ����㼸��ִ��
			Trigger trigger = TriggerUtils.makeWeeklyTrigger(5,17,42);
			trigger.setGroup("myTriggerGroup");
			// �ӵ�ǰʱ�����һ�뿪ʼִ��
			trigger.setStartTime(TriggerUtils.getEvenSecondDate(new Date()));
			// ָ��trigger��name
			trigger.setName("myTrigger");
			// ��scheduler��JobDetail��Trigger������һ�𣬿�ʼ��������
			sched.scheduleJob(jobDetail, trigger);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
