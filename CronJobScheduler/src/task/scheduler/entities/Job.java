package task.scheduler.entities;

import java.util.logging.Level;

public class Job implements Runnable,  Comparable<Job> {
static SchedulerLogger logger = new SchedulerLogger();;
private int jobId; 
private long runFrequency;
private long runDate;
private Runnable func;

public Job(long runFrequency, int jobId, Runnable func) {
	this.runFrequency = runFrequency;
	this.jobId = jobId;
	this.runDate = System.currentTimeMillis()+this.runFrequency;
	this.func = func;	
}


	@Override
	public void run() {
		try {
			logger.log(Level.INFO, "Running the Executing function for Job: " + jobId );
			this.func.run();
		} catch (Exception e) {
			logger.log(Level.SEVERE,  e.getMessage());
		}
		
	}
	@Override
	public int compareTo(Job o) {
		return Long.compare(this.runDate, o.runDate);
	}

	public int getJobId() {
		return jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	public long getRunFrequency() {
		return runFrequency;
	}

	public void setRunFrequency(long runFrequency) {
		this.runFrequency = runFrequency;
	}
	
	public long getRunDate() {
		return runDate;
	}
	
	public void setRunDate(long runDate) {
		this.runDate = runDate;
	}
	
	public Runnable getFunc() {
		return func;
	}

	public void setFunc(Runnable func) {
		this.func = func;
	}

}
