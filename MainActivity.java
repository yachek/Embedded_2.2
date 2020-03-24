package com.example.es_22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static int n = 8;
    private static int N = 256;
    private static double W = 1100;
    private double[] signal = new double[N];
    private double real1;
    private double real2;
    private double imagine1;
    private double imagine2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(generateSignal(signal));
        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(fastFourier());
        GraphView graph = findViewById(R.id.graph1);
        customizationGraph(graph, series1, -5, 6);
        graph = findViewById(R.id.graph2);
        customizationGraph(graph, series2, 0, 70);
    }

    private DataPoint[] generateSignal(double[] res) {
        double phi;
        double A;
        double x;
        DataPoint[] data = new DataPoint[N];
        Random rnd = new Random();
        for (int i = 0; i < N; i++) {
            phi = rnd.nextDouble();
            A = rnd.nextDouble();
            x = 0.0;
            for (int j = 0; j < n; j++) {
                x += A * Math.sin(W / (j + 1) * i + phi);
            }
            res[i] = x;
            data[i] = new DataPoint(i, x);
        }
        return data;
    }

    private DataPoint[] fastFourier() {
        DataPoint[] res = new DataPoint[N];
        for (int p = 0; p < N; p++) {
            double temp = 4 * Math.PI * p / N;
            real1 = real2 = imagine1 = imagine2 = 0;
            for (int k = 0; k < N / 2 - 1; k++) {
                double tmp = 4 * Math.PI * p * k / N;
                real1 += signal[2*k] * Math.cos(tmp);
                imagine1 += signal[2*k] * Math.sin(tmp);
                real2 += signal[2*k+1] * Math.cos(tmp);
                imagine2 += signal[2*k+1] * Math.sin(tmp);
            }
            if (p < N / 2) {
                res[p] = new DataPoint(p, Math.sqrt(Math.pow((real2
                        + real1 * Math.cos(temp)), 2) + Math.pow((imagine2
                        + imagine1 * Math.sin(temp)), 2)));
            } else {
                res[p] = new DataPoint(p, Math.sqrt(Math.pow((real2
                        - real1 * Math.cos(temp)), 2) + Math.pow((imagine2
                        - imagine1 * Math.sin(temp)), 2)));
            }
        }
        return res;
    }

    private void customizationGraph(GraphView graph, LineGraphSeries line, int miny, int maxy) {
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(30);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(maxy);
        graph.getViewport().setMinY(miny);
        // enable scrolling
        graph.getViewport().setScrollable(true);

        graph.addSeries(line);
    }

}
