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
