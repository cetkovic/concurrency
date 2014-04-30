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
		
		new Thread(new InputProcessor(tasks)).start();
		new Thread(new ResultsProcessor(tasks)).start();
		
		log.info("Server started");
	}
	
}

	