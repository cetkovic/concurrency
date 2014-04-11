package rs.pstech.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.server.task.MessageResult;

public class MainServer {
	private static final Logger log = LoggerFactory.getLogger(MainServer.class);
	
	public static void main(String[] args) {
		
		
		BlockingQueue<Future<MessageResult>> tasks = new LinkedBlockingQueue<Future<MessageResult>>();
		
		InputProcessor inputProcessor = new InputProcessor(tasks);
		ResultsProcessor resultsProcessor = new ResultsProcessor(tasks);
		new Thread(inputProcessor).start();
		new Thread(resultsProcessor).start();
		
		log.info("Server started");
	}
	
	
	
	
	
	
}

	