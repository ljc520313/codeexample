package com.buaa;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Lvoy
 * ʹ�� ScheduledExcetuor�� Calendar�����������
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
	 * ����ӵ�ǰʱ��currentDate��ʼ����������dayOfWeek, hourOfDay, 
	 * minuteOfHour, secondOfMinite�����ʱ��
	 * @return
	 */
	public Calendar getEarliestDate(Calendar currentDate, int dayOfWeek,
		int hourOfDay, int minuteOfHour, int secondOfMinite) {
		//���㵱ǰʱ���WEEK_OF_YEAR,DAY_OF_WEEK, HOUR_OF_DAY, MINUTE,SECOND�ȸ����ֶ�ֵ
		int currentWeekOfYear = currentDate.get(Calendar.WEEK_OF_YEAR);
		int currentDayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK);
		int currentHour = currentDate.get(Calendar.HOUR_OF_DAY);
		int currentMinute = currentDate.get(Calendar.MINUTE);
		int currentSecond = currentDate.get(Calendar.SECOND);

		//������������е�dayOfWeekС�ڵ�ǰ���ڵ�dayOfWeek,��WEEK_OF_YEAR��Ҫ�Ƴ�һ��
		boolean weekLater = false;
		if (dayOfWeek < currentDayOfWeek) {
			weekLater = true;
		} else if (dayOfWeek == currentDayOfWeek) {
			//�����������뵱ǰ���ڵ�dayOfWeek���ʱ���Ƴ�һ���Ƴ�һ��
			if (hourOfDay < currentHour) {
				//hourOfDayС�ڵ�ǰ����  ��Ҫ�Ƴ�һ��	
				weekLater = true;
			} else if (hourOfDay == currentHour) {
                 //�����������뵱ǰ���ڵ�dayOfWeek, hourOfDay���ʱ�� ��Ҫ�Ƴ�һ��
				if (minuteOfHour < currentMinute) {
					//������������е�minuteOfHourС�ڵ�ǰ���ڵ�
					weekLater = true;
				} else if (minuteOfHour == currentSecond) {
                     //�����������뵱ǰ���ڵ�dayOfWeek, hourOfDay�� ��Ҫ�Ƴ�һ��
					if (secondOfMinite < currentSecond) {
						//secondOfMiniteС�ڵ�ǰ���ڵ�currentSecond����Ҫ�Ƴ�һ��
						weekLater = true;
					}
				}
			}
		}
		if (weekLater) {
			//���õ�ǰ�����е�WEEK_OF_YEARΪ��ǰ���Ƴ�һ��
			currentDate.set(Calendar.WEEK_OF_YEAR, currentWeekOfYear + 1);
		}
		// ���õ�ǰ������DAY_OF_WEEK,HOUR_OF_DAY,MINUTE,SECOND��ֵ
		currentDate.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		currentDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
		currentDate.set(Calendar.MINUTE, minuteOfHour);
		currentDate.set(Calendar.SECOND, secondOfMinite);
		return currentDate;

	}

	public static void main(String[] args) throws Exception {
		ScheduledExceutorCalendarTest test = new ScheduledExceutorCalendarTest("zuidaima1");
		// ��ȡ��ǰʱ��
		Calendar currentDate = Calendar.getInstance();
		long currentDateLong = currentDate.getTime().getTime();
		
		System.out.println("��ǰʱ��  = " + currentDate.getTime().toString());
		// �����������������һ��ִ��ʱ��
		Calendar earliestDate = test.getEarliestDate(currentDate, 5, 17, 20, 10);
		long earliestDateLong = earliestDate.getTime().getTime();
		
		System.out.println("��һ��ִ��ʱ�� = "+ earliestDate.getTime().toString());
		// ����ӵ�ǰʱ�䵽���һ��ִ��ʱ���ʱ���
		long delay = earliestDateLong - currentDateLong;
		
		// ����ִ������Ϊһ����
		long period = 7 * 24 * 60 * 60 * 1000;
		
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		
		//�����ڿ�ʼdelay����֮��ÿ��һ����ִ��һ��zuidaima1
		service.scheduleAtFixedRate(test, delay, period,TimeUnit.MILLISECONDS);
	}
}
