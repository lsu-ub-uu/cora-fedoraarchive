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
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

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
import se.uu.ub.cora.storage.archive.ResourceMetadata;
import se.uu.ub.cora.storage.archive.record.ResourceMetadataToUpdate;

public class FedoraResourceArchiveTest {

	public static final String ERR_MSG_CREATE_CONFLICT = ""
			+ "Failed to create resource due to already existing record id in Fedora Archive for type {0}"
			+ " and id {1}.";
	public static final String ERR_MSG_EXCEPTION = ""
			+ "{2} of resource unsuccessful for type {0} and id {1}.";
	public static final String ERR_MSG_NOT_FOUND_FEDORA_ADAPTER = ""
			+ "Failed to {2} resource due to it could not be found in Fedora Archive for type {0}"
			+ " and id {1}.";

	private static final String SOME_MESSAGE = "someMessage";
	private static final String SOME_MIME_TYPE = "someMimeType";
	private static final String SOME_DATA_DIVIDER = "someDataDivider";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String ARCHIVE_ID_FORMAT = "{0}:{1}-master";
	FedoraResourceArchive archive;
	FedoraAdapterSpy fedoraAdapter;
	private InputStream stream;
	private String ensembledId;

	private ResourceMetadataToUpdate resourceMetadataStorage;

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapter = new FedoraAdapterSpy();
		archive = new FedoraResourceArchive(fedoraAdapter);
		stream = new InputStreamSpy();
		ensembledId = MessageFormat.format(ARCHIVE_ID_FORMAT, SOME_TYPE, SOME_ID);
		resourceMetadataStorage = new ResourceMetadataToUpdate("someFileName",
				"someDetectedMimeType");
	}

	@Test
	public void testCreate() throws Exception {

		archive.createMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);

		fedoraAdapter.MCR.assertParameters("createResource", 0, SOME_DATA_DIVIDER, ensembledId,
				stream, SOME_MIME_TYPE);
	}

	@Test
	public void testCreateConflictException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraConflictException.withMessage(SOME_MESSAGE));

		try {
			archive.createMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ResourceConflictException);
			assertEquals(e.getMessage(),
					MessageFormat.format(ERR_MSG_CREATE_CONFLICT, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testCreateFedoraException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.createMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID, "Creation"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testCreateAnyOtherException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				new RuntimeException(SOME_MESSAGE));

		try {
			archive.createMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID, "Creation"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testRead() throws Exception {

		InputStream readResource = archive.readMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);

		fedoraAdapter.MCR.assertParameters("readResource", 0, SOME_DATA_DIVIDER, ensembledId);
		assertTrue(readResource instanceof InputStream);
	}

	@Test
	public void testReadIdNoTFoundException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				FedoraNotFoundException.withMessage(SOME_MESSAGE));

		try {
			archive.readMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals(e.getMessage(), MessageFormat.format(ERR_MSG_NOT_FOUND_FEDORA_ADAPTER,
					SOME_TYPE, SOME_ID, "read"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadIdFedoraException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.readMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID, "Reading"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadAnyOtherException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResource",
				new RuntimeException(SOME_MESSAGE));

		try {
			archive.readMasterResource(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID, "Reading"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadMetadata() throws Exception {

		ResourceMetadata resourceMetadataStorage = archive.readMasterResourceMetadata(SOME_DATA_DIVIDER,
				SOME_TYPE, SOME_ID);

		fedoraAdapter.MCR.assertParameters("readResourceMetadata", 0, SOME_DATA_DIVIDER,
				ensembledId);

		se.uu.ub.cora.fedora.record.ResourceMetadata resourceMetadataFedora = (se.uu.ub.cora.fedora.record.ResourceMetadata) fedoraAdapter.MCR
				.getReturnValue("readResourceMetadata", 0);
		assertEquals(resourceMetadataStorage.fileSize(), resourceMetadataFedora.fileSize());
		assertEquals(resourceMetadataStorage.checksumSHA512(),
				resourceMetadataFedora.checksumSHA512());

	}

	@Test
	public void testReadMeatadataNotFoundException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResourceMetadata",
				FedoraNotFoundException.withMessage(SOME_MESSAGE));

		try {
			archive.readMasterResourceMetadata(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals(e.getMessage(), MessageFormat.format(ERR_MSG_NOT_FOUND_FEDORA_ADAPTER,
					SOME_TYPE, SOME_ID, "read metadata for"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testReadMeatadataExceptionInFedora() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("readResourceMetadata",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.readMasterResourceMetadata(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(), MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID,
					"Reading metadata"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testUpdateMetadata() throws Exception {

		archive.updateMasterResourceMetadata(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, resourceMetadataStorage);

		fedoraAdapter.MCR.assertParameters("updateResourceMetadata", 0, SOME_DATA_DIVIDER,
				ensembledId);

		se.uu.ub.cora.fedora.record.ResourceMetadataToUpdate resourceMetadataFedora = (se.uu.ub.cora.fedora.record.ResourceMetadataToUpdate) fedoraAdapter.MCR
				.getValueForMethodNameAndCallNumberAndParameterName("updateResourceMetadata", 0,
						"resourceMetadataToUpdate");

		assertEquals(resourceMetadataFedora.originalFileName(),
				resourceMetadataStorage.originalFileName());
		assertEquals(resourceMetadataFedora.mimeType(), resourceMetadataStorage.mimeType());
	}

	@Test
	public void testUpdateMetadataResourceNotFound() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("updateResourceMetadata",
				FedoraNotFoundException.withMessage(SOME_MESSAGE));

		try {
			archive.updateMasterResourceMetadata(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, resourceMetadataStorage);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ResourceNotFoundException);
			assertEquals(e.getMessage(), MessageFormat.format(ERR_MSG_NOT_FOUND_FEDORA_ADAPTER,
					SOME_TYPE, SOME_ID, "update metadata for"));
			assertEquals(e.getCause().getMessage(), SOME_MESSAGE);
		}
	}

	@Test
	public void testUpdateMetadataFedoraException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("updateResourceMetadata",
				FedoraException.withMessage(SOME_MESSAGE));

		try {
			archive.updateMasterResourceMetadata(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, resourceMetadataStorage);
			fail("Failed");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(), MessageFormat.format(ERR_MSG_EXCEPTION, SOME_TYPE, SOME_ID,
					"Updating metadata"));
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
