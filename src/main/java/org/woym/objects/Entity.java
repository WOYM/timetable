package org.woym.objects;

import java.io.Serializable;

import org.woym.exceptions.DatasetException;
import org.woym.persistence.DataAccess;

public abstract class Entity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5127366857697076089L;

	public void persist() throws DatasetException{
		DataAccess.getInstance().persist(this);
	}
	
	public void update() throws DatasetException{
		DataAccess.getInstance().update(this);
	}
	
	public void delete() throws DatasetException{
		DataAccess.getInstance().delete(this);
	}
	
}
