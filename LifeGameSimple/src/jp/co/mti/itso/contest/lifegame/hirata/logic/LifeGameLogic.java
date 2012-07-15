package jp.co.mti.itso.contest.lifegame.hirata.logic;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

/**
 * 
 * @author hirata_ta
 * 
 */
public final class LifeGameLogic {

	public LifeGameLogic() {
		isDeath = false;
		isStateUnchanged = false;
	}

	/**
	 * 計算する配列の末端の定数
	 */
	private static final int ARRAY_LENGTH = 2;
	/**
	 * 死の状態
	 */
	public static final int DEATH = 0;
	/**
	 * 緑の状態
	 */
	public static final int GREEN = 1;
	/**
	 * 生の状態
	 */
	public static final int LIVE = 1;
	/**
	 * 赤の状態
	 */
	public static final int RED = 2;

	// ブロックは赤と緑それぞれで持つ
	// 0:死　1:生
	// 混合配列に関しては0:死　1:緑 2:赤
	/**
	 * ゲーム開始時間
	 */
	private long startTime;
	/**
	 * 前回のブロック
	 */
	private int lastBlock[][];
	/**
	 * 二回前のブロック
	 */
	private int lastBeforeBlock[][];
	/**
	 * ボード上が死んでいるかのフラグ
	 */
	private boolean isDeath;
	/**
	 * 状態の変化が起きているかのフラグ
	 */
	private boolean isStateUnchanged;

	/**
	 * block[x][y]の 周りのライフの数を数えるメソッド
	 * 
	 * @param block
	 * @return counter
	 */
	int lifeCount(int block[][], int x, int y) {
		int counter = 0;
		counter -= block[x][y];// 始めに自分自身を引いておく
		int xLength = block.length;// x
		int yLength = block[0].length;// y
		int xCount = ARRAY_LENGTH;
		int yCount = ARRAY_LENGTH;
		// 端っこの判定
		// 端っこの場合は計算範囲を狭めている
		if (x < 1) {
			x++;
			xCount--;
		} else if (x >= xLength - 1) {
			xCount--;
		}
		if (y < 1) {
			y++;
			yCount--;
		} else if (y >= yLength - 1) {
			yCount--;
		}
		// ライフカウントの計算
		for (int i = x - 1; i < x + xCount; i++) {
			for (int j = y - 1; j < y + yCount; j++) {
				counter += block[i][j];
			}
		}
		return counter;
	}

	/**
	 * 周りのライフの数をもとに生死を判定するメソッド
	 * 
	 * @param lifecount
	 * @param self
	 */
	int checkLifeCount(int lifecount, int self) {
		int retState;
		switch (lifecount) {
		case 3:// ３は必ず生きる
			retState = 1;
			break;
		case 2:// ２は変化なし
			retState = self;
			break;
		default:// それ以外は死ぬ
			retState = 0;
		}
		return retState;
	}

	/**
	 * 次の状態を返すメソッド
	 * 
	 * @param block
	 * @return
	 */
	int[][] changeState(int[][] block) {
		// 返却用配列の生成
		int[][] changedBlock = new int[block.length][block[0].length];
		for (int i = 0; i < block.length; i++) {
			for (int j = 0; j < block[0].length; j++) {
				// 各要素に対して計算して配列に代入
				int lifeCount = lifeCount(block, i, j);
				changedBlock[i][j] = checkLifeCount(lifeCount, block[i][j]);
			}
		}
		return changedBlock;
	}

	/**
	 * 赤と緑の結合した配列を返す
	 * 
	 * @param redBlock
	 * @param greenBlock
	 * @return　bindBlock
	 */
	int[][] bindRedAndGreen(int[][] redBlock, int[][] greenBlock) {
		int bindBlock[][] = new int[redBlock.length][redBlock[0].length];
		for (int i = 0; i < redBlock.length; i++) {
			for (int j = 0; j < redBlock[0].length; j++) {
				// 各要素に対して計算して配列に代入
				if (redBlock[i][j] == 0) {
					bindBlock[i][j] = greenBlock[i][j];
				} else {// 赤が勝つので２を代入
					bindBlock[i][j] = redBlock[i][j] * 2;
				}
			}
		}
		return bindBlock;
	}

