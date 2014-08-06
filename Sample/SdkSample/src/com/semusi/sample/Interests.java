package com.semusi.sample;

public class Interests {
	private String interest;

	public Interests(String val) {
		interest = val;
	}

	public String getInterest() {
		return interest;
	}

	@Override
	public String toString() {
		return interest;
	}
}
