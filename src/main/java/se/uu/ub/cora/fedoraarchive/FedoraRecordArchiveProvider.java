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
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.fedora.FedoraAdapterImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraRecordArchive;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.httphandler.HttpHandlerFactoryImp;
import se.uu.ub.cora.storage.archive.RecordArchive;
import se.uu.ub.cora.storage.archive.RecordArchiveProvider;

public class FedoraRecordArchiveProvider implements RecordArchiveProvider {

	private FedoraRecordArchive fedoraRecordArchive;

	@Override
	public int getOrderToSelectImplementionsBy() {
		return 0;
	}

	@Override
	public void startUsingInitInfo(Map<String, String> initInfo) {
		String fedoraUrl = initInfo.get("fedoraArchiveUrl");
		HttpHandlerFactory httpHandlerFactory = new HttpHandlerFactoryImp();
		FedoraAdapterImp fedoraAdapter = new FedoraAdapterImp(httpHandlerFactory, fedoraUrl);
		ExternallyConvertibleToStringConverter converter = ConverterProvider
				.getExternallyConvertibleToStringConverter("xml");

		fedoraRecordArchive = new FedoraRecordArchive(converter, fedoraAdapter);

	}

	@Override
	public RecordArchive getRecordArchive() {
		return fedoraRecordArchive;
	}

}
