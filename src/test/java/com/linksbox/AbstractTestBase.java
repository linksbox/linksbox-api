package com.linksbox;

import org.apache.commons.lang3.RandomStringUtils;

public abstract class AbstractTestBase {

	protected String generateString(int length) {
		return RandomStringUtils.randomAlphabetic(length);
	}
}
