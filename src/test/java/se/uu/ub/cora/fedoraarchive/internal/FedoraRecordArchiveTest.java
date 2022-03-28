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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.fedoraarchive.spy.DataGroupSpy;
import se.uu.ub.cora.fedoraarchive.spy.ExternallyConvertibleToStringConverterSpy;
import se.uu.ub.cora.fedoraarchive.spy.FedoraWrapperSpy;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchiveTest {

	private FedoraWrapperSpy fedoraWrapperSpy;
	private RecordArchive fedoraArchive;
	private DataGroup someDataGroup;
	private ExternallyConvertibleToStringConverterSpy xmlConverterSpy;

	@BeforeMethod
	public void beforeMethod() {
		fedoraWrapperSpy = new FedoraWrapperSpy();
		xmlConverterSpy = new ExternallyConvertibleToStringConverterSpy();
		fedoraArchive = new FedoraRecordArchive(xmlConverterSpy, fedoraWrapperSpy);
		someDataGroup = new DataGroupSpy();
	}

	@Test
	public void testinit() throws Exception {
		fedoraArchive.create("somType", "someId", someDataGroup);

	}

	@Test(expectedExceptions = RecordConflictException.class, expectedExceptionsMessageRegExp = ""
			+ "Record could not be created in Fedora Archive")
	public void testRecordAlreadyExists() throws Exception {
		fedoraWrapperSpy.throwExceptionOnCreateRecordAlreadyExists = true;
		fedoraArchive.create("somType", "someId", someDataGroup);

	}

	@Test
	public void testCreateRecordInFedora() throws Exception {
		fedoraArchive.create("somType", "someId", someDataGroup);

		xmlConverterSpy.MCR.assertParameters("convert", 0, someDataGroup);
		String xml = (String) xmlConverterSpy.MCR.getReturnValue("convert", 0);

		fedoraWrapperSpy.MCR.assertParameters("create", 0, "someId", xml);
	}

	@Test
	public void testHandleExceptionOnConversionOnCreate() throws Exception {
		xmlConverterSpy.throwExceptionOnConvert = true;

		try {
			fedoraArchive.create("somType", "someId", someDataGroup);
		} catch (RecordConflictException e) {
			assertEquals(e.getMessage(),
					"Record could not be converted to xml and therefore could not be stored in Fedora Archive");
		}
	}

}
