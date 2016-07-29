package com.ibm.whhack.db;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DBAccessTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUserCreateAndAuthorized() throws SQLException {
		DatabaseAccess.createUser("vravisha", "welcome", true);
		DatabaseAccess.createUser("ssnair", "hello", false);
		Assert.assertTrue(DatabaseAccess.userValid("vravisha", "welcome"));
		Assert.assertFalse(DatabaseAccess.userValid("vravisha", "hello"));
		Assert.assertFalse(DatabaseAccess.userValid("ssnair", "welcome"));
		Assert.assertTrue(DatabaseAccess.userValid("ssnair", "hello"));
		Assert.assertTrue(DatabaseAccess.userAuthorized("vravisha"));
		Assert.assertFalse(DatabaseAccess.userAuthorized("ssnair"));
	}

}
