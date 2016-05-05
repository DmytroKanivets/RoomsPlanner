package com.coursework.rules;

public class PriorityRule extends Rule {
	
	private int priority;
	
	public PriorityRule(int p) {
		priority = p;
	}
	
	public void setPriority(int p) {
		priority = p;
	}
	
	public int getPriority() {
		return priority;
	}
}
