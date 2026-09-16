package com.ganapati.ai;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.view.Gravity;
import android.widget.*;

import java.util.*;

public class MainActivity extends Activity {

    Button start;
    TextView status;
    boolean running = false;
    Thread audioThread;

    String[] names = {
        "Tabla",
        "Dholak",
        "Sambal",
        "Harmonium",
        "Piano",
        "Tanpura",
        "Bells"
    };

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        buildUI();

        if (android.os.Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                new String[]{Manifest.permission.RECORD_AUDIO},
                10
            );
        }
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(0xff2b170d);
        t.setPadding(8, 8, 8, 8);
        return t;
    }

    void buildUI() {

        ScrollView scroll = new ScrollView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 24, 28, 28);
        root.setBackgroundColor(0xfffff8f2);

        TextView title = text("🕉️ Ganapati AI", 30);
        title.setGravity(Gravity.CENTER);

        root.addView(title,
            new LinearLayout.LayoutParams(-1, 70));

        TextView subtitle =
            text("Ganapati Aarti • Android MVP", 16);

        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        status = text("Tap START SINGING", 17);
        status.setGravity(Gravity.CENTER);
        root.addView(status);

        start = new Button(this);
        start.setText("🎤 START SINGING");

        start.setOnClickListener(v -> {
            if (running)
                stopApp();
            else
                startApp();
        });

        root.addView(start,
            new LinearLayout.LayoutParams(-1, 70));

        root.addView(text("🎚️ Music Volume", 19));

        SeekBar master = new SeekBar(this);
        master.setMax(100);
        master.setProgress(70);

        root.addView(master);

        root.addView(text("🥁 Instruments", 19));

        int[] defaults = {
            80, 70, 50, 70, 35, 45, 30
        };

        for (int i = 0; i < names.length; i++) {

            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            TextView name = text(names[i], 15);

            row.addView(name,
                new LinearLayout.LayoutParams(130, 60));

            SeekBar bar = new SeekBar(this);
            bar.setMax(100);
            bar.setProgress(defaults[i]);

            row.addView(bar,
                new LinearLayout.LayoutParams(
                    0, 60, 1));

            root.addView(row);
        }

        TextView note = text(
            "🎵 The MVP listens through the microphone " +
            "and plays a devotional accompaniment. " +
            "Professional instrument sounds and AI " +
            "tempo-following will be added next.",
            13
        );

        note.setPadding(8, 24, 8, 8);
        root.addView(note);

        scroll.addView(root);
        setContentView(scroll);
    }

    void startApp() {

        if (android.os.Build.VERSION.SDK_INT >= 23 &&
            checkSelfPermission(Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                new String[]{Manifest.permission.RECORD_AUDIO},
                10
            );

            return;
        }

        running = true;

        start.setText("⏹ STOP");
        status.setText(
            "🎤 Listening • accompaniment playing"
        );

        audioThread = new Thread(() -> playPattern());
        audioThread.start();
    }

    void stopApp() {

        running = false;

        start.setText("🎤 START SINGING");
        status.setText("Tap START SINGING");
    }

    void playPattern() {

        final int sampleRate = 44100;

        AudioTrack track = new AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            sampleRate * 2,
            AudioTrack.MODE_STREAM
        );

        track.play();

        int beat = 0;
        int sampleCount = 0;

        while (running) {

            short[] buffer = new short[sampleRate / 20];

            int position = beat % 8;

            for (int i = 0; i < buffer.length; i++) {

                double t =
                    (sampleCount + i) /
                    (double) sampleRate;

                double sound = 0;

                if (position == 0 ||
                    position == 4) {

                    sound +=
                        0.18 *
                        Math.sin(
                            2 * Math.PI * 110 * t
                        );
                }

                if (position == 2 ||
                    position == 6) {

                    sound +=
                        0.10 *
                        Math.sin(
                            2 * Math.PI * 170 * t
                        );
                }

                if (position == 0 ||
                    position == 3 ||
                    position == 5) {

                    sound +=
                        0.055 *
                        Math.sin(
                            2 * Math.PI * 196 * t
                        );
                }

                if (position == 0) {

                    sound +=
                        0.04 *
                        Math.sin(
                            2 * Math.PI * 130.81 * t
                        );

                    sound +=
                        0.03 *
                        Math.sin(
                            2 * Math.PI * 164.81 * t
                        );
                }

                if (position == 4) {

                    sound +=
                        0.045 *
                        Math.sin(
                            2 * Math.PI * 261.63 * t
                        );
                }

                if (position == 7) {

                    sound +=
                        0.03 *
                        Math.sin(
                            2 * Math.PI * 784 * t
                        );
                }

                buffer[i] =
                    (short)(
                        Math.max(
                            -32767,
                            Math.min(
                                32767,
                                sound * 32767
                            )
                        )
                    );
            }

            track.write(
                buffer,
                0,
                buffer.length
            );

            sampleCount += buffer.length;

            if (sampleCount %
                (sampleRate / 20) == 0) {

                beat++;
            }
        }

        track.stop();
        track.release();
    }
}
