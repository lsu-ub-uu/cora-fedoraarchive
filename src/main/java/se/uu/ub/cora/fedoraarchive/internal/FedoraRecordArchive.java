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

import se.uu.ub.cora.converter.ConverterException;
import se.uu.ub.cora.converter.ExternallyConvertibleToStringConverter;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.storage.RecordConflictException;
import se.uu.ub.cora.storage.archive.RecordArchive;

public class FedoraRecordArchive implements RecordArchive {

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
			// TODO: do we need to store type in Fedora??? If yes, how???
			// TODO: Hantera dubbletter och andra fel.
			// TODO: Hantera dubbletter och andra fel.

			String xml = xmlConverter.convert(dataRecord);
			fedoraAdapter.create(id, xml);

		} catch (FedoraException e) {
			throw RecordConflictException
					.withMessage("Record could not be created in Fedora Archive");
		} catch (ConverterException e) {
			throw RecordConflictException.withMessage(
					"Record could not be converted to xml and therefore could not be stored "
							+ "in Fedora Archive");
		}
	}

	public FedoraAdapter onlyForTestGetFedoraAdapter() {
		return fedoraAdapter;

	}

	public ExternallyConvertibleToStringConverter onlyForTestGetXmlConverter() {
		return xmlConverter;
	}

}
