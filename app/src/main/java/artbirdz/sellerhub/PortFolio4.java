package artbirdz.sellerhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vipul Chauhan on 1/7/2017.
 */

public class PortFolio4 extends AppCompatActivity implements View.OnClickListener{
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=100;
    public static final int MEDIA_TYPE_IMAGE=1;
    public static final int RESULT_LOAD_IMG=2;

    private static final String IMAGE_DIRECTORY_NAME="Port folio";

    private Uri fileUri;


    private Button camera,gallery,next;
    private ImageView folioImage;
    String imgDecodableString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.port_folio4);

        camera=(Button)findViewById(R.id.btn_camera);
        gallery=(Button)findViewById(R.id.btn_gallery);
        next=(Button)findViewById(R.id.btn_next);
        folioImage=(ImageView)findViewById(R.id.iv_pf1);

        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_camera){
            captureImage();
        }
        else if(view.getId()==R.id.btn_gallery){
            loadImage();
        }
        else if(view.getId()==R.id.btn_next){
            Intent next= new Intent(PortFolio4.this,PortFolio5.class);
            startActivity(next);
        }
        
    }

    private void loadImage() {
        Intent galleryIntent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent,RESULT_LOAD_IMG);
    }

    private void captureImage() {
        Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri=getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);

        startActivityForResult(intent,CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==CAMERA_CAPTURE_IMAGE_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                previewCapturedImage();
            }
            else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "USER CANCELLED IMAGE CAPTURE", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==RESULT_LOAD_IMG){
            if(resultCode==RESULT_OK){
                Uri selectedImage=data.getData();
                String[] filePathColumn={MediaStore.Images.Media.DATA};
                Cursor cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                folioImage.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));
            }
            else
            {
                Toast.makeText(getApplicationContext(), "ABE image utha le", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
            folioImage.setImageBitmap(bitmap);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    private Uri getOutputMediaFileUri(int mediaTypeImage) {
        return Uri.fromFile(getOutputMediaFile(mediaTypeImage));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                Log.d(IMAGE_DIRECTORY_NAME,"Oops! Failed create"+IMAGE_DIRECTORY_NAME+"directory");
                return  null;
            }
        }
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if(type==MEDIA_TYPE_IMAGE){
            mediaFile=new File(mediaStorageDir.getPath()+File.separator+"IMG"+timeStamp+".jpg");
        }
        else{
            return null;
        }
        return mediaFile;
    }
}
