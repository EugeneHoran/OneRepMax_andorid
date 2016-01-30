package h.eugene.com.onerepmax;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import h.eugene.com.onerepmax.adapters.HistoryRecyclerAdapter;
import h.eugene.com.onerepmax.adapters.OnRecyclerViewItemClickListener;
import h.eugene.com.onerepmax.adapters.OnRecyclerViewLongItemClickListener;
import h.eugene.com.onerepmax.modelsdata.DateInterface;
import h.eugene.com.onerepmax.modelsdata.LiftDate;
import h.eugene.com.onerepmax.modelsdata.LiftDateHandler;
import h.eugene.com.onerepmax.modelsdata.OneRepMax;
import h.eugene.com.onerepmax.modelsdata.OneRepMaxHandler;

/**
 * {@link DateInterface} Interface List
 * {@link Collections}
 * {@link Comparator}
 */
public class FragmentHistory extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        // Add dates and lifts to single interface list
        final ArrayList<DateInterface> items = new ArrayList<>();
        ArrayList<LiftDate> liftDates = LiftDateHandler.get(getActivity()).getDate();
        ArrayList<OneRepMax> oneRepMaxes = OneRepMaxHandler.get(getActivity()).getORM();
        items.addAll(liftDates);
        items.addAll(oneRepMaxes);
        // Compare the two list to determine their ordering with respect to each other
        Comparator<DateInterface> comparator = new Comparator<DateInterface>() {
            @Override
            public int compare(DateInterface lhs, DateInterface rhs) {
                return lhs.getModelDate().compareTo(rhs.getModelDate());
            }
        };
        // Sorts the given list using the given comparator. The algorithm is stable which means equal elements don't get reordered.
        Collections.sort(items, comparator);
        // Set the adapter and its onClick interface
        final HistoryRecyclerAdapter historyRecyclerAdapter = new HistoryRecyclerAdapter(getActivity(), items);
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recyclerHistory);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(historyRecyclerAdapter);
        historyRecyclerAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Pass the selected OneRepMax to MainActivity via Interface
                OneRepMax oneRepMax = (OneRepMax) items.get(position);
                mCallbacks.historyClicked(oneRepMax);
            }
        });
        historyRecyclerAdapter.setOnItemLongClickListener(new OnRecyclerViewLongItemClickListener() {
            @Override
            public void onItemLongClick(View view, final int position) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                OneRepMax oneRepMax = (OneRepMax) items.get(position);
                                OneRepMaxHandler.get(getActivity()).deleteORM(oneRepMax);
                                historyRecyclerAdapter.removeAt(position);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
            }
        });
        return v;
    }

    /**
     * Interface
     */
    private FragmentCallback mCallbacks;

    public interface FragmentCallback {
        void historyClicked(OneRepMax oneRepMax);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (FragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement FragmentHistory");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
