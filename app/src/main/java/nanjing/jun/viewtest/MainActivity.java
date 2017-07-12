package nanjing.jun.viewtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ClockView clockView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clockView = (ClockView) findViewById(R.id.clock);


        ArrayList<TimeDuration> timeDurations = new ArrayList<>();
        timeDurations.add(new TimeDuration(8, 10));
        timeDurations.add(new TimeDuration(11, 12));
        timeDurations.add(new TimeDuration(13, 16));
        clockView.setDurationList(timeDurations);


    }

}
