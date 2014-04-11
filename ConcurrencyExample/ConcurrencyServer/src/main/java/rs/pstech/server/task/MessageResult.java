package rs.pstech.server.task;

import java.net.Socket;

import rs.pstech.common.Message;

public class MessageResult {
	private Message message;
	private Socket socket;
	
	public MessageResult (Message message, Socket socket){
		this.message = message;
		this.socket = socket;
	}
	
	public Message getMessage(){
		return message;
	}
	
	public Socket getSocket(){
		return socket;
	}
}
