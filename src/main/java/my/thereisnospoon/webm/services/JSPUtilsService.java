package my.thereisnospoon.webm.services;

import org.springframework.stereotype.Service;

@Service("jspUtils")
public class JSPUtilsService {

	public int getRowsCount(int rowSize, int total) {

		int result = total / rowSize;
		if (total % rowSize > 0) {
			result++;
		}
		return result;
	}
}