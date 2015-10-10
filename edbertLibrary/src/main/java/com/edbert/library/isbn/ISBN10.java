package com.edbert.library.isbn;

public class ISBN10 extends ISBN {

	protected ISBN10(String isbn) {
		super(isbn);
	}

	protected int checkSum() {
		int sum = 0;
		int multiplier = 10;
		for (int i = 0; i < getIsbn().length(); i++) {
			char c = getIsbn().charAt(i);
			int k = 0;
			if (c == 'X' || c == 'x')
				k = 10;
			else
				k = Integer.parseInt(String.valueOf(c));
			k = ((multiplier--) * k);
			sum = (sum + k);
		}

		if (sum % 11 == 0)
			return 0;

		return 11 - (sum % 11);
	}
}
