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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.fedoraarchive.spy.DataGroupSpy;
import se.uu.ub.cora.fedoraarchive.spy.ExternallyConvertibleToStringConverterSpy;
import se.uu.ub.cora.fedoraarchive.spy.FedoraAdapterSpy;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchiveTest {

	private FedoraAdapterSpy fedoraAdapterSpy;
	private RecordArchive fedoraArchive;
	private DataGroup someDataGroup;
	private ExternallyConvertibleToStringConverterSpy xmlConverterSpy;

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapterSpy = new FedoraAdapterSpy();
		xmlConverterSpy = new ExternallyConvertibleToStringConverterSpy();
		fedoraArchive = new FedoraRecordArchive(xmlConverterSpy, fedoraAdapterSpy);
		someDataGroup = new DataGroupSpy();
	}

	@Test
	public void testinit() throws Exception {
		fedoraArchive.create("someType", "someId", someDataGroup);
	}

	@Test
	public void testRecordAlreadyExists() throws Exception {
		fedoraAdapterSpy.throwExceptionOnCreateRecordAlreadyExists = true;
		try {
			fedoraArchive.create("someType", "someId", someDataGroup);
			assertFalse(true);
		} catch (Exception e) {
			assertTrue(e instanceof RecordConflictException);
			assertEquals(e.getMessage(),
					"Record could not be created in Fedora Archive for type: someType and id: someId");
			assertEquals(e.getCause().getMessage(), "From spy, record alreadyExists");
		}
	}

	@Test
	public void testCreateRecordInFedora() throws Exception {
		fedoraArchive.create("someType", "someId", someDataGroup);

		xmlConverterSpy.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverterSpy.MCR.getReturnValue("convert", 0);

		fedoraAdapterSpy.MCR.assertParameters("create", 0, "someType:someId", xml);
	}

	@Test
	public void testHandleOtherExceptionsOnCreate() throws Exception {
		xmlConverterSpy.throwExceptionOnConvert = true;

		try {
			fedoraArchive.create("someType", "someId", someDataGroup);
		} catch (Exception e) {
			assertTrue(e instanceof ArchiveException);
			assertEquals(e.getMessage(),
					"Record could not be created in Fedora Archive for type: someType and id: someId");
			assertEquals(e.getCause().getMessage(), "Spy exception, error con xml convertion");
		}
	}
}
