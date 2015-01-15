package org.woym.common.objects.spec;

import org.woym.common.exceptions.DatasetException;

public interface IActivityObject {
	
	public void persist() throws DatasetException;
	
	public void update() throws DatasetException;
	
	public void delete() throws DatasetException;

}
