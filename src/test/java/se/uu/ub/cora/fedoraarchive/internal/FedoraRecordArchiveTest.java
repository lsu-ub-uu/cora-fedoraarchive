/*
 * Copyright 2022 Uppsala University Library
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

import java.text.MessageFormat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.fedora.FedoraNotFoundException;
import se.uu.ub.cora.fedoraarchive.spy.ExternallyConvertibleToStringConverterSpy;
import se.uu.ub.cora.fedoraarchive.spy.FedoraAdapterSpy;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.RecordNotFoundException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;
import se.uu.ub.cora.testspies.data.DataGroupSpy;

public class FedoraRecordArchiveTest {

	private RecordArchive fedoraArchive;
	private FedoraAdapterSpy fedoraAdapter;
	private DataGroupSpy someDataGroup;
	private ExternallyConvertibleToStringConverterSpy xmlConverter;

	private static final String SOME_DATA_DIVIDER = "someDataDivider";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	private static final String COMBINED_TYPE_AND_ID = "someType:someId";

	public static final String RECORD_CREATE_CONFLICT_MESSAGE = ""
			+ "Failed to create record, record already exists in Fedora Archive for type {0} "
			+ "and id {1}.";
	public static final String RECORD_CREATE_ERR_MESSAGE = ""
			+ "Failed to create record for type {0} and id {1}.";
	public static final String RECORD_UPDATE_MISSING_MESSAGE = ""
			+ "Failed to update record because it was not found in Fedora Archive "
			+ "for type {0} and id {1}.";
	public static final String RECORD_UPDATE_ERR_MESSAGE = ""
			+ "Failed to update record in Fedora Archive for type {0} and id {1}.";
	public static final String RECORD_DELETE_MISSING_MESSAGE = ""
			+ "Failed to delete record because it was not found in Fedora Archive "
			+ "for type {0} and id {1}.";
	public static final String RECORD_DELETE_ERR_MESSAGE = ""
			+ "Failed to delete record in Fedora Archive for type {0} and id {1}.";

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapter = new FedoraAdapterSpy();
		xmlConverter = new ExternallyConvertibleToStringConverterSpy();
		fedoraArchive = new FedoraRecordArchive(xmlConverter, fedoraAdapter);
		someDataGroup = new DataGroupSpy();
	}

	@Test
	public void testCreateRecordAlreadyExists() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createRecord",
				FedoraConflictException.withMessage("From spy, record alreadyExists"));
		try {
			fedoraArchive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof RecordConflictException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_CREATE_CONFLICT_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "From spy, record alreadyExists");
		}
	}

	@Test
	public void testCreateRecordInFedora() throws Exception {
		fedoraArchive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);

		xmlConverter.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverter.MCR.getReturnValue("convert", 0);

		fedoraAdapter.MCR.assertParameters("createRecord", 0, SOME_DATA_DIVIDER,
				COMBINED_TYPE_AND_ID, xml);
	}

	@Test
	public void testCreateHandleOtherExceptions() throws Exception {
		xmlConverter.MRV.setAlwaysThrowException("convert",
				new RuntimeException("Spy exception, error con xml convertion"));
		try {
			fedoraArchive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_CREATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}

	@Test
	public void testUpdateRecordDoesNotExist() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("updateRecord",
				FedoraNotFoundException.withMessage("From spy, record not found"));
		try {
			fedoraArchive.update(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof RecordNotFoundException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_UPDATE_MISSING_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "From spy, record not found");
		}
	}

	@Test
	public void testUpdateRecordInFedora() throws Exception {
		fedoraArchive.update(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);

		xmlConverter.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverter.MCR.getReturnValue("convert", 0);

		fedoraAdapter.MCR.assertParameters("updateRecord", 0, SOME_DATA_DIVIDER,
				COMBINED_TYPE_AND_ID, xml);
	}

	@Test
	public void testUpdateHandleOtherExceptions() throws Exception {
		xmlConverter.MRV.setAlwaysThrowException("convert",
				new RuntimeException("Spy exception, error con xml convertion"));
		try {
			fedoraArchive.update(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_UPDATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}

	@Test
	public void deleteCallsFedoraAdapter() throws Exception {
		fedoraArchive.delete(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);

		fedoraAdapter.MCR.assertParameters("deleteRecord", 0, SOME_DATA_DIVIDER,
				COMBINED_TYPE_AND_ID);
	}

	@Test
	public void deleteRecordNotFound() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("deleteRecord",
				FedoraNotFoundException.withMessage("From spy, record not found"));

		try {
			fedoraArchive.delete(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof RecordNotFoundException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_DELETE_MISSING_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "From spy, record not found");
		}
	}

	@Test
	public void deleteExceptionFromFedoraAdapter() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("deleteRecord",
				new RuntimeException("Spy exception, error con xml convertion"));
		try {
			fedoraArchive.delete(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);
			fail("It should throw Exception");
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_DELETE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}
}
