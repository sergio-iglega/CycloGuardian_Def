package com.example.sergi.cycloguardian.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sergi.cycloguardian.Events.SensorEvent;
import com.example.sergi.cycloguardian.MyApplication;
import com.example.sergi.cycloguardian.R;
import com.example.sergi.cycloguardian.Utils.Constants;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;


public class FragmentGraph extends Fragment {

    LineChart mChart;
    View mView;
    int index = 0;
    private  boolean plotData = true;
    ArrayList<Entry> yAXESsen1 = new ArrayList<>();
    ArrayList<Entry> yAXESsen2 = new ArrayList<>();
    Queue<Float> miQueueSen1, miQueueSen2;
    MyApplication myApplication;
    SeekBar seekBar;
    TextView textViewSeek;

    public FragmentGraph() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this); //Registro al bus de evnetos
        myApplication = ((MyApplication)getActivity().getApplication());
        miQueueSen1 = new Queue<Float>() {
            @Override
            public boolean add(Float aFloat) {
                return false;
            }

            @Override
            public boolean offer(Float aFloat) {
                return false;
            }

            @Override
            public Float remove() {
                return null;
            }

            @Override
            public Float poll() {
                return null;
            }

            @Override
            public Float element() {
                return null;
            }

            @Override
            public Float peek() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Float> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Float> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
        miQueueSen2 = new Queue<Float>() {
            @Override
            public boolean add(Float aFloat) {
                return false;
            }

            @Override
            public boolean offer(Float aFloat) {
                return false;
            }

            @Override
            public Float remove() {
                return null;
            }

            @Override
            public Float poll() {
                return null;
            }

            @Override
            public Float element() {
                return null;
            }

            @Override
            public Float peek() {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Float> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Float> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_grafica, container, false);
        textViewSeek = mView.findViewById(R.id.textViewSeek);
        seekBar = mView.findViewById(R.id.seekBar);
        mChart = (LineChart) mView.findViewById(R.id.chart);


        //Set the seek Bar
        seekBar.setMax(6);
        seekBar.incrementProgressBy(1);
        seekBar.setProgress(3);
        textViewSeek.setText(String.valueOf(myApplication.mySession.getLimitOvertaking()) + "m");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progrees, boolean b) {
                float progressD=(float) progrees/2;
                textViewSeek.setText(String.valueOf(progressD) + "m");

                //Delete the las thresholdlin
                mChart.getAxisLeft().getLimitLines().clear();

                //Create a thersholdline
                LimitLine limitThreshold = new LimitLine(progressD, getString(R.string.thershold_line));
                mChart.getAxisLeft().addLimitLine(limitThreshold);
                limitThreshold.setLineWidth(4f);
                limitThreshold.setLineColor(Color.RED);
                limitThreshold.setTextSize(12f);


                //Set into the session
                myApplication.mySession.setLimitOvertaking(progressD);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       // mChart.setOnChartValueSelectedListener((OnChartValueSelectedListener) this.getContext());

        // enable description text
        mChart.getDescription().setEnabled(true);
        mChart.getDescription().setText(getString(R.string.footer_graph));

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);


        //Create a thersholdline
        LimitLine limitThreshold = new LimitLine(Constants.MAX_DISTANCE, getString(R.string.thershold_line));
        limitThreshold.setLineWidth(4f);
        limitThreshold.setLineColor(Color.RED);
        limitThreshold.setTextSize(12f);
        mChart.getAxisLeft().addLimitLine(limitThreshold);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.DEFAULT);
        l.setTextColor(Color.WHITE);

        XAxis xl = mChart.getXAxis();
        xl.setAxisMinimum(0.0f);
        xl.setTypeface(Typeface.DEFAULT);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.DEFAULT);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(3.0f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);




        return mView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Subscripción al evento para la recepción de nuevas distancias
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onEvent(SensorEvent event){
       float dateSen1, dateSen2;
       dateSen1 = event.getSensor1();
       dateSen2 = event.getSensor2();
       miQueueSen1.add(dateSen1);
       miQueueSen2.add(dateSen2);

       //Set the session queue
        //myApplication.mySession.setSensorDatesQueue(miQueueSen1);

        //Set data from the graph
       setData(dateSen1, dateSen2);

    }


    /**
     * Añade la distancias de los dos sensores a la gráfica
     * @param valS1
     * @param valS2
     */
    private  void setData(float valS1, float valS2) {
        //Añadimos los valores al array list
        yAXESsen1.add(new Entry(index, valS1));
        yAXESsen2.add(new Entry(index, valS2));
        index++;

        //Configuramos los LineDataSet
        LineDataSet setSensor1, setSensor2;

        if(mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {  //Los data set ya estan creados
            setSensor1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            setSensor2 = (LineDataSet) mChart.getData().getDataSetByIndex(1);
            setSensor1.setValues(yAXESsen1);
            setSensor2.setValues(yAXESsen2);

            // move to the latest entry
            mChart.moveViewToX(index);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();

            //limit the number of visible entries
            mChart.setVisibleXRangeMaximum(20);

        } else {
            //Create a dataset and give it a type --> setSensor1
            setSensor1 = new LineDataSet(yAXESsen1, getString(R.string.sensor1_footer));
            setSensor1.setAxisDependency(YAxis.AxisDependency.LEFT);
            setSensor1.setColor(ColorTemplate.getHoloBlue());
            setSensor1.setCircleColor(Color.WHITE);
            setSensor1.setLineWidth(2f);
            setSensor1.setCircleRadius(3f);
            setSensor1.setFillAlpha(65);
            setSensor1.setFillColor(ColorTemplate.getHoloBlue());
            setSensor1.setHighLightColor(Color.rgb(244, 117, 117));
            setSensor1.setDrawCircleHole(false);

            //Create a dataset and give it a type --> setSensor2
            setSensor2 = new LineDataSet(yAXESsen2, getString(R.string.sensor2_footer));
            setSensor2.setAxisDependency(YAxis.AxisDependency.LEFT);
            setSensor2.setColor(Color.GREEN);
            setSensor2.setCircleColor(Color.WHITE);
            setSensor2.setLineWidth(2f);
            setSensor2.setCircleRadius(3f);
            setSensor2.setFillAlpha(65);
            setSensor2.setFillColor(Color.RED);
            setSensor2.setDrawCircleHole(false);
            setSensor2.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(setSensor1, setSensor2);
            //data.setValueTextColor(Color.WHITE);
            //data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            mChart.setData(data);
        }
    }




}
