package my.thereisnospoon.webm.controllers;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class HomeControllerTest {

	@Test
	public void testRangeSplit() {

		String range = "0-";
		assertEquals("0", range.split("\\D+")[0]);

		range = "0-123";
		assertEquals("123", range.split("\\D+")[1]);

		range = "bytes=0-7307904";
		assertEquals("7307904", range.split("\\D+")[2]);
	}
}