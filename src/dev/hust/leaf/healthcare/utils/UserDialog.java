package dev.hust.leaf.healthcare.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.model.TerUser;
import dev.hust.leaf.healthcare.model.TotalUser;
import dev.hust.leaf.healthcare.model.User;

public class UserDialog {

	private Context context;

	private TextView tv_username;
	private TextView tv_realname;
	private TextView tv_sex;
	private TextView tv_email;
	private TextView tv_phone;
	private TextView tv_type;
	private TextView tv_birthday;
	private TextView tv_identifyID;
	private TextView tv_address;
	private TextView tv_career;
	private TextView tv_comment;
	private ImageView img_avatar;
	private Bitmap bm;

	public UserDialog(Context context) {
		this.context = context;
	}

	public void show(TotalUser user) {
		LayoutInflater inflaterDl = LayoutInflater.from(context);
		View layout = inflaterDl.inflate(R.layout.layout_user_fragment, null);

		Dialog dialog = new Dialog(context);

		dialog.setTitle("œÍœ∏–≈œ¢");
		dialog.setContentView(layout);

		display(layout, user.getUser(), user.getTerUser());
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (bm != null) {
					bm.recycle();
				}

			}

		});
		dialog.show();
	}

	private void display(View a, User user, TerUser teruser) {
		a.findViewById(R.id.progress_bar).setVisibility(View.GONE);
		View layout_content = a.findViewById(R.id.layout_content);
		layout_content.setVisibility(View.VISIBLE);
		layout_content
				.setBackgroundResource(R.color.abs__background_holo_light);
		tv_username = (TextView) a.findViewById(R.id.tv_username);
		tv_realname = (TextView) a.findViewById(R.id.tv_realname);
		tv_sex = (TextView) a.findViewById(R.id.tv_sex);
		tv_email = (TextView) a.findViewById(R.id.tv_email);
		tv_phone = (TextView) a.findViewById(R.id.tv_phone);
		tv_type = (TextView) a.findViewById(R.id.tv_type);
		tv_birthday = (TextView) a.findViewById(R.id.tv_birthday);
		tv_identifyID = (TextView) a.findViewById(R.id.tv_identifyID);
		tv_address = (TextView) a.findViewById(R.id.tv_address);
		tv_career = (TextView) a.findViewById(R.id.tv_career);
		tv_comment = (TextView) a.findViewById(R.id.tv_comment);
		img_avatar = (ImageView) a.findViewById(R.id.img_avatar);

		if (user != null) {
			tv_username.setText(user.getUserName());
			tv_realname.setText(user.getRealName());
			tv_sex.setText(user.getSexString());
			tv_email.setText(user.getEmail());
			tv_phone.setText(user.getPhone());
			tv_type.setText(user.getUserTypeString());
			byte[] b = user.getPhoto();
			if (b != null) {
				bm = BitmapFactory.decodeByteArray(b, 0, b.length);
				img_avatar.setImageBitmap(bm);
			}
		}
		if (teruser != null) {
			tv_birthday.setText(teruser.getBirthDt().trim().split(" ")[0]);
			tv_identifyID.setText(teruser.getIdentityCode());
			tv_address.setText(teruser.getAddress());
			tv_career.setText(teruser.getOccupation());
			tv_comment.setText(teruser.getMemo());
		} else {
			a.findViewById(R.id.row_identifyID).setVisibility(View.GONE);
			a.findViewById(R.id.row_address).setVisibility(View.GONE);
			a.findViewById(R.id.row_birthday).setVisibility(View.GONE);
			a.findViewById(R.id.row_career).setVisibility(View.GONE);
			a.findViewById(R.id.row_comment).setVisibility(View.GONE);
		}
	}
}
