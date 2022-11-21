package task.scheduler;

import java.util.logging.Level;

import task.scheduler.entities.SchedulerLogger;
import task.scheduler.enums.FrequencyType;


public class EntryPoint {
	
    static SchedulerLogger logger;
	
	public static void main(String []args) {
		setUp();
		logger.log(Level.INFO,"Inside main function, Inserting jobs to be scheduled");
		Runnable func = () ->{};
		Scheduler.schedule(1, 1, 10, FrequencyType.SECONDS, func);
		logger.log(Level.INFO,"Inside main function, Completed Scheduling job with id " + 1);

		Scheduler.schedule(2, 10, 1, FrequencyType.MINUTES, func);
		logger.log(Level.INFO, "Inside main function, Completed Scheduling  job with id " + 2);

		Scheduler.schedule(3, 10, 5, FrequencyType.SECONDS, func);
		logger.log(Level.INFO, "Inside main function, Completed Scheduling  job with id " + 3);

		Scheduler.executeJobs();
	}
	
	
	public static void setUp() {
		// For a larger scale we would place properties in separate files and read them via FileReader
		SchedulerLogger schedulerLogger = new SchedulerLogger();
		logger = schedulerLogger;
	}
	
}
