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
			if (priority > head.priority) {
				Node next = head;
				head = new Node (element, priority);
				head.next = next;
			} else {
				Node current = head;
				
				while (current.next != null && priority <= current.next.priority) {
					current = current.next;
				}
				
				Node next = current.next;
				current.next = new Node (element, priority);
				current.next.next = next;
			}
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
		while (!(current.next == null || current.next.element == element)) {
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

	public void clear() {
		size = 0;
		head = null;
	}

	public int size() {
		return size;
	}
	
	@Override
	public String toString() {
		String result = "--------------------\n" + super.toString();
		Node current = head;
		while (current != null) {
			result += "\n " + current.priority + " " + current.element.toString();
			
			current = current.next;
		}
		
		return result + "\n-----------------";
	}

}
