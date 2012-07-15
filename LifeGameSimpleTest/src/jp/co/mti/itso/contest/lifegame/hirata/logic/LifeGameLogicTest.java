package jp.co.mti.itso.contest.lifegame.hirata.logic;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.test.AndroidTestCase;

/**
 * 
 * @author hirata_ta
 * 
 */
public final class LifeGameLogicTest extends AndroidTestCase {

	/**
	 * ロジック
	 */
	private LifeGameLogic logic;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		logic = new LifeGameLogic();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 周りのライフの数を数えるメソッドのテスト
	 */
	public void testLifeCount() {
		// prepare
		int block[][] = { { 0, 1, 0 }, { 0, 1, 0 }, { 0, 1, 0 }, { 0, 1, 0 } };
		int actualCount = logic.lifeCount(block, 1, 1);
		assertEquals(2, actualCount);
		actualCount = logic.lifeCount(block, 0, 0);
		assertEquals(2, actualCount);
		actualCount = logic.lifeCount(block, 2, 1);
		assertEquals(2, actualCount);
		actualCount = logic.lifeCount(block, 1, 2);
		assertEquals(3, actualCount);
	}

	/**
	 * 周りのライフの数をもとに生死を判定するメソッドのテスト
	 */
	public void testCheckLifeCount() {
		int actualState = logic.checkLifeCount(0, 1);
		assertEquals(0, actualState);
		actualState = logic.checkLifeCount(1, 1);
		assertEquals(0, actualState);
		actualState = logic.checkLifeCount(2, 1);
		assertEquals(1, actualState);
		actualState = logic.checkLifeCount(3, 1);
		assertEquals(1, actualState);
		actualState = logic.checkLifeCount(3, 0);
		assertEquals(1, actualState);
		actualState = logic.checkLifeCount(4, 1);
		assertEquals(0, actualState);
	}

	/**
	 * 配列状態を次の状態に変換するメソッド
	 */
	public void testChangeState() {
		int block[][] = { { 0, 1, 0 }, { 0, 1, 0 }, { 0, 1, 0 } };

		int actual[][] = logic.changeState(block);

		assertEquals(1, actual[1][0]);
		assertEquals(1, actual[1][1]);
		assertEquals(1, actual[1][2]);

	}

	/**
	 * 緑と赤の配列の和集合を計算するメソッドのテスト
	 */
	public void testBindRedAndGreen() {

		int redBlock[][] = { { 0, 1, 0 }, { 0, 1, 0 }, { 0, 1, 0 } };
		int greenBlock[][] = { { 0, 0, 0 }, { 1, 1, 1 }, { 0, 0, 0 } };

		int bindBlock[][] = logic.bindRedAndGreen(redBlock, greenBlock);
		assertEquals(0, bindBlock[0][0]);
		assertEquals(2, bindBlock[1][1]);
		assertEquals(1, bindBlock[1][0]);
		assertEquals(2, bindBlock[0][1]);
	}

	/**
	 * 和集合の配列を赤と緑の配列に戻すメソッドの計算
	 */
	public void testUnbindBlocks() {
		int bindBlock[][] = { { 0, 2, 0 }, { 1, 2, 1 }, { 0, 2, 0 } };
		int greenBlock[][] = new int[3][3];
		int redBlock[][] = new int[3][3];
		greenBlock = logic.unbindBlocks(bindBlock, LifeGameLogic.GREEN);
		redBlock = logic.unbindBlocks(bindBlock, LifeGameLogic.RED);

		assertEquals(1, greenBlock[1][0]);
		assertEquals(0, greenBlock[1][1]);
		assertEquals(1, greenBlock[1][2]);

		assertEquals(1, redBlock[0][1]);
		assertEquals(1, redBlock[1][1]);
		assertEquals(1, redBlock[2][1]);

	}

	/**
	 * 配列の更新処理
	 */
	@SuppressLint("UseValueOf")
	public void testUpdate() {
		logic.initArray(4, 3);

		List<Integer> lifeList = new ArrayList<Integer>();
		lifeList.add(0);
		lifeList.add(1);
		lifeList.add(0);
		lifeList.add(0);
		lifeList.add(1);
		lifeList.add(0);
		lifeList.add(0);
		lifeList.add(1);
		lifeList.add(0);
		lifeList.add(0);
		lifeList.add(1);
		lifeList.add(0);
		List<Integer> actual = logic.update(lifeList, 4, 3);

		assertEquals(new Integer(1), actual.get(3));
		assertEquals(new Integer(1), actual.get(4));
		assertEquals(new Integer(1), actual.get(5));
		assertEquals(new Integer(0), actual.get(9));
		assertEquals(new Integer(0), actual.get(10));
		assertEquals(new Integer(0), actual.get(11));

	}
}
