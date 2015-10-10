package com.edbert.library.isbn;

public class ISBN13 extends ISBN {

	public static final String SUFFIX = "978";

	protected ISBN13(String isbn) {
		super(isbn);
	}

	@Override
	protected int checkSum() {

		int sum = 0;
		for (int i = 0; i < getIsbn().length(); i++) {
			int k = Integer.parseInt(String.valueOf(getIsbn().charAt(i)));
			int c = (k * ((i % 2 == 1) ? 3 : 1));
			sum += c;
		}

		if (sum % 10 == 0)
			return 0;

		return 10 - (sum % 10);
	}

}
