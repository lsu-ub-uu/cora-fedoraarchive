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
package se.uu.ub.cora.fedoraarchive.spy;

import java.io.InputStream;

import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class FedoraAdapterSpy implements FedoraAdapter {

	public MethodCallRecorder MCR = new MethodCallRecorder();
	public boolean throwExceptionOnCreateRecordAlreadyExists = false;

	@Override
	public void create(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);

		if (throwExceptionOnCreateRecordAlreadyExists) {
			throw FedoraConflictException.withMessage("From spy, record alreadyExists");
		}

	}

	@Override
	public void createBinary(String recordId, InputStream binary, String binaryContentType) {
		MCR.addCall("recordId", recordId, "binary", binary, "binaryContentType", binaryContentType);

	}

	@Override
	public String read(String recordId) {
		MCR.addCall("recordId", recordId);

		String dataArchived = "";
		MCR.addReturned(dataArchived);
		return dataArchived;
	}

	@Override
	public InputStream readBinary(String recordId) {
		MCR.addCall("recordId", recordId);

		InputStream stream = null;
		MCR.addReturned(stream);
		return stream;
	}

	@Override
	public void update(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);

	}

}
