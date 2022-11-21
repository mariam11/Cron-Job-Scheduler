package task.scheduler;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Level;

import Constants.SchedulerConstants;
import task.scheduler.entities.Job;
import task.scheduler.entities.SchedulerLogger;
import task.scheduler.enums.FrequencyType;   

public class Scheduler {
	// Assumed attributes public for simplicity, on a bigger scale we would keep them private and expose them through getters and setters
	static SchedulerLogger logger = new SchedulerLogger();;
	public static Queue<Job> queue = new PriorityBlockingQueue<Job>();
	public static HashMap<Integer,ExecutorService> threadPools = new HashMap<Integer,ExecutorService>();
	
	public Scheduler() {
	}

	// Assumptions: 
	// - When receiving a job to schedule the first run date will be current time plus the frequency calculated from this moment
	// - jobInterval is assumed to be provided in seconds for simplification
	public static void schedule(int jobId, int jobInterval, int jobFrequencyAmount, FrequencyType jobFrequencyType, Runnable func) {
		
		logger.log(Level.INFO, "Inside the schedule function");
		
		long freqMillis = calculateFrequencyMillis(jobFrequencyAmount,jobFrequencyType);
		//Checking if a job with a similar id was introduced before, otherwise a threadpool dedicated to this job will be created
		if(!(threadPools == null) && !threadPools.containsKey(jobId)) {	
			logger.log(Level.INFO, "No prior thread pool was created for job id: " + jobId + ", creating pool");

			//In case the interval of running the job is greater than the frequency, we will need more than one thread, therefore we will calculate the amount needed	
			int noThreads = calculateThreadPoolsPerJobId(jobInterval, freqMillis);
			
			//Inserting the new threads pool dedicated for this job id, since the number accommodates for the maximum threads 
			//running at the same time, unused threads are returned to the pool
			threadPools.put(jobId,Executors.newFixedThreadPool(noThreads));

			
		}
		logger.log(Level.INFO, "Frequency for job id: " + jobId + ", " + calculateFrequencyMillis(jobFrequencyAmount,jobFrequencyType) + "ms");
		Job newJob = new Job(freqMillis, jobId, func);
		queue.add(newJob);
	}
	
	
	//function to executeJobs, contains one main thread consuming from blocking priority queue that contains any new job id to be executed, 
	//once it calls the separate thread that belongs to the thread pool dedicated for this job id, it would reinsert the job in the priority queue but with
	// the new time which is the current time plus the frequency (amount of time needed from now to the next execution) 
	
	public static void executeJobs() {
		Runnable run = () -> {
			while(true) {
				try {
					logger.log(Level.INFO, "Started Runner");
					Job executed = null;
					long sleepTime = 0; 
					if(!queue.isEmpty()) {
						executed = queue.remove();
						logger.log(Level.INFO, "Executing Job Id " +  executed.getJobId());
						threadPools.get(executed.getJobId()).execute(executed); 
						Job newJob = new Job(executed.getRunFrequency(), executed.getJobId(),executed.getFunc());
						queue.add(newJob);	
						sleepTime = queue.peek().getRunDate()-System.currentTimeMillis();
						
						logger.log(Level.INFO, "Executed next job in common queue, main thread sleeping until next execution time; " + sleepTime);
						Thread.sleep(sleepTime);						
					}

					
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				 
			}
		};
		new Thread(run, "ExecutingThread").start();
	}
	
// function to calculate number of threads needed for one job to be done, 
	public static int calculateThreadPoolsPerJobId(int jobInterval, long freqMillis) {
		return (int) (Math.ceil((jobInterval)*SchedulerConstants.MILLI_PER_SECOND*1.0 /
				freqMillis));
	}
	
// function to transform the interval provided to milliseconds
	public static long calculateFrequencyMillis(int jobFrequencyAmount, FrequencyType jobFrequencyType) {
		long typeToMilliseconds = 0;
		
		switch(jobFrequencyType) {
		case SECONDS: typeToMilliseconds= SchedulerConstants.MILLI_PER_SECOND;break;
		case MINUTES: typeToMilliseconds =SchedulerConstants.MILLI_PER_MINUTE;break;
		case HOURS: typeToMilliseconds = SchedulerConstants.MILLI_PER_HOUR;break;
		case DAYS: typeToMilliseconds = SchedulerConstants.MILLI_PER_DAY;break;
		default:{
			typeToMilliseconds = 0;
			System.out.print("Unsupported Frequency type");
			}
		}	
		return typeToMilliseconds*jobFrequencyAmount;
		
	}
}
		
	

