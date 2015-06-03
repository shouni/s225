package com.example.myapp.image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import com.example.myapp.image.ImageMemoryCache;

class UpdateImageViewTask extends AsyncTask<Void, Void, Bitmap> {

    protected Context context;
    protected String url;
    protected ImageView imageView;

    private String tag;

    public UpdateImageViewTask(Context context, String url, ImageView imageView) {
        this.context = context;
        this.url = url;
        this.imageView = imageView;
        this.tag = imageView.getTag().toString();
    }

    @Override
    public void onPreExecute() {

        if (this.tag.equals(this.imageView.getTag())) {
            Bitmap bitmap = ImageMemoryCache.getImage(url);
            // メモリにキャッシュがある場合はTaskをキャンセルしてImageViewを更新
            if (bitmap != null) {
                this.imageView.setImageBitmap(bitmap);
                this.imageView.setVisibility(View.VISIBLE);
                cancel(true);
                return;
            }
        }
    }

    @Override
    protected Bitmap doInBackground(Void... params) {

        Bitmap bitmap = null;

        // ディスクにキャッシュがなかった場合だけネットワークを通して画像を取得する
        if (bitmap == null) {

            try {
                InputStream input = null;
                input = new URL(url).openStream();

                bitmap = BitmapFactory.decodeStream(input);
                input.close();

            } catch (IOException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
            }

        }
        // メモリキャッシュに入れる
        ImageMemoryCache.setImage(url, bitmap);

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {

        // 画像をセットする
        if (this.tag.equals(this.imageView.getTag())) {
            if (result != null) {
                this.imageView.setImageBitmap(result);
                this.imageView.setVisibility(View.VISIBLE);
            }
        }
    }
}
