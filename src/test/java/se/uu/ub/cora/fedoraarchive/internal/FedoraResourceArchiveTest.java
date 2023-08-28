package se.uu.ub.cora.fedoraarchive.internal;

import java.io.InputStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.fedora.FedoraConflictException;
import se.uu.ub.cora.fedora.FedoraException;
import se.uu.ub.cora.fedoraarchive.spy.FedoraAdapterSpy;
import se.uu.ub.cora.fedoraarchive.spy.InputStreamSpy;
import se.uu.ub.cora.storage.ResourceConflictException;
import se.uu.ub.cora.storage.archive.ArchiveException;

public class FedoraResourceArchiveTest {

	private static final String SOME_MIME_TYPE = "someMimeType";
	private static final String SOME_ID = "someId";
	private static final String SOME_TYPE = "someType";
	FedoraResourceArchive archive;
	FedoraAdapterSpy fedoraAdapter;
	private InputStream stream;

	@BeforeMethod
	public void beforeMethod() {
		fedoraAdapter = new FedoraAdapterSpy();
		archive = new FedoraResourceArchive(fedoraAdapter);
		stream = new InputStreamSpy();
	}

	@Test
	public void testCreate() throws Exception {
		archive.create(SOME_TYPE, SOME_ID, stream, SOME_MIME_TYPE);
		String id = SOME_TYPE + ":" + SOME_ID + "-master";

		fedoraAdapter.MCR.assertParameters("createResource", 0, id, stream, SOME_MIME_TYPE);
	}

	@Test(expectedExceptions = ResourceConflictException.class, expectedExceptionsMessageRegExp = "someMessage")
	public void testCreateConflictException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraConflictException.withMessage("someMessage"));

		archive.create(SOME_MIME_TYPE, SOME_ID, stream, SOME_ID);
	}

	@Test(expectedExceptions = ArchiveException.class, expectedExceptionsMessageRegExp = "someMessage")
	public void testCreateAnyOtherException() throws Exception {
		fedoraAdapter.MRV.setAlwaysThrowException("createResource",
				FedoraException.withMessage("someMessage"));

		archive.create(SOME_MIME_TYPE, SOME_ID, stream, SOME_ID);
	}

}
