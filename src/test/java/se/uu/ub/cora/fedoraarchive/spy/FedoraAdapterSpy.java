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
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;
import se.uu.ub.cora.testutils.mrv.MethodReturnValues;

public class FedoraAdapterSpy implements FedoraAdapter {
	public MethodCallRecorder MCR = new MethodCallRecorder();
	public MethodReturnValues MRV = new MethodReturnValues();

	public FedoraAdapterSpy() {
		MCR.useMRV(MRV);
		MRV.setDefaultReturnValuesSupplier("readRecord", String::new);
		MRV.setDefaultReturnValuesSupplier("readResource", InputStreamSpy::new);
	}

	@Override
	public void createRecord(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);
	}

	@Override
	public void createResource(String resourceId, InputStream resource, String mimeType) {
		MCR.addCall("recordId", resourceId, "resource", resource, "binaryContentType", mimeType);
	}

	@Override
	public String readRecord(String recordId) {
		return (String) MCR.addCallAndReturnFromMRV("recordId", recordId);
	}

	@Override
	public InputStream readResource(String resourceId) {
		return (InputStream) MCR.addCallAndReturnFromMRV("resourceId", resourceId);
	}

	@Override
	public void updateRecord(String recordId, String recordXml) {
		MCR.addCall("recordId", recordId, "recordXml", recordXml);
	}

	@Override
	public void updateResource(String resourceId, InputStream resource, String mimeType) {
		MCR.addCall("resourceId", resourceId, "resource", resource, "mimeType", mimeType);
	}

	@Override
	public void delete(String id) {
		MCR.addCall("id", id);
	}
}
