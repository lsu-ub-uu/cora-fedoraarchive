/*
 * Copyright 2023 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.fedoraarchive.internal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.InputStream;
import java.text.MessageFormat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.fedora.FedoraNotFoundException;
import se.uu.ub.cora.fedoraarchive.spy.FedoraAdapterSpy;
import se.uu.ub.cora.fedoraarchive.spy.InputStreamSpy;
import se.uu.ub.cora.storage.ResourceConflictException;
import se.uu.ub.cora.storage.ResourceNotFoundException;
import se.uu.ub.cora.storage.archive.ArchiveException;

public class FedoraResourceArchiveTest {

	public static final String RESOURCE_CREATE_CONFLICT_MESSAGE = ""
			+ "Failed to create record due to already existing record id in Fedora Archive for type {0}"
			+ " and id {1}.";
	public static final String RESOURCE_CREATE_ERR_MESSAGE = ""
			+ "Creation of record unsuccessful for type {0} and id {1}.";
	public static final String RESOURCE_READ_MISSING_MESSAGE = ""
			+ "Failed to read record due to it could not be found in Fedora Archive for type {0}"
			+ " and id {1}.";
	public static final String RESOURCE_READ_ERR_MESSAGE = ""
			+ "Reading of record unsuccessful for type {0} and id {1}.";

	private static final String SOME_MESSAGE = "someMessage";
	private static final String SOME_MIME_TYPE = "someMimeType";
	private static final String SOME_DATA_DIVIDER = "someDataDivider";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String ARCHIVE_ID_FORMAT = "{0}:{1}-master";
	FedoraResourceArchive archive;
	FedoraAdapterSpy fedoraAdapter;
	private InputStream stream;
	private String expectedId;

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapter = new FedoraAdapterSpy();
		archive = new FedoraResourceArchive(fedoraAdapter);
		stream = new InputStreamSpy();
		expectedId = MessageFormat.format(ARCHIVE_ID_FORMAT, SOME_TYPE, SOME_ID);
	}

	@Test
	public void testCreate() throws Exception {

		archive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);

		fedoraAdapter.MCR.assertParameters("createResource", 0, SOME_DATA_DIVIDER, expectedId,
				stream, SOME_MIME_TYPE);
	}

	@Test
	public void testCreateConflictException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraConflictException.withMessage(SOME_MESSAGE));

		try {
			archive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ResourceConflictException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_CREATE_CONFLICT_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testCreateFedoraException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_CREATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testCreateAnyOtherException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				new RuntimeException(SOME_MESSAGE));

		try {
			archive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_CREATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testRead() throws Exception {

		InputStream readResource = archive.read(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);

		fedoraAdapter.MCR.assertParameters("readResource", 0, SOME_DATA_DIVIDER, expectedId);
		assertTrue(readResource instanceof InputStream);
	}

	@Test
	public void testReadIdNoTFoundException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				FedoraNotFoundException.withMessage(SOME_MESSAGE));

		try {
			archive.read(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_READ_MISSING_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadIdFedoraException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.read(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_READ_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadAnyOtherException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				new RuntimeException(SOME_MESSAGE));

		try {
			archive.read(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RESOURCE_READ_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test(expectedExceptions = UnsupportedOperationException.class, expectedExceptionsMessageRegExp = "Not implemented yet")
	public void testUpdateUnsupported() throws Exception {

		archive.update(null, null, null, null, null);
	}

	@Test(expectedExceptions = UnsupportedOperationException.class, expectedExceptionsMessageRegExp = "Not implemented yet")
	public void testDeleteUnsupported() throws Exception {

		archive.delete(null, null, null);
	}
}
