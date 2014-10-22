package remoteeyes.bsp.vn.adapter;

import java.util.ArrayList;

import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.asynctask.ImageLoaderAsyncTask;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.model.CommentObject;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<CommentObject> {

	Context context;
	ArrayList<CommentObject> commentList;
	int layoutId;
	
	public CommentAdapter(Context context, int layoutId, ArrayList<CommentObject> commentList) {
		super(context, layoutId);
		this.context = context;
		this.commentList = commentList;
		this.layoutId = layoutId;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		convertView = inflater.inflate(layoutId, null);
		
		ImageView ivAvatarMine = (ImageView)convertView.findViewById(R.id.iv_avatar_mine);
		ImageView ivAvatarFriend = (ImageView)convertView.findViewById(R.id.iv_avatar_friend);
		TextView tvMessageMine = (TextView)convertView.findViewById(R.id.tv_message_mine);
		TextView tvMessageFriend = (TextView)convertView.findViewById(R.id.tv_message_friend);
		LinearLayout llCommentMine = (LinearLayout)convertView.findViewById(R.id.ll_comment_mine);
		LinearLayout llCommentFriend = (LinearLayout)convertView.findViewById(R.id.ll_comment_friend);
		
		CommentObject co = commentList.get(index);
		
		if(co.isMine()){
			llCommentFriend.setVisibility(View.GONE);
			ivAvatarMine.getLayoutParams().width = (int)(Config.screenWidth*0.07);
			ivAvatarMine.getLayoutParams().height = (int)(Config.screenWidth*0.07);
			new ImageLoaderAsyncTask(ivAvatarMine, (int)(Config.screenHeight * 0.07), false).execute(co.getUrlAvatar());
			tvMessageMine.getLayoutParams().width = (int)(Config.screenHeight * 0.3);
			tvMessageMine.setText(co.getMessage());
		} else {
			llCommentMine.setVisibility(View.GONE);
			ivAvatarFriend.getLayoutParams().width = (int)(Config.screenWidth*0.07);
			ivAvatarFriend.getLayoutParams().height = (int)(Config.screenWidth*0.07);
			new ImageLoaderAsyncTask(ivAvatarFriend, (int)(Config.screenHeight * 0.07), false).execute(co.getUrlAvatar());
			tvMessageFriend.getLayoutParams().width = (int)(Config.screenHeight * 0.3);
			tvMessageFriend.setText(co.getMessage());
		}
		
		return convertView;
	}
	
	@Override
    public int getCount() {
        return commentList.size();
    }
	
	@Override
    public CommentObject getItem(int position) {
        return commentList.get(position);
    }
}
