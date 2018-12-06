package blockm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MatrixBuilderTest {

	@Test
	public void testSparseDiagonal() {
		int size = 1_000_000;
		MatrixBuilder b = new MatrixBuilder();
		for (int i = 0; i < size; i++) {
			b.put(i, i, i);
		}
		IMatrix m = b.finish();
		assertEquals(size, m.rows());
		assertEquals(size, m.columns());
		for (int i = 0; i < size; i++) {
			double val = m.get(i, i);
			assertEquals((double) i, val, 1e-16);
		}
		assertEquals(SparseMatrix.class, m.getClass());
	}

	@Test
	public void testFullDense() {
		MatrixBuilder b = new MatrixBuilder(0.1, 500_000);
		for (int row = 0; row < 2500; row++) {
			for (int col = 0; col < 1500; col++) {
				b.put(row, col, row * col);
			}
		}
		IMatrix m = b.finish();
		assertEquals(DenseMatrix.class, m.getClass());
		for (int row = 0; row < 2500; row++) {
			for (int col = 0; col < 1500; col++) {
				double val = m.get(row, col);
				assertEquals((double) row * col, val, 1e-16);
			}
		}
	}

	@Test
	public void testFullDenseMinSize() {
		MatrixBuilder b = new MatrixBuilder(0.1, 500_000);
		b.minSize(2000, 1000);
		for (int row = 0; row < 2500; row++) {
			for (int col = 0; col < 1500; col++) {
				b.put(row, col, row * col);
			}
		}
		IMatrix m = b.finish();
		assertEquals(DenseMatrix.class, m.getClass());
		for (int row = 0; row < 2500; row++) {
			for (int col = 0; col < 1500; col++) {
				double val = m.get(row, col);
				assertEquals((double) row * col, val, 1e-16);
			}
		}
	}
}
