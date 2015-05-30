package my.thereisnospoon.webm.controllers.vo;

public class ResponseVO {

	public final String status, message;

	public ResponseVO(String status, String message) {
		this.status = status;
		this.message = message;
	}
}
