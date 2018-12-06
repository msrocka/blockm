package blockm;

import gnu.trove.impl.Constants;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * A sparse matrix implementation that uses primitive hash maps from the Trove
 * project to store the data. Filling this matrix is fast with relatively low
 * memory consumption.
 */
public class SparseMatrix implements IMatrix {

	int rows = 0;
	int cols = 0;

	private final TIntObjectHashMap<TIntDoubleHashMap> data;

	public SparseMatrix() {
		data = new TIntObjectHashMap<>(
			Constants.DEFAULT_CAPACITY,
			Constants.DEFAULT_LOAD_FACTOR,
			-1);
	}

	@Override
	public int rows() {
		return rows;
	}

	@Override
	public int columns() {
		return cols;
	}

	@Override
	public void set(int row, int col, double val) {
		// do nothing if val = 0 *and* when there is no value to overwrite
		if (val == 0 && !hasEntry(row, col))
			return;
		if (row >= rows) {
			rows = row + 1;
		}
		if (col >= cols) {
			cols = col + 1;
		}
		TIntDoubleHashMap rowMap = data.get(row);
		if (rowMap == null) {
			rowMap = new TIntDoubleHashMap(
				Constants.DEFAULT_CAPACITY,
				Constants.DEFAULT_LOAD_FACTOR,
				-1,
				0);
			data.put(row, rowMap);
		}
		rowMap.put(col, val);
	}

	private boolean hasEntry(int row, int col) {
		TIntDoubleHashMap rowMap = data.get(row);
		if (rowMap == null)
			return false;
		return rowMap.get(col) != 0;
	}

	public void clear() {
		data.clear();
		rows = 0;
		cols = 0;
	}

	@Override
	public double get(int row, int col) {
		TIntDoubleHashMap rowMap = data.get(row);
		if (rowMap == null)
			return 0;
		return rowMap.get(col);
	}

	@Override
	public double[] getColumn(int i) {
		double[] column = new double[rows];
		for (int row = 0; row < rows; row++) {
			column[row] = get(row, i);
		}
		return column;
	}

	@Override
	public double[] getRow(int i) {
		double[] row = new double[cols];
		for (int col = 0; col < cols; col++) {
			row[col] = get(i, col);
		}
		return row;
	}

	@Override
	public SparseMatrix copy() {
		SparseMatrix copy = new SparseMatrix();
		TIntObjectIterator<TIntDoubleHashMap> rows = data.iterator();
		while (rows.hasNext()) {
			rows.advance();
			int row = rows.key();
			TIntDoubleIterator cols = rows.value().iterator();
			while (cols.hasNext()) {
				cols.advance();
				copy.set(row, cols.key(), cols.value());
			}
		}
		return copy;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("SparseMatrix = [");
		int maxRows = rows > 15 ? 15 : rows;
		int maxCols = cols > 15 ? 15 : cols;
		for (int row = 0; row < maxRows; row++) {
			for (int col = 0; col < maxCols; col++) {
				double val = get(row, col);
				builder.append(val);
				if (col < (maxCols - 1))
					builder.append(",");
			}
			if (row < (maxRows - 1))
				builder.append(";");
		}
		if (maxCols < cols)
			builder.append(", ...");
		if (maxRows < rows)
			builder.append("; ...");
		builder.append("]");
		return builder.toString();
	}

}
