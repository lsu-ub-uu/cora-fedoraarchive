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
import se.uu.ub.cora.fedora.FedoraNotFoundException;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.RecordNotFoundException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchive implements RecordArchive {
	public static final String RECORD_CREATE_CONFLICT_MESSAGE = ""
			+ "Failed to create record due to already existing record id in Fedora Archive for type {0} "
			+ "and id {1}.";
	public static final String RECORD_CREATE_ERR_MESSAGE = ""
			+ "Creation of record unsuccessful for type {0} and id {1}.";
	public static final String RECORD_UPDATE_MISSING_MESSAGE = ""
			+ "Update of record could not be executed since it was not located in Fedora Archive "
			+ "for type {0} and id {1}.";
	public static final String RECORD_UPDATE_ERR_MESSAGE = ""
			+ "Unable to update record in Fedora Archive for type {0} and id {1}.";

	private FedoraAdapter fedoraAdapter;
	private ExternallyConvertibleToStringConverter xmlConverter;

	public FedoraRecordArchive(ExternallyConvertibleToStringConverter xmlConverter,
			FedoraAdapter fedoraWrapper) {
		this.xmlConverter = xmlConverter;
		this.fedoraAdapter = fedoraWrapper;
	}

	@Override
	public void create(String type, String id, DataGroup dataRecord) {
		try {
			String combinedId = combineTypeAndId(type, id);
			tryToCreate(combinedId, dataRecord);
		} catch (FedoraConflictException e) {
			throw RecordConflictException.withMessageAndException(
					MessageFormat.format(RECORD_CREATE_CONFLICT_MESSAGE, type, id), e);
		} catch (Exception e) {
			throw ArchiveException.withMessageAndException(
					MessageFormat.format(RECORD_CREATE_ERR_MESSAGE, type, id), e);
		}
	}

	private String combineTypeAndId(String type, String id) {
		return type + ":" + id;
	}

	private void tryToCreate(String id, DataGroup dataRecord) {
		String xml = xmlConverter.convert(dataRecord);
		fedoraAdapter.createRecord(null, id, xml);
	}

	@Override
	public void update(String type, String id, DataGroup dataRecord) {
		try {
			String combinedId = combineTypeAndId(type, id);
			tryToUpdate(combinedId, dataRecord);
		} catch (FedoraNotFoundException e) {
			throw RecordNotFoundException.withMessageAndException(
					MessageFormat.format(RECORD_UPDATE_MISSING_MESSAGE, type, id), e);
		} catch (Exception e) {
			throw ArchiveException.withMessageAndException(
					MessageFormat.format(RECORD_UPDATE_ERR_MESSAGE, type, id), e);
		}
	}

	private void tryToUpdate(String id, DataGroup dataRecord) {
		String xml = xmlConverter.convert(dataRecord);
		fedoraAdapter.updateRecord(null, id, xml);
	}

	public FedoraAdapter onlyForTestGetFedoraAdapter() {
		return fedoraAdapter;
	}

	public ExternallyConvertibleToStringConverter onlyForTestGetXmlConverter() {
		return xmlConverter;
	}
}
