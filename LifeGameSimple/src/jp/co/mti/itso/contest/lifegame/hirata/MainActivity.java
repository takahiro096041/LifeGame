package jp.co.mti.itso.contest.lifegame.hirata;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jp.co.mti.itso.contest.lifegame.hirata.logic.LifeGameLogic;
import jp.co.mti.itso.contest.lifegame.hirata.view.LifeAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity extends Activity {

	public static final String VERTICAL = "vertiacal";
	public static final String HORIZONTAL = "horizontal";
	private final static int TIMER_PERIOD = 250;
	private Timer timer;
	private LifeGameLogic logic;
	private LifeAdapter adapter;
	private List<Integer> lifeList = new ArrayList<Integer>();
	private final Handler handler = new Handler();
	private int horizontal, vertical;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		vertical = intent.getIntExtra(VERTICAL, 0);
		horizontal = intent.getIntExtra(HORIZONTAL, 0);
		((GridView) findViewById(R.id.mainGridView)).setNumColumns(horizontal);

		init(vertical, horizontal);
	}

	private final void init(int x, int y) {
		logic = new LifeGameLogic();
		logic.initArray(x, y);
		Display disp = getWindowManager().getDefaultDisplay();
		for (int i = 0; i < x * y; i++) {
			lifeList.add(0);
		}
		adapter = new LifeAdapter(getApplicationContext(), lifeList,
		        R.drawable.ic_launcher, disp.getWidth() / x);
		GridView gridView = (GridView) findViewById(R.id.mainGridView);
		gridView.setOnItemClickListener(new OnLifeItemClickListener());
		gridView.setAdapter(adapter);
		gridView.invalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/**
	 * スタートボタンをクリックしたときの処理
	 * 
	 * @param v
	 */
	public void onStartBtnClicked(View v) {
		gameStart();
	}

	/**
	 * ゲームの開始処理
	 */
	private final void gameStart() {
		if (timer != null) {
			return;
		}
		logic.setStartTime(System.currentTimeMillis());
		timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						lifeList = logic.update(lifeList, vertical, horizontal);
						adapter.refreshDtoList(lifeList);
					}
				});
			}
		}, TIMER_PERIOD, TIMER_PERIOD);
	}

	/**
	 * ストップボタンをクリックしたときの処理
	 * 
	 * @param v
	 */
	public void onStopBtnClicked(View v) {
		gameStop();
	}

	/**
	 * ゲームのストップ処理
	 */
	private final void gameStop() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	private class OnLifeItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
		        long id) {
			Integer item = (Integer) parent.getAdapter().getItem(position);
			if (item != null) {
				lifeList.set(position, (item + 1) % 3);
				adapter.setChangeItemList(position, item, view);
			}
		}
	}

}
