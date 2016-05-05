package com.coursework.util;

import java.util.Iterator;

public class PriorityQueue<T> implements Iterable<T> {

	private class Node {
		T element;
		int priority; 
		
		public Node(T element, int priority) {
			this.element = element;
			this.priority = priority;
		}
 		
		Node next;
	}
	
	Node head;
	int size = 0;
	
	public PriorityQueue() {}
	
	public void add(T element, int priority) {
		if (head == null) {
			head = new Node(element, priority);
		} else  {
			Node current = head;
			
			while (current.next != null && priority < current.priority) {
				current = current.next;
			}
			
			Node next = current.next;
			current.next = new Node (element, priority);
			current.next.next = next;
		}
		
		size++;
	}
	
	public void remove(T element) {
		if (head == null)
			return;
		
		if (head.element == element) {
			head = head.next;
			size--;
			return;
		}
		
		Node current = head;
		while (!(current.next == null || current.next == element)) {
			current = current.next;
		}
		
		if (current.next != null) {
			size--;
			current.next = current.next.next;
		}
	}
	
	private class MyIterator implements Iterator<T> {

		Node current;
		
		public MyIterator() {
			current = head;
		}
		
		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T element = current.element;
			current = current.next;
			return element;
		}}
	
	@Override
	public Iterator<T> iterator() {
		return new MyIterator();
	}

}
