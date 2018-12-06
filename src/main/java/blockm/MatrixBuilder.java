package blockm;

public class MatrixBuilder {

	private final double maxSparseFileRate;
	private final int checkpoint;

	private int sparseEntries = 0;
	private int totalRowCount = 0;
	private int totalColCount = 0;
	private SparseMatrix sparseBlock = new SparseMatrix();

	private int denseRowCount = 0;
	private int denseColCount = 0;
	private DenseMatrix denseBlock;

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

	public void put(int row, int col, double val) {
		if (row < 0 || col < 0)
			return;
		if (denseRowCount > row && denseColCount > col) {
			denseBlock.set(row, col, val);
			return;
		}
	}
}
