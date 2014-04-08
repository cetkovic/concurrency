package rs.pstech.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.pstech.common.Message;
import rs.pstech.server.task.MessageProcessTask;

public class MainServer {
	private static final Logger log = LoggerFactory.getLogger(MainServer.class);
	
	public static void main(String[] args) {
		ServerSocket listener = null;
		
		BlockingQueue<Future<Message>> messages = new LinkedBlockingQueue<Future<Message>>();
		ExecutorService executor = Executors.newFixedThreadPool(2);
		try {
			log.info("Message server started");
			listener = new ServerSocket(9898);
			while (true){
				Socket socket = listener.accept();
				log.info("Client connected");
				messages.add(executor.submit(new MessageProcessTask(socket)));
				log.info("Submitted message for processing");
			}
		} catch (IOException e) {
			log.error("Error starting server", e);
		} finally {
			try {
				listener.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

	