package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ComputerAITests {

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSelectTarget() {
		/*
		if no rooms in list, select randomly
		if room in list that has not been seen, select it
		if room in list that has been seen, each target (including room) selected randomly
		*/
	}
	@Test
	void testMakeASuggestion() {
		/*
		Room matches current location
		If only one weapon not seen, it's selected
		If only one person not seen, it's selected (can be same test as weapon)
		If multiple weapons not seen, one of them is randomly selected
		If multiple persons not seen, one of them is randomly selected
		*/
	}

}
