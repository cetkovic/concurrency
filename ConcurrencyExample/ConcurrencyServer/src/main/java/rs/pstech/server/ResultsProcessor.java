package rs.pstech.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
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
			ObjectOutputStream output = null;
			try{
				MessageResult result = tasks.take().get();
				output = new ObjectOutputStream(result.getSocket().getOutputStream());
				output.writeObject(result.getMessage());
			} catch (Exception e){
				log.error("Error processing results",e);
			} finally {
				try {
					output.close();
				} catch (IOException e) {
					log.error("Error closing socket!",e);
				}
			}
		}
	}
}