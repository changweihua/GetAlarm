package net.cmono.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import net.cmono.dtos.SpecialDay;
import net.cmono.getalarm.R;
import net.cmono.utils.D;
import net.cmono.utils.ViewFindUtil;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.flyco.animation.FlipEnter.FlipLeftEnter;
import com.flyco.animation.FlipExit.FlipHorizontalExit;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

public class SpecialDayDialog extends BaseDialog<SpecialDayDialog> {
	private TextView tv_cancel;
	private TextView tv_exit;

	private EditText ed_name;
	private EditText ed_description;
	private DatePicker dp_date;

	private Context mContext;

	public SpecialDayDialog(Context context) {
		super(context);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView() {
		widthScale(0.85f);
		showAnim(new FlipLeftEnter());
		dismissAnim(new FlipHorizontalExit());
		View inflate = View.inflate(context,
				R.layout.dialog_customer_specialday, null);
		tv_cancel = ViewFindUtil.find(inflate, R.id.sdd_cancel);
		tv_exit = ViewFindUtil.find(inflate, R.id.sdd_exit);

		ed_name = ViewFindUtil.find(inflate, R.id.sdd_day_name);
		ed_description = ViewFindUtil.find(inflate, R.id.sdd_day_description);
		dp_date = ViewFindUtil.find(inflate, R.id.sdd_day);

		inflate.setBackgroundDrawable(CornerUtils.cornerDrawable(
				Color.parseColor("#ffffff"), dp2px(5)));

		return inflate;
	}

	@Override
	public void setUiBeforShow() {
		tv_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		tv_exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				// 生成节日表
				List<SpecialDay> list = new ArrayList<SpecialDay>();

				int id = DataSupport.max(SpecialDay.class, "id", int.class) + 1;
//				int y = dp_date.getYear();
				String m = String.format("%02d", dp_date.getMonth() + 1);
				String d = String.format("%02d", dp_date.getDayOfMonth());
				String name = ed_name.getText().toString();
				String description = ed_description.getText().toString();

				SpecialDay day = new SpecialDay(id, m + "" + d, name,
						description);
				list.add(day);
				// 保存
				DataSupport.saveAll(list);

				new D(getContext()).ShowMaterialSimpleConfirmDialog("添加成功");

				dismiss();
			}
		});
	}
}
