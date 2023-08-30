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
package se.uu.ub.cora.fedoraarchive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.fedora.FedoraFactory;
import se.uu.ub.cora.fedora.FedoraFactoryImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraResourceArchive;
import se.uu.ub.cora.fedoraarchive.spy.FedoraAdapterSpy;
import se.uu.ub.cora.fedoraarchive.spy.FedoraFactorySpy;
import se.uu.ub.cora.initialize.SettingsProvider;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.storage.archive.ResourceArchiveInstanceProvider;
import se.uu.ub.cora.testspies.logger.LoggerFactorySpy;

public class FedoraResourceArchiveProviderTest {

	private static final String SOME_FEDORA_ARCHIVE_URL = "someFedoraArchiveURLa";
	FedoraResourceArchiveProvider provider;
	FedoraAdapterSpy fedoraAdapterSpy = new FedoraAdapterSpy();
	FedoraFactorySpy fedoraFactorySpy = new FedoraFactorySpy();
	Map<String, String> settings;
	private LoggerFactorySpy loggerFactorySpy;

	@BeforeMethod
	public void beforeMethod() {
		provider = new FedoraResourceArchiveProvider();
		settings = new HashMap<>();
		settings.put("fedoraArchiveURL", SOME_FEDORA_ARCHIVE_URL);
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		SettingsProvider.setSettings(settings);

	}

	@Test
	public void testiImplementsResourceArchiveInstanceProvider() throws Exception {
		assertTrue(provider instanceof ResourceArchiveInstanceProvider);
	}

	@Test
	public void testGetResourceArchive() throws Exception {
		FedoraResourceArchive resourceArchive = (FedoraResourceArchive) provider
				.getResourceArchive();

		assertNotNull(resourceArchive);
		assertTrue(resourceArchive instanceof FedoraResourceArchive);
		FedoraFactoryImp fedoraFactory = (FedoraFactoryImp) provider.onlyForTestgetFedoraFactory();
		assertEquals(fedoraFactory.onlyForTestGetBaseUrl(), SOME_FEDORA_ARCHIVE_URL);
	}

	@Test
	public void testName() throws Exception {
		fedoraFactorySpy.MRV.setDefaultReturnValuesSupplier("factorFedoraAdapter",
				() -> fedoraAdapterSpy);
		FedoraRecordArchiveProviderExtendedForTest providerForTest = new FedoraRecordArchiveProviderExtendedForTest();
		providerForTest.getResourceArchive();

		fedoraFactorySpy.MCR.assertParameters("factorFedoraAdapter", 0);

	}

	public class FedoraRecordArchiveProviderExtendedForTest extends FedoraResourceArchiveProvider {

		@Override
		public FedoraFactory createFedoraFactoryUsingUrlSettingName(String urlSettingName) {
			return fedoraFactorySpy;
		}
	}

}
