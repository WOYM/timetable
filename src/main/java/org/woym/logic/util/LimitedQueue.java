package org.woym.logic.util;

import java.io.Serializable;
import java.util.LinkedList;

import org.woym.logic.spec.ICommand;
import org.woym.logic.spec.ILimitedQueue;

public class LimitedQueue<E extends ICommand> implements ILimitedQueue<E>, Serializable {

	private static final long serialVersionUID = -660504902790365981L;
	
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

	public void clear() {
		queue.clear();
	}

	@Override
	public E getFirst() {
		if (queue.size() > 0) {
			return queue.pollFirst();
		}
		return null;
	}

	@Override
	public Integer size() {
		return queue.size();
	}

	@Override
	public E getLast() {
		if (queue.size() > 0) {
			return queue.pollLast();
		}
		return null;
	}

}
