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
package se.uu.ub.cora.fedoraarchive;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.converter.ConverterProvider;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraFactory;
import se.uu.ub.cora.fedora.FedoraFactoryImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraRecordArchive;
import se.uu.ub.cora.fedoraarchive.spy.ConverterFactorySpy;
import se.uu.ub.cora.fedoraarchive.spy.FedoraFactorySpy;
import se.uu.ub.cora.fedoraarchive.spy.LoggerFactorySpy;
import se.uu.ub.cora.fedoraarchive.spy.LoggerSpy;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.storage.StorageException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchiveProviderTest {

	private Map<String, String> initInfo = new HashMap<>();
	private FedoraRecordArchiveProvider provider;
	private String fedoraBaseUrl = "http://someFedoraUrl/";
	private ConverterFactorySpy converterFactorySpy;
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "FedoraRecordArchiveProvider";
	FedoraRecordArchiveProviderExtendedForTest providerForTest;

	@BeforeMethod
	public void beforeMethod() {
		setUpFactories();
		initInfo = new HashMap<>();
		initInfo.put("fedoraArchiveUrl", fedoraBaseUrl);
		provider = new FedoraRecordArchiveProvider();

		providerForTest = new FedoraRecordArchiveProviderExtendedForTest();
	}

	private void setUpFactories() {
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		converterFactorySpy = new ConverterFactorySpy();
		ConverterProvider.setConverterFactory("xml", converterFactorySpy);
	}

	@Test
	public void testInit() throws Exception {
		assertEquals(provider.getOrderToSelectImplementionsBy(), 0);
	}

	@Test(expectedExceptions = ArchiveException.class, expectedExceptionsMessageRegExp = ""
			+ "startUsingInitInfo MUST be called before calling getRecordArchive.")
	public void testThrowExceptionIfGetRecordArchiveCalledBeforeInit() throws Exception {
		provider.getRecordArchive();
	}

	@Test
	public void testStartUsingInitInfo() throws Exception {
		providerForTest.startUsingInitInfo(initInfo);
		FedoraFactoryImp fedoraFactory = (FedoraFactoryImp) providerForTest
				.onlyForTestGetFedoraFactory();
		assertEquals(fedoraFactory.onlyForTestGetBaseUrl(), initInfo.get("fedoraArchiveUrl"));
	}

	@Test
	public void testGetRecordArchive() throws Exception {
		provider.startUsingInitInfo(initInfo);

		RecordArchive recordArchive = provider.getRecordArchive();

		assertNotNull(recordArchive);
		assertTrue(recordArchive instanceof FedoraRecordArchive);
	}

	@Test
	public void testGetRecordArchiveCreatedWithConverterDependency() throws Exception {
		provider.startUsingInitInfo(initInfo);
		FedoraRecordArchive fedoraRecordArchive = (FedoraRecordArchive) provider.getRecordArchive();

		ExternallyConvertibleToStringConverter converter = fedoraRecordArchive
				.onlyForTestGetXmlConverter();

		assertSame(converterFactorySpy.MCR
				.getReturnValue("factorExternallyConvertableToStringConverter", 0), converter);
	}

	@Test
	public void testGetRecordArchiveTwiceReturnsTwoDifferentInstances() throws Exception {
		provider.startUsingInitInfo(initInfo);

		RecordArchive recordArchive = provider.getRecordArchive();
		RecordArchive recordArchive2 = provider.getRecordArchive();

		assertNotSame(recordArchive, recordArchive2);
	}

	@Test
	public void testGetRecordArchiveWithSpy() throws Exception {
		providerForTest.startUsingInitInfo(initInfo);
		FedoraFactorySpy fedoraFactorySpy = new FedoraFactorySpy();
		providerForTest.setFedoraFactory(fedoraFactorySpy);

		FedoraRecordArchive recordArchive = (FedoraRecordArchive) providerForTest
				.getRecordArchive();

		FedoraAdapter fedoraAdapter = recordArchive.onlyForTestGetFedoraAdapter();

		fedoraFactorySpy.MCR.assertReturn("factorFedoraAdapter", 0, fedoraAdapter);

	}

	public class FedoraRecordArchiveProviderExtendedForTest extends FedoraRecordArchiveProvider {

		FedoraFactory onlyForTestGetFedoraFactory() {
			return fedoraFactory;
		}

		void setFedoraFactory(FedoraFactory fedoraFactory) {
			this.fedoraFactory = fedoraFactory;
		}
	}

	@Test
	public void testNormalStartupReturnsFedoraRecordArchive() {
		provider.startUsingInitInfo(initInfo);
		RecordArchive recordArchive = provider.getRecordArchive();
		assertTrue(recordArchive instanceof FedoraRecordArchive);
	}

	@Test
	public void testLoggingNormalStartup() {
		LoggerSpy loggerSpy = getProviderLoggerSpy();
		loggerSpy.MCR.assertParameters("logInfoUsingMessage", 0,
				"FedoraRecordArchiveProvider starting FedoraRecordArchive...");
		loggerSpy.MCR.assertParameters("logInfoUsingMessage", 1,
				"Found http://someFedoraUrl/ as fedoraArchiveUrl");
		loggerSpy.MCR.assertParameters("logInfoUsingMessage", 2,
				"FedoraRecordArchiveProvider started FedoraRecordArchive");
	}

	private LoggerSpy getProviderLoggerSpy() {
		provider.startUsingInitInfo(initInfo);
		loggerFactorySpy.MCR.assertParameters("factorForClass", 0,
				FedoraRecordArchiveProvider.class);
		LoggerSpy loggerSpy = (LoggerSpy) loggerFactorySpy.MCR.getReturnValue("factorForClass", 0);
		return loggerSpy;
	}

	@Test(expectedExceptions = StorageException.class, expectedExceptionsMessageRegExp = ""
			+ "InitInfo must contain fedoraArchiveUrl")
	public void testErrorMissingFedoraArchiveUrlInInitInfo() throws Exception {
		provider.startUsingInitInfo(new HashMap<>());
	}

	@Test
	public void testLoggingMissingFedoraArchiveUrlInInitInfo() throws Exception {
		try {
			provider.startUsingInitInfo(new HashMap<>());
		} catch (Exception e) {
			LoggerSpy loggerSpy = getProviderLoggerSpy();
			loggerSpy.MCR.assertParameters("logInfoUsingMessage", 0,
					"FedoraRecordArchiveProvider starting FedoraRecordArchive...");
			loggerSpy.MCR.assertParameters("logFatalUsingMessage", 0,
					"InitInfo must contain fedoraArchiveUrl");
		}
	}
}
