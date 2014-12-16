package org.woym.spec.logic;

import org.woym.exceptions.DatasetException;

public interface ActivityObject {
	
	public void persist() throws DatasetException;
	
	public void update() throws DatasetException;
	
	public void delete() throws DatasetException;

}
