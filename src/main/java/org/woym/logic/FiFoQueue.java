package org.woym.logic;

import java.util.LinkedList;

import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.IFiFoQueue;

public class FiFoQueue<E extends ICommand> implements IFiFoQueue<E> {

	private final LinkedList<E> queue = new LinkedList<>();

	@Override
	public void add(E element) {
		if (queue.size() < LIST_SIZE) {
			queue.add(element);
		} else {
			queue.remove();
			queue.add(element);
		}

	}

	@Override
	public E get() {
		if (queue.size() > 0) {
			return queue.pollFirst();
		}
		return null;
	}

	@Override
	public Integer size() {
		return queue.size();
	}

}
