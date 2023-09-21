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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
	private FedoraAdapterSpy fedoraAdapterSpy;
	private DataGroupSpy someDataGroup;
	private ExternallyConvertibleToStringConverterSpy xmlConverterSpy;

	private static final String SOME_DATA_DIVIDER = "someDataDivider";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";

	public static final String RECORD_CREATE_CONFLICT_MESSAGE = ""
			+ "Failed to create record due to already existing record id in Fedora Archive for type {0} "
			+ "and id {1}.";
	public static final String RECORD_CREATE_ERR_MESSAGE = ""
			+ "Creation of record unsuccessful for type {0} and id {1}.";
	public static final String RECORD_UPDATE_MISSING_MESSAGE = ""
			+ "Update of record could not be executed since it was not located in Fedora Archive "
			+ "for type {0} and id {1}.";
	public static final String RECORD_UPDATE_ERR_MESSAGE = ""
			+ "Unable to update record in Fedora Archive for type {0} and id {1}.";

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapterSpy = new FedoraAdapterSpy();
		xmlConverterSpy = new ExternallyConvertibleToStringConverterSpy();
		fedoraArchive = new FedoraRecordArchive(xmlConverterSpy, fedoraAdapterSpy);
		someDataGroup = new DataGroupSpy();
	}

	@Test
	public void testCreateRecordAlreadyExists() throws Exception {
		fedoraAdapterSpy.MRV.setAlwaysThrowException("createRecord",
				FedoraConflictException.withMessage("From spy, record alreadyExists"));
		try {
			fedoraArchive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			assertFalse(true);
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

		xmlConverterSpy.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverterSpy.MCR.getReturnValue("convert", 0);

		fedoraAdapterSpy.MCR.assertParameters("createRecord", 0, SOME_DATA_DIVIDER,
				"someType:someId", xml);
	}

	@Test
	public void testCreateHandleOtherExceptions() throws Exception {
		xmlConverterSpy.MRV.setAlwaysThrowException("convert",
				new RuntimeException("Spy exception, error con xml convertion"));
		try {
			fedoraArchive.create(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_CREATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}

	@Test
	public void testUpdateRecordDoesNotExist() throws Exception {
		fedoraAdapterSpy.MRV.setAlwaysThrowException("updateRecord",
				FedoraNotFoundException.withMessage("From spy, record not found"));
		try {
			fedoraArchive.update(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			assertFalse(true);
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

		xmlConverterSpy.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverterSpy.MCR.getReturnValue("convert", 0);

		fedoraAdapterSpy.MCR.assertParameters("updateRecord", 0, SOME_DATA_DIVIDER,
				"someType:someId", xml);
	}

	@Test
	public void testUpdateHandleOtherExceptions() throws Exception {
		xmlConverterSpy.MRV.setAlwaysThrowException("convert",
				new RuntimeException("Spy exception, error con xml convertion"));
		try {
			fedoraArchive.update(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID, someDataGroup);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					MessageFormat.format(RECORD_UPDATE_ERR_MESSAGE, SOME_TYPE, SOME_ID));
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}
}
