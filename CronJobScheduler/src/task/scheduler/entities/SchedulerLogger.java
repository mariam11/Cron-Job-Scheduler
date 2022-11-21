package task.scheduler.entities;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SchedulerLogger {
    static Logger logger;

	
	private static Logger getLogger(){
		try {
			if(logger == null){
				logger = Logger.getLogger("Scheduler Logger");
			    logger.setLevel(Level.FINE);
				logger.addHandler(new ConsoleHandler());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return logger;
	}
	public void log(Level level, String msg){
	    getLogger().log(level, msg);
	    System.out.println(msg);
	}
}
