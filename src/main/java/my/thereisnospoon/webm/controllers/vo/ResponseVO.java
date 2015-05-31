package my.thereisnospoon.webm.controllers.vo;

import java.util.Collection;

public class ResponseVO {

	public final String status, message;
	public final Collection<?> errors;

	public ResponseVO(String status, String message, Collection<?> errors) {
		this.status = status;
		this.message = message;
		this.errors = errors;
	}
}
