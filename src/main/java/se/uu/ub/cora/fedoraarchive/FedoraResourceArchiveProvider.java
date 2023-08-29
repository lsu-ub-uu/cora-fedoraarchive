package se.uu.ub.cora.fedoraarchive;

import se.uu.ub.cora.fedora.FedoraFactory;
import se.uu.ub.cora.fedora.FedoraFactoryImp;
import se.uu.ub.cora.fedoraarchive.internal.FedoraResourceArchive;
import se.uu.ub.cora.initialize.SettingsProvider;
import se.uu.ub.cora.storage.archive.ResourceArchive;
import se.uu.ub.cora.storage.archive.ResourceArchiveInstanceProvider;

public class FedoraResourceArchiveProvider implements ResourceArchiveInstanceProvider {

	private static final String FEDORA_ARCHIVE_URL = "fedoraArchiveURL";
	private FedoraFactory fedoraFactory;

	@Override
	public int getOrderToSelectImplementionsBy() {
		return 0;
	}

	@Override
	public ResourceArchive getResourceArchive() {
		fedoraFactory = createFedoraFactoryUsingUrlSettingName(FEDORA_ARCHIVE_URL);
		return new FedoraResourceArchive(fedoraFactory.factorFedoraAdapter());
	}

	FedoraFactory createFedoraFactoryUsingUrlSettingName(String urlSettingName) {
		return new FedoraFactoryImp(SettingsProvider.getSetting(urlSettingName));
	}

	FedoraFactory onlyForTestgetFedoraFactory() {
		return fedoraFactory;
	}
}
