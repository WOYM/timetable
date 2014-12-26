/**
 * 
 */
package org.woym.logic.command;

import static org.junit.Assert.*;

import javax.swing.text.html.parser.Entity;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * @author JurSch
 *
 */
public class TestAddCommand {
	
	@Mock
	Entity entity;
	
	@InjectMocks
	AddCommand<org.woym.objects.Entity> addCommand;

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
