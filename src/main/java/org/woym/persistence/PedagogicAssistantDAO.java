package org.woym.persistence;

import org.woym.objects.PedagogicAssistant;

public class PedagogicAssistantDAO extends AbstractEmployeeDAO<PedagogicAssistant> {

	private static final PedagogicAssistantDAO INSTANCE = new PedagogicAssistantDAO();
	
	private PedagogicAssistantDAO(){
		DataBase.getInstance().addObserver(this);
		setClazz(PedagogicAssistant.class);
	}
	
	public static PedagogicAssistantDAO getInstance(){
		return INSTANCE;
	}
}
