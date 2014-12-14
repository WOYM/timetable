package org.woym.logic;

import java.util.LinkedList;

import org.woym.spec.logic.ICommand;
import org.woym.spec.logic.ILimitedQueue;

public class LimitedQueue<E extends ICommand> implements ILimitedQueue<E> {

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
