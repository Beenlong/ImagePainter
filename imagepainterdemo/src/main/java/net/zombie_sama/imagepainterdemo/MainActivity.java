package net.zombie_sama.imagepainterdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import net.zombie_sama.imagepainter.PaintableImageView;

public class MainActivity extends AppCompatActivity {
    private PaintableImageView piv;
    private int strokeWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        piv = (PaintableImageView) findViewById(R.id.piv);
        findViewById(R.id.color_black).setOnClickListener(onClick);
        findViewById(R.id.color_white).setOnClickListener(onClick);
        findViewById(R.id.button).setOnClickListener(onClick);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        strokeWidth = (int) (piv.getPaintStrokeWidth() - 1);
        seekBar.setProgress(strokeWidth);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                strokeWidth = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                strokeWidth = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                piv.setPaintStrokeWidth(seekBar.getProgress() + 1);
            }
        });
    }

    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.color_black:
                    piv.setPaintColor(Color.BLACK);
                    Toast.makeText(MainActivity.this, "BLACK", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.color_white:
                    piv.setPaintColor(Color.WHITE);
                    Toast.makeText(MainActivity.this, "WHITE", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button:
                    piv.reset();
                    break;
            }
        }
    };
}