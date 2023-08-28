package se.uu.ub.cora.fedoraarchive.internal;

import java.io.InputStream;
import java.text.MessageFormat;

import se.uu.ub.cora.fedora.FedoraAdapter;
import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.storage.ResourceConflictException;
import se.uu.ub.cora.storage.archive.ArchiveException;
import se.uu.ub.cora.storage.archive.ResourceArchive;

public class FedoraResourceArchive implements ResourceArchive {

	private FedoraAdapter fedoraAdapter;
	private static final String ARCHIVE_ID_FORMAT = "{0}:{1}-master";

	public FedoraResourceArchive(FedoraAdapter fedoraAdapter) {
		this.fedoraAdapter = fedoraAdapter;
	}

	@Override
	public void create(String type, String id, InputStream resource, String mimeType) {
		String archiveId = MessageFormat.format(ARCHIVE_ID_FORMAT, type, id);
		try {
			fedoraAdapter.createResource(archiveId, resource, mimeType);
		} catch (FedoraConflictException e) {
			throw ResourceConflictException.withMessage(e.getMessage());
		} catch (FedoraException e) {
			throw ArchiveException.withMessage(e.getMessage());
		}
	}

	@Override
	public InputStream read(String type, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(String type, String id, InputStream resource, String mimeType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(String type, String id) {
		// TODO Auto-generated method stub

	}

}
