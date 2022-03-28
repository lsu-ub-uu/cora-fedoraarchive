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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.converter.ConverterProvider;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.fedora.FedoraAdapterImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraRecordArchive;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.httphandler.HttpHandlerFactoryImp;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.storage.StorageException;
import se.uu.ub.cora.storage.archive.RecordArchive;
import se.uu.ub.cora.storage.archive.RecordArchiveProvider;

public class FedoraRecordArchiveProviderTest {

	private Map<String, String> initInfo = new HashMap<>();
	private FedoraRecordArchiveProvider provider;
	private String fedoraBaseUrl = "http://someFedoraUrl/";
	private ConverterFactorySpy converterFactorySpy;
	private LoggerFactorySpy loggerFactorySpy;
	private String testedClassName = "FedoraRecordArchiveProvider";

	@BeforeMethod
	public void beforeMethod() {
		setUpFactories();
		initInfo = new HashMap<>();
		initInfo.put("fedoraArchiveUrl", fedoraBaseUrl);
		provider = new FedoraRecordArchiveProvider();
	}

	private void setUpFactories() {
		loggerFactorySpy = new LoggerFactorySpy();
		LoggerProvider.setLoggerFactory(loggerFactorySpy);
		converterFactorySpy = new ConverterFactorySpy();
		ConverterProvider.setConverterFactory("xml", converterFactorySpy);
	}

	@Test
	public void testInit() throws Exception {
		RecordArchiveProvider fedoraRecordArchiveProvider = new FedoraRecordArchiveProvider();
	}

	@Test
	public void testStartUsingInitInfo() throws Exception {
		provider.startUsingInitInfo(initInfo);
		assertEquals(provider.getOrderToSelectImplementionsBy(), 0);
	}

	@Test
	public void testNormalStartupReturnsFedoraRecordArchive() {
		provider.startUsingInitInfo(initInfo);
		RecordArchive recordArchive = provider.getRecordArchive();
		assertTrue(recordArchive instanceof FedoraRecordArchive);
	}

	@Test
	public void testInitializeFedoraAdapterImp() throws Exception {
		provider.startUsingInitInfo(initInfo);
		FedoraRecordArchive fedoraRecordArchive = (FedoraRecordArchive) provider.getRecordArchive();

		FedoraAdapterImp fedoraAdapter = (FedoraAdapterImp) fedoraRecordArchive
				.onlyForTestGetFedoraAdapter();
		String baseUrl = fedoraAdapter.getBaseUrl();

		assertEquals(baseUrl, fedoraBaseUrl);

		HttpHandlerFactory httpHandlerFactory = fedoraAdapter.getHttpHandlerFactory();

		assertTrue(httpHandlerFactory instanceof HttpHandlerFactoryImp);
	}

	@Test
	public void testInitializeFedoraRecordArchive() throws Exception {
		provider.startUsingInitInfo(initInfo);
		FedoraRecordArchive fedoraRecordArchive = (FedoraRecordArchive) provider.getRecordArchive();

		ExternallyConvertibleToStringConverter converter = fedoraRecordArchive
				.onlyForTestGetXmlConverter();

		assertSame(converterFactorySpy.MCR
				.getReturnValue("factorExternallyConvertableToStringConverter", 0), converter);
	}

	@Test
	public void testLoggingNormalStartup() {
		provider.startUsingInitInfo(initInfo);
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"FedoraRecordArchiveProvider starting FedoraRecordArchive...");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 1),
				"Found http://someFedoraUrl/ as fedoraArchiveUrl");
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 2),
				"FedoraRecordArchiveProvider started FedoraRecordArchive");
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
		}
		assertEquals(loggerFactorySpy.getInfoLogMessageUsingClassNameAndNo(testedClassName, 0),
				"FedoraRecordArchiveProvider starting FedoraRecordArchive...");
		assertEquals(loggerFactorySpy.getFatalLogMessageUsingClassNameAndNo(testedClassName, 0),
				"InitInfo must contain fedoraArchiveUrl");
	}
}
