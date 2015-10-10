package com.edbert.library.isbn;

/**
 * ISBN.java
 * ISBN class
 * @author nn
 *
 */
public abstract class ISBN {

	private String isbn = null;

	protected ISBN(String isbn) {
		isbn = isbn.trim().replaceAll("-", "");
		this.isbn = isbn;
	}

	/**
	 * Parse the given string and return appropriate ISBN object.
	 * 
	 * @param isbn
	 * @return ISBN object
	 * @throws IllegalArgumentException
	 *             when isbn is not a valid ISBN number.
	 */
	protected static ISBN parse(String isbn) {

		// replace '-' or spaces in string.
		isbn = isbn.trim().replaceAll("-", "").replaceAll(" ", "");

		if (isbn.length() == 13)
			return new ISBN13(isbn);
		if (isbn.length() == 10)
			return new ISBN10(isbn);

		throw new IllegalArgumentException(
				"Not a valid ISBN number (invalid length) : " + isbn);
	}

	public String getIsbn() {
		return isbn;
	}

	/**
	 * Whether an ISBN is valid.
	 * 
	 * @return
	 */
	public static boolean isValid(String isbn) {
		ISBN i = parse(isbn);
		return i.checkSum() == 0;
	}

	public static String toISBN10(String isbn) {

		if (!isValid(isbn))
			throw new IllegalArgumentException("The ISBN number is not valid");

		ISBN i = ISBN.parse(isbn);

		if (i instanceof ISBN10) {
			return i.getIsbn();
		}

		// Convert ISBN13 to ISBN 10.
		return new ISBN10(isbn.substring(3, 12)
				+ ISBN10.parse(isbn.substring(3, 12) + "0").checkSum())
				.getIsbn();

	}

	public static String toISBN13(String isbn) {
		if (!isValid(isbn))
			throw new IllegalArgumentException("The ISBN number is not valid");

		ISBN i = ISBN.parse(isbn);

		if (i instanceof ISBN13) {
			return i.getIsbn();
		}

		int checkSum = ISBN13.parse(ISBN13.SUFFIX + isbn.substring(0, 9) + "0")
				.checkSum();
		String s_checkSum = checkSum < 10 ? String.valueOf(checkSum) : "x";

		// Convert ISBN13 to ISBN 10.
		return new ISBN13(ISBN13.SUFFIX + isbn.substring(0, 9) + s_checkSum)
				.getIsbn();
	}

	/**
	 * Get the checksum digit of the ISBN number
	 */
	abstract protected int checkSum();

	public static void main(String[] a) {

		// Using ISBN.java
		ISBN.isValid("1107666392"); // returns true
		ISBN.isValid("1107666393"); // returns false

		ISBN.isValid("9781107666399"); // returns true
		ISBN.isValid("9781107666398"); // returns false

		ISBN.toISBN13("1107666392"); // returns 9789382618348
		ISBN.toISBN10("9781107666399"); // returns 9382618341

	}

}
