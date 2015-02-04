package org.woym.controller.planning;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public abstract class ContextMocker extends FacesContext {

	private FacesMessage msg;

	private ContextMocker() {
	}

	private static final Release RELEASE = new Release();

	private static class Release implements Answer<Void> {
		@Override
		public Void answer(InvocationOnMock invocation) throws Throwable {
			setCurrentInstance(null);
			return null;
		}
	}

	public static FacesContext mockFacesContext() {
		FacesContext context = Mockito.mock(ContextMocker.class);
		setCurrentInstance(context);
		Mockito.doAnswer(RELEASE).when(context).release();
		Mockito.doCallRealMethod()
				.when(context)
				.addMessage(Mockito.anyString(),
						Mockito.any(FacesMessage.class));
		Mockito.doCallRealMethod().when(context).getMessageList();
		return context;
	}

	@Override
	public void addMessage(String clientId, FacesMessage message) {
		msg = message;
	}

	@Override
	public List<FacesMessage> getMessageList() {
		List<FacesMessage> list = new ArrayList<FacesMessage>();
		list.add(msg);
		return list;
	}
}
