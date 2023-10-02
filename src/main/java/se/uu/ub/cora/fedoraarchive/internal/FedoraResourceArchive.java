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
package se.uu.ub.cora.fedoraarchive.internal;

import java.io.InputStream;
import java.text.MessageFormat;

import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.fedora.FedoraNotFoundException;
import se.uu.ub.cora.storage.ResourceConflictException;
import se.uu.ub.cora.storage.ResourceNotFoundException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.ResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceMetadata;

public class FedoraResourceArchive implements ResourceArchive {

	public static final String RESOURCE_CREATE_CONFLICT_MESSAGE = ""
			+ "Failed to create record due to already existing record id in Fedora Archive for type {0}"
			+ " and id {1}.";
	public static final String RESOURCE_CREATE_ERR_MESSAGE = ""
			+ "Creation of record unsuccessful for type {0} and id {1}.";
	public static final String RESOURCE_READ_MISSING_MESSAGE = ""
			+ "Failed to read record due to it could not be found in Fedora Archive for type {0}"
			+ " and id {1}.";
	public static final String RESOURCE_READ_ERR_MESSAGE = ""
			+ "Reading of record unsuccessful for type {0} and id {1}.";

	private FedoraAdapter fedoraAdapter;
	private static final String ARCHIVE_ID_FORMAT = "{0}:{1}-master";

	public FedoraResourceArchive(FedoraAdapter fedoraAdapter) {
		this.fedoraAdapter = fedoraAdapter;
	}

	@Override
	public void create(String dataDivider, String type, String id, InputStream resource,
			String mimeType) {
		tryToCreateResource(dataDivider, type, id, resource, mimeType);
	}

	private void tryToCreateResource(String dataDivider, String type, String id,
			InputStream resource, String mimeType) {
		try {
			createResource(dataDivider, type, id, resource, mimeType);
		} catch (FedoraConflictException e) {
			throw createResourceConflictException(RESOURCE_CREATE_CONFLICT_MESSAGE, type, id, e);
		} catch (Exception e) {
			throw createArchiveException(RESOURCE_CREATE_ERR_MESSAGE, type, id, e);
		}
	}

	private void createResource(String dataDivider, String type, String id, InputStream resource,
			String mimeType) {
		String archiveId = ensembleId(type, id);
		fedoraAdapter.createResource(dataDivider, archiveId, resource, mimeType);
	}

	private String ensembleId(String type, String id) {
		return MessageFormat.format(ARCHIVE_ID_FORMAT, type, id);
	}

	private ResourceConflictException createResourceConflictException(String message, String type,
			String id, FedoraConflictException e) {
		return ResourceConflictException
				.withMessageAndException(MessageFormat.format(message, type, id), e);
	}

	private ArchiveException createArchiveException(String message, String type, String id,
			Exception e) {
		String formatedMessage = MessageFormat.format(message, type, id);
		return ArchiveException.withMessageAndException(formatedMessage, e);
	}

	@Override
	public InputStream read(String dataDivider, String type, String id) {
		return tryToReadResource(dataDivider, type, id);
	}

	private InputStream tryToReadResource(String dataDivider, String type, String id) {
		try {
			return readResource(dataDivider, type, id);
		} catch (FedoraNotFoundException e) {
			throw createResourceNotFoundException(RESOURCE_READ_MISSING_MESSAGE, type, id, e);
		} catch (Exception e) {
			throw createArchiveException(RESOURCE_READ_ERR_MESSAGE, type, id, e);
		}
	}

	private InputStream readResource(String dataDivider, String type, String id) {
		String archiveId = ensembleId(type, id);
		return fedoraAdapter.readResource(dataDivider, archiveId);
	}

	private ResourceNotFoundException createResourceNotFoundException(String message, String type,
			String id, FedoraNotFoundException e) {
		String formatedMessage = MessageFormat.format(message, type, id);
		return ResourceNotFoundException.withMessageAndException(formatedMessage, e);
	}

	@Override
	public ResourceMetadata readMetadata(String dataDivider, String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(String dataDivider, String type, String id, InputStream resource,
			String mimeType) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public void delete(String dataDivider, String type, String id) {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	public FedoraAdapter onlyForTestGetFedoraAdapter() {
		return fedoraAdapter;
	}

}
