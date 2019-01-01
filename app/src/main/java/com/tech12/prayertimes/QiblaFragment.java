package com.tech12.prayertimes;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
/*import android.graphics.drawable.Animatable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
*/
import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class QiblaFragment extends Fragment implements SensorEventListener {
    private ImageView imageView;
    private SensorManager sensorManager;

    public QiblaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_qibla, container, false);
        imageView =(ImageView) view.findViewById(R.id.compass);
        sensorManager=(SensorManager) getActivity().getSystemService(SENSOR_SERVICE);

        return view;
    }


    // private ImageView imageView;
    private float[] mGravity=new float[3];
    private float[] mGeomagnatic=new float[3];
    private float azimuth=0f;
    private float currectAzimuth=0f;
    //private SensorManager sensorManager;


    @Override
    public void onPause()
    {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        ((HomeActivity) getActivity()).setActionBarTitle("Qibla");
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha=0.97f;
        synchronized (this){
            if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER)
            {
                mGravity[0]=alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1]=alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2]=alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }

            if(sensorEvent.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD)
            {
                mGeomagnatic[0]=alpha*mGeomagnatic[0]+(1-alpha)*sensorEvent.values[0];
                mGeomagnatic[1]=alpha*mGeomagnatic[1]+(1-alpha)*sensorEvent.values[1];
                mGeomagnatic[2]=alpha*mGeomagnatic[2]+(1-alpha)*sensorEvent.values[2];
            }
            float R[]=new float[9];
            float I[]=new float[9];
            boolean success=SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnatic);
            if(success)
            {
                float orientation[]=new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth=(float)Math.toDegrees(orientation[0]);
                azimuth=(azimuth+360)%360;

                Animation anim=new RotateAnimation(-currectAzimuth,-azimuth,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                currectAzimuth=azimuth;
                anim.setDuration(500);
                anim.setRepeatCount(0);
                anim.setFillAfter(true);
                imageView.startAnimation(anim);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
