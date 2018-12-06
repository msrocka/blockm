package blockm;

public class MatrixBuilder {

	private final double maxSparseFileRate;
	private final int checkpoint;

	private int sparseEntries = 0;
	private SparseMatrix sparse = new SparseMatrix();
	private DenseMatrix dense;
	private int denseCols; // we need these because
	private int denseRows; // dense is null by default

	public MatrixBuilder() {
		this(0.2, 5000);
	}

	public MatrixBuilder(double maxSparseFileRate) {
		this(maxSparseFileRate, 5000);
	}

	public MatrixBuilder(double maxSparseFileRate, int checkpoint) {
		this.maxSparseFileRate = maxSparseFileRate;
		this.checkpoint = checkpoint;
	}

	public void minSize(int rows, int cols) {
		if (sparse.rows < rows) {
			sparse.rows = rows;
		}
		if (sparse.cols < cols) {
			sparse.cols = cols;
		}
	}

	public void put(int row, int col, double val) {
		if (val == 0 || row < 0 || col < 0)
			return;
		if (row < denseRows && col < denseCols) {
			dense.set(row, col, val);
			return;
		}
		sparse.set(row, col, val);
		sparseEntries++;
		if (sparseEntries % checkpoint == 0) {
			int n = sparse.rows * sparse.cols
					- denseRows * denseCols;
			double fr = (double) sparseEntries / (double) n;
			if (fr > maxSparseFileRate) {
				mapDense();
			}
		}
	}

	public IMatrix finish() {
		if (dense != null) {
			mapDense();
			return dense;
		}
		int n = sparse.rows * sparse.cols;
		double fr = (double) sparseEntries / (double) n;
		if (fr > maxSparseFileRate) {
			mapDense();
			return dense;
		}
		return sparse;
	}

	private void mapDense() {
		if (dense == null) {
			dense = new DenseMatrix(
					sparse.rows, sparse.cols);
		} else if (dense.rows < sparse.rows
				|| dense.columns < sparse.cols) {
			DenseMatrix next = new DenseMatrix(
					sparse.rows, sparse.cols);
			for (int col = 0; col < dense.columns; col++) {
				int oldIdx = col * dense.rows;
				int nextIdx = col * next.rows;
				System.arraycopy(dense.data, oldIdx,
						next.data, nextIdx, dense.rows);
			}
			dense = next;
		}
		denseRows = dense.rows;
		denseCols = dense.columns;
		sparse.iterate(
				(row, col, val) -> dense.set(row, col, val));
		sparse.clear();
		sparseEntries = 0;
	}

}
