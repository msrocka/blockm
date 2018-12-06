package blockm;

public class MatrixBuilder {

	private final double maxSparseFileRate;
	private final int checkpoint;

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

}
