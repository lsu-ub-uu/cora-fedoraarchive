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
package se.uu.ub.cora.fedoraarchive.path;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.archive.ArchivePathBuilder;
import se.uu.ub.cora.storage.spies.hash.CoraDigestorSpy;

public class ArchivePathBuilderTest {
	private static final String ARCHIVE_PATH = "someArchivePath";
	private static final String DATA_DIVIDER = "someDataDivider";
	private static final String TYPE = "someType";
	private static final String ID = "someId";

	private ArchivePathBuilderImp pathBuilder;
	private CoraDigestorSpy digestor;

	@BeforeMethod
	private void beforeMethod() {
		digestor = new CoraDigestorSpy();
		pathBuilder = ArchivePathBuilderImp.usingBasePathAndCoraDigestUtils(ARCHIVE_PATH, digestor);
	}

	@Test
	public void testArchivePathBuilderImpImplementsPathBuilder() {
		assertTrue(pathBuilder instanceof ArchivePathBuilder);
	}

	@Test
	public void testCallBuildPathToAResourceInArchive_sendsOcflPathToDigestor() {
		pathBuilder.buildPathToAResourceInArchive(DATA_DIVIDER, TYPE, ID);

		String ocflPathLayout = "info:fedora/{0}:{1}:{2}-master";
		String ocflPath = MessageFormat.format(ocflPathLayout, DATA_DIVIDER, TYPE, ID);

		digestor.MCR.assertParameters("stringToSha256Hex", 0, ocflPath);
	}

	@Test
	public void testCallBuildPathToAResourceInArchive_usesReturnedShaInPath() {
		digestor.MRV.setDefaultReturnValuesSupplier("stringToSha256Hex", () -> "ABCDEFGHIJKLMNO");

		String path = pathBuilder.buildPathToAResourceInArchive(DATA_DIVIDER, TYPE, ID);

		assertEquals(path, ARCHIVE_PATH
				+ "/abc/def/ghi/abcdefghijklmno/v1/content/someDataDivider:someType:someId-master");
	}

	@Test
	public void testOnlyForTEstGetArchiveBasePath() {
		assertEquals(pathBuilder.onlyForTestGetArchiveBasePath(), ARCHIVE_PATH);
	}

	@Test
	public void testOnlyForTestGetCoraDigestUtils() {
		assertSame(pathBuilder.onlyForTestGetCoraDigestor(), digestor);
	}
}
