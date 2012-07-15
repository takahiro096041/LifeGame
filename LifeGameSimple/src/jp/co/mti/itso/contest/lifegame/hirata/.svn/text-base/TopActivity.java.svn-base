package jp.co.mti.itso.contest.lifegame.hirata;

import static jp.co.mti.itso.contest.lifegame.hirata.MainActivity.HORIZONTAL;
import static jp.co.mti.itso.contest.lifegame.hirata.MainActivity.VERTICAL;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TopActivity extends Activity {

	private int verticalCell, horizontalCell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top);
	}

	/**
	 * ゲームへボタンをクリックしたときの処理
	 * 
	 * @param v
	 */
	public void onGameBtnClicked(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		if (!checkNumber()) {
			Toast.makeText(getApplicationContext(), "その値はきついです。",
			        Toast.LENGTH_SHORT);
			return;
		}
		intent.putExtra(VERTICAL, verticalCell);
		intent.putExtra(HORIZONTAL, horizontalCell);
		startActivity(intent);
	}

	/**
	 * 入力値を計算するメソッド
	 * 
	 * @return
	 */
	private boolean checkNumber() {
		// TODO 空文字チェック
		EditText vertical = (EditText) findViewById(R.id.editText1);
		EditText horizontal = (EditText) findViewById(R.id.editText2);
		try {
			verticalCell = Integer.parseInt(vertical.getText().toString());
			horizontalCell = Integer.parseInt(horizontal.getText().toString());
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
