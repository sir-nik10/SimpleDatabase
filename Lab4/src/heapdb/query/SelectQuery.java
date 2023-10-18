package heapdb.query;


import java.util.ArrayList;

import heapdb.ITable;
import heapdb.Schema;
import heapdb.Table;
import heapdb.Tuple;

/**
 * A simple select query of the form:
 * select column, column, . . . from table where condition
 * 
 * @author Glenn
 *
 */

public class SelectQuery  {
	
	private Condition cond;
	private String[] colNames;	   // a value of null means return all columns of the table
	
	/**
	 * A query that contains both a where condition and a projection of columns
	 * @param colNames are the columns to return
	 * @param cond is the where clause
	 */
	public SelectQuery(String[] colNames, Condition cond) {
		this.colNames = colNames;
		this.cond = cond;
	}
	
	/**
	 * A query that contains both a where condition.  All columns 
	 * of the Tuples are returned.
	 * @param cond is the where clause
	 */
	public SelectQuery(Condition cond) {
		this(null, cond);
	}
	
	public static Table naturalJoin(ITable table1, ITable table2) {
		Schema resultSchema = table1.getSchema().naturaljoin(table2.getSchema());
		Table result = new Table(resultSchema);
		ArrayList<String> joinColumns  = new ArrayList<>();
		// TODO  find the list of join column of the 2 tables
		
		for(int i=0;i<table1.getSchema().size();i++) {
			for(int j=0;j<table2.getSchema().size();j++) {
				if(table1.getSchema().getName(i).equals(table2.getSchema().getName(j))) {
					joinColumns.add(table1.getSchema().getName(i));
				}
			}
		}
		for (Tuple t1: table1) {
			for (Tuple t2: table2) {
				// TODO if t1 matches all join columns of t2
					// TODO then insert the join of t1 and t2 into 
				for(int i=0;i<joinColumns.size();i++) {
					if(t1.get(joinColumns.get(i)).equals(t2.get(joinColumns.get(i)))){
						result.insert(Tuple.joinTuple(resultSchema, t1, t2));
					}
				}
			}
		}
		return result;
		}
	
	public ITable eval(ITable table) {
		Schema s;
		// if there is a projection operation, make the new schema
		if (colNames==null) s = table.getSchema();
		else s = table.getSchema().project(colNames);
		
		Table result = new Table(s);
		for (Tuple t: table) {
			// TODO 
			if (cond.eval(t)) {
				result.insert(t.project(s));
			}
			// if tuple t satisfies the condition, insert t into the result table.
			
		}
		return result;
	}

	@Override
	public String toString() {
	    String proj_columns;
	    if (colNames != null) {
	    	proj_columns = String.join(",", colNames);
	    } else {
	    	proj_columns = "*";
	    }
	    return "select " + proj_columns + " where " + cond;
	}

}
