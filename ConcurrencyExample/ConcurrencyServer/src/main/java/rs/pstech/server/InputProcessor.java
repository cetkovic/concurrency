package rs.pstech.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.server.task.MessageProcessTask;
import rs.pstech.server.task.MessageResult;

public class InputProcessor implements Runnable {
		private static final Logger log = LoggerFactory.getLogger(InputProcessor.class);
		private static final int SERVER_LISTENER_PORT = 9898;
		
		private BlockingQueue<Future<MessageResult>> messages;

		private ServerSocket listener;
		private ExecutorService executor = Executors.newFixedThreadPool(2);
		
		public InputProcessor (BlockingQueue<Future<MessageResult>> messages){
			this.messages = messages;
		}

		public void run (){
			try {
				listener = new ServerSocket(SERVER_LISTENER_PORT);
			
				while (true){
					Socket socket = listener.accept();
					messages.add(executor.submit(new MessageProcessTask(socket)));
					log.debug("Submitted message for processing");
				}
			} catch (IOException e) {
				log.error("Server error while listening for messages!");
			} finally {
				try {
					listener.close();
				} catch (IOException e) {
					log.error("Could not close Server socket listener");
				}
			}
		}
	}
