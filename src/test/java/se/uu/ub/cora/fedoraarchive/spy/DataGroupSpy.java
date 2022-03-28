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

}
