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

import java.util.Collection;
import java.util.List;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataAttribute;
import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.testutils.mcr.MethodCallRecorder;

public class DataGroupSpy implements DataGroup {

	MethodCallRecorder MCR = new MethodCallRecorder();

	@Override
	public String getRepeatId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setRepeatId(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNameInData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addChild(DataElement arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addChildren(Collection<DataElement> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsChildWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<DataElement> getAllChildrenWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataElement> getAllChildrenWithNameInDataAndAttributes(String arg0,
			DataAttribute... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataAtomic> getAllDataAtomicsWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataGroup> getAllGroupsWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataGroup> getAllGroupsWithNameInDataAndAttributes(String arg0,
			DataAttribute... arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DataElement> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFirstAtomicValueWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataElement getFirstChildWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataAtomic getFirstDataAtomicWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGroup getFirstGroupWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllChildrenWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAllChildrenWithNameInDataAndAttributes(String arg0,
			DataAttribute... arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeFirstChildWithNameInData(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addAttributeByIdWithValue(String nameInData, String value) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasAttributes() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataAttribute getAttribute(String nameInData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataAttribute> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

}
