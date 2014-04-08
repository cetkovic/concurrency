package rs.pstech.common;

import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 8302211787737721865L;
	
	private String messageString;
	private long processingTime;
	
	public Message (String messageString){
		this.setMessageString(messageString);
	}

	public String getMessageString() {
		return messageString;
	}

	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(long processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public String toString() {
		return "Message [messageString=" + messageString + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((messageString == null) ? 0 : messageString.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (messageString == null) {
			if (other.messageString != null)
				return false;
		} else if (!messageString.equals(other.messageString))
			return false;
		return true;
	}
	
}
