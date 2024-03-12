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
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.storage.StorageException;
import se.uu.ub.cora.storage.archive.ArchivePathBuilder;

public class ArchivePathBuilderTest {

	private static final String SOME_ARCHIVE_PATH = "someArchivePath";
	private static final String SOME_DATA_DIVIDER = "someDataDivider";
	private static final String SOME_TYPE = "someType";
	private static final String SOME_ID = "someId";
	private static final String SHA256_OF_OCFL_PATH_LAYOUT = "dad16615d91e1770634b9a6b2dae6d6b6e184f"
			+ "ca4260cbcce1b329b8c6b9c7e3";
	private static final String EXPECTED_ARCHIVE_BASE_MASTER = SOME_ARCHIVE_PATH + "/dad/166/15d/"
			+ SHA256_OF_OCFL_PATH_LAYOUT + "/v1/content/" + SOME_DATA_DIVIDER + ":" + SOME_TYPE
			+ ":" + SOME_ID + "-master";
	private static final String SOME_FILE_SYSTEM_BASE_PATH = "/tmp/streamStorageOnDiskTempStream/";

	private ArchivePathBuilderImp pathBuilder;

	@BeforeMethod
	private void beforeMethod() throws Exception {
		makeSureBasePathExistsAndIsEmpty();
		pathBuilder = new ArchivePathBuilderImp(SOME_ARCHIVE_PATH);
	}

	public void makeSureBasePathExistsAndIsEmpty() throws IOException {
		File dir = new File(SOME_FILE_SYSTEM_BASE_PATH);
		dir.mkdir();
		deleteFiles(SOME_FILE_SYSTEM_BASE_PATH);
	}

	private void deleteFiles(String path) throws IOException {
		Stream<Path> list;
		list = Files.list(Paths.get(path));
		list.forEach(p -> {
			try {
				deleteFile(p);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		list.close();
	}

	private void deleteFile(Path path) throws IOException {
		if (new File(path.toString()).isDirectory()) {
			deleteFiles(path.toString());
		}
		try {
			Files.delete(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod
	public void removeTempFiles() throws IOException {
		if (Files.exists(Paths.get(SOME_FILE_SYSTEM_BASE_PATH))) {
			deleteFiles(SOME_FILE_SYSTEM_BASE_PATH);
			File dir = new File(SOME_FILE_SYSTEM_BASE_PATH);
			dir.delete();
		}
	}

	@Test
	public void testArchivePathBuilderImpImplementsPathBuilder() throws Exception {
		assertTrue(pathBuilder instanceof ArchivePathBuilder);
	}

	@Test
	public void testCallBuildPathToAResourceInArchive() throws Exception {

		String path = pathBuilder.buildPathToAResourceInArchive(SOME_DATA_DIVIDER, SOME_TYPE,
				SOME_ID);

		assertEquals(path, EXPECTED_ARCHIVE_BASE_MASTER);
	}

	@Test
	public void testWrongAlgorithm() throws Exception {
		pathBuilder.onlyForTestSetHashAlgorithm("NonExistingAlgorithm");

		try {
			pathBuilder.buildPathToAResourceInArchive(SOME_DATA_DIVIDER, SOME_TYPE, SOME_ID);

			fail("It should fail");
		} catch (Exception e) {
			assertTrue(e instanceof StorageException);
			assertEquals(e.getMessage(), "Error while analyzing image.");
			assertEquals(e.getCause().getMessage(),
					"NonExistingAlgorithm MessageDigest not available");
		}
	}

	@Test
	public void testOnlyForTEstGetArchiveBasePath() throws Exception {
		assertEquals(pathBuilder.onlyForTestGetArchiveBasePath(), SOME_ARCHIVE_PATH);
	}

	@Test
	public void testByteToHexIfLengthIsOne() throws Exception {
		byte[] hash = { 1, 2, 11 };

		String bytesToHex = pathBuilder.bytesToHex(hash);

		assertEquals(bytesToHex, "01020b");
	}
}
