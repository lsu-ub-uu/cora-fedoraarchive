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

import java.text.MessageFormat;

import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchive implements RecordArchive {

	private FedoraAdapter fedoraAdapter;
	private ExternallyConvertibleToStringConverter xmlConverter;
	private String pattern = "Record could not be created in Fedora Archive for type: {0} and id: {1}";

	public FedoraRecordArchive(ExternallyConvertibleToStringConverter xmlConverter,
			FedoraAdapter fedoraWrapper) {
		this.xmlConverter = xmlConverter;
		this.fedoraAdapter = fedoraWrapper;
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord) {
		try {
			String combinedId = type + ":" + id;
			tryToCreate(combinedId, dataRecord);
		} catch (FedoraConflictException e) {
			throw RecordConflictException
					.withMessageAndException(MessageFormat.format(pattern, type, id), e);
		} catch (Exception e) {
			throw ArchiveException.withMessageAndException(MessageFormat.format(pattern, type, id),
					e);
		}
	}

	private void tryToCreate(String id, DataGroup dataRecord) {
		String xml = xmlConverter.convert(dataRecord);
		fedoraAdapter.create(id, xml);
	}

	public FedoraAdapter onlyForTestGetFedoraAdapter() {
		return fedoraAdapter;
	}

	public ExternallyConvertibleToStringConverter onlyForTestGetXmlConverter() {
		return xmlConverter;
	}

}
