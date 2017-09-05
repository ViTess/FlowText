package vite.flowtext;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by trs on 17-8-11.
 */

public class TestFlowActivity extends Activity {

    FlowTextLayout layout;
    Button btn_add;

    Random mRandom = new Random();
    String[] mTextArray = new String[]{"asd", "wcasdw", "pasojdp asid", "sd", "[wpasndasd", "sdsdw", "p[apsojnw"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        layout = (FlowTextLayout) findViewById(R.id.flow_layout);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void add() {
        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_flow, layout, false);
        tv.setText(mTextArray[mRandom.nextInt(mTextArray.length)]);
        layout.addView(tv);
    }
}
