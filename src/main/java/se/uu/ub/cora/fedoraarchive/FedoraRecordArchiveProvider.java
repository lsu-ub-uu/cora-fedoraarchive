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

import java.util.Map;

import se.uu.ub.cora.converter.ConverterProvider;
import se.uu.ub.cora.fedora.FedoraFactory;
import se.uu.ub.cora.fedora.FedoraFactoryImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraRecordArchive;
import se.uu.ub.cora.logger.Logger;
import se.uu.ub.cora.logger.LoggerProvider;
import se.uu.ub.cora.storage.StorageException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;
import se.uu.ub.cora.storage.archive.RecordArchiveProvider;

public class FedoraRecordArchiveProvider implements RecordArchiveProvider {

	private Logger logger = LoggerProvider.getLoggerForClass(FedoraRecordArchiveProvider.class);
	private Map<String, String> initInfo;

	FedoraFactory fedoraFactory;

	@Override
	public int getOrderToSelectImplementionsBy() {
		return 0;
	}

	@Override
	public void startUsingInitInfo(Map<String, String> initInfo) {
		this.initInfo = initInfo;
		logAndStartFedoraRecordArchive();
	}

	private void logAndStartFedoraRecordArchive() {
		logger.logInfoUsingMessage("FedoraRecordArchiveProvider starting FedoraRecordArchive...");
		startFedoraRecordArchive();
		logger.logInfoUsingMessage("FedoraRecordArchiveProvider started FedoraRecordArchive");
	}

	private void startFedoraRecordArchive() {
		fedoraFactory = new FedoraFactoryImp(
				tryToGetInitParameterLogIfFoundThrowErrorIfNot("fedoraArchiveUrl"));
	}

	private String tryToGetInitParameterLogIfFoundThrowErrorIfNot(String parameterName) {
		String parameterValue = tryToGetInitParameter(parameterName);
		logger.logInfoUsingMessage("Found " + parameterValue + " as " + parameterName);
		return parameterValue;
	}

	private String tryToGetInitParameter(String parameterName) {
		throwErrorIfKeyIsMissingFromInitInfo(parameterName);
		return initInfo.get(parameterName);
	}

	private void throwErrorIfKeyIsMissingFromInitInfo(String key) {
		if (!initInfo.containsKey(key)) {
			String errorMessage = "InitInfo must contain " + key;
			logger.logFatalUsingMessage(errorMessage);
			throw StorageException.withMessage(errorMessage);
		}
	}

	@Override
	public RecordArchive getRecordArchive() {
		if (fedoraFactory == null) {
			throw ArchiveException.withMessage(
					"startUsingInitInfo MUST be called before calling getRecordArchive.");
		}
		var xmlConverter = ConverterProvider.getExternallyConvertibleToStringConverter("xml");
		var fedoraAdapter = fedoraFactory.factorFedoraAdapter();
		return new FedoraRecordArchive(xmlConverter, fedoraAdapter);
	}
}
