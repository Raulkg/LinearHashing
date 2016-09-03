import java.io.IOException;
import java.util.List;

public class LHAdaptee {

	public static void LHImport(String tableName, String fileName) {
		try {
			LinearHash lhash = LinearHash.getLinHash(tableName);
			if (null != lhash) lhash.importData(fileName);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static boolean InsertTuple(String tableName, String tupleStr) {
		boolean insert = true;
		try {
			LinearHash lhash = LinearHash.getLinHash(tableName);
			
			byte tuple[] = new byte[Tuple.TupleSize(tableName)];
			
			List<TupleAttribute> tupleConfig = Tuple.tupleAttr.get(tableName);
			
			String [] row = tupleStr.split(" ");
			for (int j =0; j < row.length; j++) {
				TupleAttribute attr = tupleConfig.get(j);
				String colData = Util.truncate(row[j], attr.getSize());
				int colIndex = attr.startIndex(tupleConfig);
				
				System.arraycopy(colData.getBytes(), 0, tuple, colIndex, colData.length());
			}
			
			
			if (null != lhash) {
				lhash.InsertTuple(tuple);
			} else {
				insert = false;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return insert;
	}
	
	public static byte[] Search(String tableName, byte[] keyValue){
		LinearHash lhash = null;
		try {
			lhash = LinearHash.getLinHash(tableName);
			if (null != lhash) {
				lhash.Search(keyValue);
			}
			else {
				return null;
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lhash.Search(keyValue);
		
	}
	
	public static String MemoryStatus() {
		String diskstatus = LinearHash.getDisk().DiskStatus();
		Integer pagesUsed = LinearHash.countPagesInUse();
		return "<b>DISK STATUS</b>" + diskstatus + "<tr><td>pages in use:</td><td>" + pagesUsed.toString() + "</td></tr>";
	}
	
	
	public static String ShowLinHash(String tableName) {
		try {
			LinearHash lhash = LinearHash.getLinHash(tableName);
			String lhash_string = null;
			if (null != lhash) {
				Double avg_length = lhash.getAverageChainLength();
				lhash_string = "<b>Avg chain length: "+ avg_length.toString() +"</b><br>";
				lhash_string += lhash.showLinearHash();
				
			}
			return lhash_string;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "not a table!";
	}

}
