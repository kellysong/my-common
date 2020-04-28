package cn.feng.skin.manager.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;

public class BaseSkinFragment extends Fragment implements IDynamicNewView{
	
	private IDynamicNewView mIDynamicNewView;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		try{
			mIDynamicNewView = (IDynamicNewView)context;
		}catch(ClassCastException e){
			mIDynamicNewView = null;
		}
	}

	@Override
	public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
		if(mIDynamicNewView == null){
			throw new RuntimeException("IDynamicNewView should be implements !");
		}else{
			mIDynamicNewView.dynamicAddView(view, pDAttrs);
		}
	}
	public void dynamicAddView(View view, String attrName, int attrValueResId) {
		List<DynamicAttr> pDAttrs = new ArrayList<>();
		pDAttrs.add(new DynamicAttr(attrName, attrValueResId));
		dynamicAddView(view, pDAttrs);
	}

	public LayoutInflater getLayoutInflater(Bundle savedInstanceState) {
		LayoutInflater result = getActivity().getLayoutInflater();
		return result;
	}
}
