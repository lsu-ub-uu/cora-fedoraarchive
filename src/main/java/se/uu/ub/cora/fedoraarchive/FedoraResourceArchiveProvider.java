package se.uu.ub.cora.fedoraarchive;

import se.uu.ub.cora.fedoraarchive.internal.FedoraResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceArchiveInstanceProvider;

public class FedoraResourceArchiveProvider implements ResourceArchiveInstanceProvider {

	@Override
	public int getOrderToSelectImplementionsBy() {
		return 0;
	}

	@Override
	public ResourceArchive getResourceArchive() {
		return new FedoraResourceArchive(null);
	}

}
