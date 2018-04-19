package net.performance.assessment.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import net.performance.assessment.R;
import net.performance.assessment.entity.PersonnelSimpleInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectClassPersonAdapter extends BaseAdapter {

    private List<PersonnelSimpleInfo> mClassPersonList = new ArrayList<>();

    private HashMap<String, Boolean> isSelected;
    private Context mContext;

    public SelectClassPersonAdapter(Context ctx, List<PersonnelSimpleInfo> list) {
        this.mContext = ctx;
        this.mClassPersonList = list;
        isSelected = new HashMap<>();

        initData();
    }

    private void initData() {
        for (PersonnelSimpleInfo info : mClassPersonList) {
            getIsSelected().put(info.id, false);
        }
    }

    public HashMap<String,Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<String,Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int getCount() {
        return mClassPersonList.size();
    }

    @Override
    public Object getItem(int position) {
        return mClassPersonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class_person, null);
            holder.tvName = convertView.findViewById(R.id.tv_class_person_name);
            holder.cb = convertView.findViewById(R.id.cb_select);
            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        PersonnelSimpleInfo info = (PersonnelSimpleInfo) getItem(position);
        if (info != null) {
            holder.tvName.setText(info.name);
            // 根据isSelected来设置checkbox的选中状况
            holder.cb.setChecked(getIsSelected().get(info.id));
        }
        return convertView;
    }

    public class ViewHolder {
        public CheckBox cb;
        public TextView tvName;
    }
}
