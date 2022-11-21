package test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import task.scheduler.Scheduler;
import task.scheduler.enums.FrequencyType;

public class SchedulerTest {
	
	@Test
	 public void testCalculateFrequencyMillis() {
	    	 assertEquals(172800000,Scheduler.calculateFrequencyMillis(2, FrequencyType.DAYS));
	    	 assertEquals(3600000,Scheduler.calculateFrequencyMillis(1, FrequencyType.HOURS));
	    	 assertEquals(180000,Scheduler.calculateFrequencyMillis(3, FrequencyType.MINUTES));
	    	 assertEquals(1000,Scheduler.calculateFrequencyMillis(1, FrequencyType.SECONDS));      
	    
	}
	
	@Test
	 public void testCalculateThreadPoolsPerJobId() {	
		assertEquals(2,Scheduler.calculateThreadPoolsPerJobId(2,1000));
		assertEquals(1,Scheduler.calculateThreadPoolsPerJobId(1,1000));
		assertEquals(3,Scheduler.calculateThreadPoolsPerJobId(3,1000));
		assertEquals(1,Scheduler.calculateThreadPoolsPerJobId(1,4000));

	}
	
//	@Test
//	 public void testSchedule() {	
//		Scheduler.queue = new PriorityBlockingQueue<Job>();
//		Job j = new Job(1, 2, ()-> {});
//		Scheduler.queue.add(j);
//		Scheduler.executeJobs();
//		Assertions.assertThatCode(() -> toTest.method())
//	    .doesNotThrowAnyException();
//
//	}
}