	/**
	 * 結合配列をそれぞれの色の配列に直すメソッド
	 * 
	 * @param bindBlock
	 * @param state
	 * @return
	 */
	int[][] unbindBlocks(int[][] bindBlock, int state) {
		int unbindBlock[][] = new int[bindBlock.length][bindBlock[0].length];
		for (int i = 0; i < bindBlock.length; i++) {
			for (int j = 0; j < bindBlock[0].length; j++) {
				if (bindBlock[i][j] == state) {
					unbindBlock[i][j] = LIVE;
				}
			}
		}
		return unbindBlock;
	}

	/**
	 * リストを更新するメソッド
	 * 
	 * @param lifeList
	 * @param horizontal
	 * @param vertical
	 * @return
	 */
	public List<Integer> update(List<Integer> lifeList, int horizontal,
	        int vertical) {
		// リストを配列に変更
		int block[][] = new int[horizontal][vertical];
		int counter = 0;
		for (int i = 0; i < horizontal; i++) {
			for (int j = 0; j < vertical; j++) {
				block[i][j] = lifeList.get(counter);
				counter++;
			}
		}
		// 赤と緑の配列に変更
		int green[][] = new int[horizontal][vertical];
		int red[][] = new int[horizontal][vertical];
		green = unbindBlocks(block, GREEN);
		red = unbindBlocks(block, RED);
		// それぞれの配列を更新
		green = changeState(green);
		red = changeState(red);
		// 結合配列の計算
		int bindBlock[][] = new int[horizontal][vertical];
		bindBlock = bindRedAndGreen(red, green);
		// 保持している配列要素を更新
		updateArrays(bindBlock);
		// 状態変化のチェックをしてログ出力
		outputLog(bindBlock);
		// 返却リストの作成
		List<Integer> returnList = new ArrayList<Integer>();
		for (int i = 0; i < horizontal; i++) {
			for (int j = 0; j < vertical; j++) {
				returnList.add(bindBlock[i][j]);
			}
		}
		return returnList;

	}

	private void outputLog(int[][] bindBlock) {
		if (checkLiveState(bindBlock)) {
			Log.d("LifeGame", "All Dead "
			        + (System.currentTimeMillis() - startTime) + "ミリ秒");
			isDeath = true;
		}

		if (checkStateBefore(bindBlock)) {
			Log.d("LifeGame",
			        "state is same before last "
			                + (System.currentTimeMillis() - startTime) + "ミリ秒");
			isStateUnchanged = true;
		}
	}

	private boolean checkStateBefore(int bindBlock[][]) {
		if (isStateUnchanged) {
			return false;
		}
		boolean ret = true;
		for (int i = 0; i < bindBlock.length; i++) {
			for (int j = 0; j < bindBlock[0].length; j++) {
				if (bindBlock[i][j] != lastBeforeBlock[i][j]) {
					ret = false;
					break;
				}
				if (!ret) {
					break;
				}
			}
		}
		return ret;
	}

	private boolean checkLiveState(int bindBlock[][]) {
		if (isDeath) {
			return false;
		}
		boolean ret = true;
		for (int[] array : bindBlock) {
			for (int element : array) {
				if (element != 0) {
					ret = false;
					break;
				}
			}
			if (!ret) {
				break;
			}
		}
		return ret;
	}

	private void updateArrays(int[][] bindBlock) {
		lastBeforeBlock = lastBlock;
		lastBlock = bindBlock;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * 保持配列の初期化
	 * 
	 * @param x
	 * @param y
	 */
	public void initArray(int x, int y) {
		lastBlock = new int[x][y];
		lastBeforeBlock = new int[x][y];
	}

}
