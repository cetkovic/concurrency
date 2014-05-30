package rs.pstech.server;

import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.server.task.MessageResult;

public class ResultsProcessor implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(ResultsProcessor.class);
	
	private BlockingQueue<Future<MessageResult>> tasks;
	
	public ResultsProcessor (BlockingQueue<Future<MessageResult>> tasks){
		this.tasks = tasks;
	}
	
	public void run() {
		while (true) {
			MessageResult result = null;
			try {
				result = tasks.take().get();
			} catch (InterruptedException | ExecutionException e1) {
				log.error("Error processing results", e1);
			}
			
			try (ObjectOutputStream output = new ObjectOutputStream(result.getSocket().getOutputStream())){
				output.writeObject(result.getMessage());
				log.info("Sent message {} back to client", result.getMessage().getMessageString());
			} catch (Exception e){
				log.error("Error processing results",e);
			}
		}
	}
}