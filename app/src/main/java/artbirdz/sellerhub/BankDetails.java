package artbirdz.sellerhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static artbirdz.sellerhub.R.styleable.NavigationView;
import static artbirdz.sellerhub.R.styleable.Toolbar;

public class BankDetails extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE=100;
    public static final int MEDIA_TYPE_IMAGE=1;
    public static final int RESULT_LOAD_IMG=2;

    private static final String IMAGE_DIRECTORY_NAME="Bank Cheque/Passbook";

    private Uri fileUri;

    private Button camera,gallery,next;
    private ImageView cheque;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        camera=(Button)findViewById(R.id.btn_camera);
        gallery=(Button)findViewById(R.id.btn_gallery);
        next=(Button)findViewById(R.id.btn_next);
        cheque=(ImageView)findViewById(R.id.iv_cheque);

        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);
        next.setOnClickListener(this);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bank_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.portFolio) {
            Intent next= new Intent(BankDetails.this,PortFolio1.class);
            startActivity(next);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            Intent next= new Intent(BankDetails.this,PortFolio1.class);
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
               cheque.setImageBitmap(BitmapFactory
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
            cheque.setImageBitmap(bitmap);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public Uri getOutputMediaFileUri(int mediaTypeImage) {
        return Uri.fromFile(getOutputMediaFile(mediaTypeImage));
    }

    private static File getOutputMediaFile(int type) {
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

