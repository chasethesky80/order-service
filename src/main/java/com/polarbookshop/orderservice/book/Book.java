package com.polarbookshop.orderservice.book;

import java.io.Serializable;

public record Book(
	String isbn,
	String title,
	String author,
	Double price
) implements Serializable {}
