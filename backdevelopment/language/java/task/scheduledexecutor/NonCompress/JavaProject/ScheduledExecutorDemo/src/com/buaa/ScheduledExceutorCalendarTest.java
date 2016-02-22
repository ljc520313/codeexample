package com.buaa;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Lvoy
 * 使用 ScheduledExcetuor和 Calendar进行任务调度
 */
public class ScheduledExceutorCalendarTest extends TimerTask{
	private String jobName = "";

	public ScheduledExceutorCalendarTest(String jobName) {
		super();
		this.jobName = jobName;
	}

	@Override
	public void run() {
		System.out.println("Date = "+new Date()+", execute " + jobName);
	}

	/**
	 * 计算从当前时间currentDate开始，满足条件dayOfWeek, hourOfDay, 
	 * minuteOfHour, secondOfMinite的最近时间
	 * @return
	 */
	public Calendar getEarliestDate(Calendar currentDate, int dayOfWeek,
		int hourOfDay, int minuteOfHour, int secondOfMinite) {
		//计算当前时间的WEEK_OF_YEAR,DAY_OF_WEEK, HOUR_OF_DAY, MINUTE,SECOND等各个字段值
		int currentWeekOfYear = currentDate.get(Calendar.WEEK_OF_YEAR);
		int currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);
		int currentMinute = currentDate.get(Calendar.MINUTE);
		int currentSecond = currentDate.get(Calendar.SECOND);

		//如果输入条件中的dayOfWeek小于当前日期的dayOfWeek,则WEEK_OF_YEAR需要推迟一周
		boolean weekLater = false;
		if (dayOfWeek < currentDayOfWeek) {
			weekLater = true;
		} else if (dayOfWeek == currentDayOfWeek) {
			//当输入条件与当前日期的dayOfWeek相等时，推迟一周推迟一周
			if (hourOfDay < currentHour) {
				//hourOfDay小于当前日期  需要推迟一周	
				weekLater = true;
			} else if (hourOfDay == currentHour) {
                 //当输入条件与当前日期的dayOfWeek, hourOfDay相等时， 需要推迟一周
				if (minuteOfHour < currentMinute) {
					//如果输入条件中的minuteOfHour小于当前日期的
					weekLater = true;
				} else if (minuteOfHour == currentSecond) {
                     //当输入条件与当前日期的dayOfWeek, hourOfDay， 需要推迟一周
					if (secondOfMinite < currentSecond) {
						//secondOfMinite小于当前日期的currentSecond，需要推迟一周
						weekLater = true;
					}
				}
			}
		}
		if (weekLater) {
			//设置当前日期中的WEEK_OF_YEAR为当前周推迟一周
			currentDate.set(Calendar.WEEK_OF_YEAR, currentWeekOfYear + 1);
		}
		// 设置当前日期中DAY_OF_WEEK,HOUR_OF_DAY,MINUTE,SECOND的值
		currentDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
		currentDate.set(Calendar.MINUTE, minuteOfHour);
		currentDate.set(Calendar.SECOND, secondOfMinite);
		return currentDate;

	}

	public static void main(String[] args) throws Exception {
		ScheduledExceutorCalendarTest test = new ScheduledExceutorCalendarTest("zuidaima1");
		// 获取当前时间
		Calendar currentDate = Calendar.getInstance();
		long currentDateLong = currentDate.getTime().getTime();
		
		System.out.println("当前时间  = " + currentDate.getTime().toString());
		// 计算满足条件的最近一次执行时间
		Calendar earliestDate = test.getEarliestDate(currentDate, 5, 17, 20, 10);
		long earliestDateLong = earliestDate.getTime().getTime();
		
		System.out.println("下一次执行时间 = "+ earliestDate.getTime().toString());
		// 计算从当前时间到最近一次执行时间的时间差
		long delay = earliestDateLong - currentDateLong;
		
		// 计算执行周期为一星期
		long period = 7 * 24 * 60 * 60 * 1000;
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		
		//从现在开始delay毫秒之后，每隔一星期执行一次zuidaima1
		service.scheduleAtFixedRate(test, delay, period,TimeUnit.MILLISECONDS);
	}
}
