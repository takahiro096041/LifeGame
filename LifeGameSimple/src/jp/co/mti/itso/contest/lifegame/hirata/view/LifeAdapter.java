package jp.co.mti.itso.contest.lifegame.hirata.view;

import java.util.List;

import jp.co.mti.itso.contest.lifegame.hirata.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class LifeAdapter extends ArrayAdapter<Integer> {
	/**
 * 
 */
	private final List<Integer> itemList;
	private final Context mContext;
	private static final int LAYOUT_ID = R.layout.life_base;
	private final int viewWidth;

	/**
	 * コンストラクタ
	 * 
	 * @param context
	 * @param itemList
	 * @param noneResId
	 */
	public LifeAdapter(final Context context, final List<Integer> itemList,
	        final int noneResId, final int viewWidth) {
		super(context, LAYOUT_ID, itemList);
		this.mContext = context;
		this.itemList = itemList;
		this.viewWidth = viewWidth;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
			        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(LAYOUT_ID, parent, false);
		}
		refreshView(position, view);
		return view;
	}

	private void refreshView(int position, View view) {
		Integer dto = getItem(position);
		ImageView lifeView = (ImageView) view.findViewById(R.id.lifeView);
		if (dto == 0) {
			lifeView.setBackgroundColor(Color.BLACK);
		} else if (dto == 1) {
			lifeView.setBackgroundColor(Color.GREEN);
		} else if (dto == 2) {
			lifeView.setBackgroundColor(Color.RED);
		}
		if (lifeView.getMeasuredWidth() == 0) {
			lifeView.setMinimumWidth(viewWidth);
			lifeView.setMinimumHeight(viewWidth);
		}
		view.invalidate();
	}

	public void refreshDtoList(List<Integer> newItemList) {
		itemList.clear();
		itemList.addAll(newItemList);
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void setChangeItemList(int position, int item, View view) {
		itemList.set(position, changeState(item));
		refreshView(position, view);
	}

	private int changeState(int now) {
		return (now + 1) % 3;
	}

}
