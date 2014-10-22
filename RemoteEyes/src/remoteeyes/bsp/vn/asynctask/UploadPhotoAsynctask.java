package remoteeyes.bsp.vn.asynctask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import remoteeyes.bsp.vn.DetailChallengeActivity;
import remoteeyes.bsp.vn.MyAreaActivity;
import remoteeyes.bsp.vn.R;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import remoteeyes.bsp.vn.common.UploadImageToServer;
import remoteeyes.bsp.vn.type.ShowingChallengeType;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.Toast;

public class UploadPhotoAsynctask extends AsyncTask<String, Void, String> {

	Activity activity;
	String typeFile;
	Bitmap bm;
	String typePhoto;

	public UploadPhotoAsynctask(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			if (((MyAreaActivity) activity) != null) {
				if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_ACCEPTED) {
					new AcceptedChallengeAsynctask((MyAreaActivity) activity)
							.execute(Config.IdUser);
				}
				if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_NEARBY) {
					new CurrentChallengeAsynctask((MyAreaActivity) activity)
							.execute(Config.lat + "", Config.lng + "",
									Config.IdUser,
									ShowingChallengeType.STATUS_SHOW_CURRENT
											+ "");
				}
				if (ShowingChallengeType.STATUS_SHOW_CURRENT == ShowingChallengeType.CHALLENGE_SHOW_GLOBAL) {
					new GlobalChallengeAsynctask((MyAreaActivity) activity)
							.execute(Config.IdUser);
				}
			}
		} catch (Exception e) {
			if (((DetailChallengeActivity) activity).detailChallengeFragment != null) {
				if (typeFile.equals("0")) {
					((DetailChallengeActivity) activity).detailChallengeFragment
							.addReponsePhotoVideo(typeFile);
				}
			}
		}
	}

	@Override
	protected String doInBackground(String... params) {
		typePhoto = params[2];
		String path = params[0];
		String ext = path.substring(path.lastIndexOf("."));
		File file = new File(path);
		UploadImageToServer upload = null;
		UploadImageToServer uploadThumb = null;
		int msec = 0;

		if (ext.equals(".mov") || ext.equals(".mp4")) {

			File f = null;
			Bitmap bm = ThumbnailUtils.createVideoThumbnail(path,
					MediaStore.Images.Thumbnails.MINI_KIND);
			if (bm != null) {
				// create a file to write bitmap data
				String tmp = file.getName();
				String[] name = tmp.split("\\.");
				f = new File(activity.getCacheDir(), "thumb" + name[0] + ".png");
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Convert bitmap to byte array
				Bitmap bitmap = bm;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /*
													 * ignored for PNG
													 */, bos);
				byte[] bitmapdata = bos.toByteArray();
				// write the bytes in file
				try {
					FileOutputStream fos = new FileOutputStream(f);
					fos.write(bitmapdata);
					fos.flush();
					fos.close();
				} catch (Exception e) {
				}
			}
			uploadThumb = new UploadImageToServer("image",
					Config.API_UPLOAD_IMAGE_CHALLENG);
			uploadThumb.uploadFile(f.getPath(), f.getName());
			if (!uploadThumb.urlImage.equals("-1")) {
				msec = MediaPlayer.create(activity,
						Uri.fromFile(new File(path))).getDuration();

				typeFile = "2";
				upload = new UploadImageToServer("fileUpload",
						Config.API_UPLOAD_VIDEO_CHALLENGE);
			}
		} else if (ext.equals(".jpg") || ext.equals(".png")) {
			if (typePhoto.equals("0")) {
				typeFile = "1";
			} else {
				typeFile = "3";
			}
			upload = new UploadImageToServer("image",
					Config.API_UPLOAD_IMAGE_CHALLENG);

		} else {
			activity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							activity,
							activity.getResources().getString(
									R.string.file_not_support),
							Toast.LENGTH_SHORT).show();
					return;
				}
			});
		}
		upload.uploadFile(path, file.getName());
		String result = "";
		if (!upload.urlImage.equals("-1")) {
			JSONParser jsonParser = new JSONParser();
			String pathth = upload.urlImage;
			String thumb = "";
			if (uploadThumb != null) {
				thumb = uploadThumb.urlImage;
			}
			String duration = String.valueOf(msec);
			String url = String.format(Config.API_UPLOAD_CHALLENG, pathth,
					Config.IdUser, params[1], typeFile, thumb, duration);
			result = jsonParser.getStringFromUrl(url);
		}

		return result;
	}
}
