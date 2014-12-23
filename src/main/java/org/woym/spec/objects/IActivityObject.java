package org.woym.spec.objects;

import org.woym.exceptions.DatasetException;

public interface IActivityObject {
	
	public void persist() throws DatasetException;
	
	public void update() throws DatasetException;
	
	public void delete() throws DatasetException;

}
