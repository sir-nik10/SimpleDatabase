package heapdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table implements ITable {
	
	private List<Tuple> tuples;
	private Schema schema;
	
	public Table(Schema schema) {
		this.schema = schema;
		tuples = new ArrayList<>();
	}
	
	@Override
	public Schema getSchema() {
		return schema;
	}

	
	@Override
	public int size() {
		return tuples.size();
	}

	@Override
	public void close() {
		// do nothing
	}
	
	@Override
	public boolean insert(Tuple rec) {
		if (! rec.getSchema().equals(schema)) {
			throw new IllegalStateException("Error: tuple schema does not match table schema.");
		}
		// if schema has no key, then just add the tuple.
		if(schema.getKey() == null) {
			tuples.add(rec);
			return true;
		}
		// if schema has key, see if key already exists in table
		if(schema.getKey() != null) {
			for(int i=0; i<tuples.size();i++) {
				if(tuples.get(i).getKey().equals(rec.getKey())) {
					return false;
				}
			}
				tuples.add(rec);
				return true;
		}
		
		System.out.println("Error when inserting");
		return false;
		// TODO 
		//throw new UnsupportedOperationException();
		
		
		
	}

	@Override
	public boolean delete(Object key) {
		if (schema.getKey() == null) {
			throw new IllegalStateException("Error: table does not have a primary key.  Can not delete.");
		}
		
		// TODO 
		for(int i=0; i<tuples.size();i++) {
			if(tuples.get(i).getKey().equals(key)) {
				tuples.remove(i);
				return true;
			}
		}
		//System.out.print("Error when deleting");
		return false;
		//throw new UnsupportedOperationException();
	}
	

	@Override
	public Tuple lookup(Object key) {
		if (schema.getKey() == null) {
			throw new IllegalStateException("Error: table does not have a primary key.  Can not lookup by key.");
		}

		// TODO 
		for(int i=0; i<tuples.size();i++) {
			if(tuples.get(i).getKey().equals(key)) {
				return tuples.get(i);
			}
		}
		//System.out.print("Error when looking up");
		return null;
		//throw new UnsupportedOperationException();

	}

	@Override
	public ITable lookup(String colname, Object value) {
		if (schema.getColumnIndex(colname) < 0) {
			throw new IllegalStateException("Error: table does not contain column "+colname);
		}
		Table result = new Table(this.getSchema());
		
		// find all tuples that satisfy the predicate colname=value
		// and insert the tuples to result table.
		// return the result		
		//Select * From Table where colnam = value;
		
		// TODO 
		for(int i=0; i<tuples.size();i++) {
			if(tuples.get(i).get(colname).equals(value)) {
				result.insert(tuples.get(i));
			}
		}
		return result;
		//throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple> iterator() {
		return tuples.iterator();
	}
	
	public String toString() {
		if (tuples.isEmpty()) {
			return "Empty Table";
		} else {
			StringBuilder sb = new StringBuilder();
			for (Tuple t : this) {
				sb.append(t.toString());
				sb.append("\n");
			}
			return sb.toString();
		}
	}
}
