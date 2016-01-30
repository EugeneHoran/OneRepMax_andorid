package h.eugene.com.onerepmax.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import h.eugene.com.onerepmax.R;
import h.eugene.com.onerepmax.modelsdata.DateInterface;
import h.eugene.com.onerepmax.modelsdata.LiftDate;
import h.eugene.com.onerepmax.modelsdata.LiftDateHandler;
import h.eugene.com.onerepmax.modelsdata.OneRepMax;
import h.eugene.com.onerepmax.modelsdata.OneRepMaxHandler;
import h.eugene.com.onerepmax.util.DateCompare;
import h.eugene.com.onerepmax.util.ORMPreferenceManager;

@SuppressLint("SimpleDateFormat")
public class HistoryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_HOLDER_DATE = 0;
    private static final int VIEW_HOLDER_ORM = 1;

    public Context context;
    private ArrayList<DateInterface> list;
    private ORMPreferenceManager ormPreferenceManager;
    private OnRecyclerViewItemClickListener itemClickListener;
    private OnRecyclerViewLongItemClickListener itemLongClickListener;

    public HistoryRecyclerAdapter(Context activity, ArrayList<DateInterface> log) {
        this.context = activity;
        list = log;
        ormPreferenceManager = new ORMPreferenceManager(context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof LiftDate) {
            return VIEW_HOLDER_DATE;
        } else {
            return VIEW_HOLDER_ORM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_DATE:
                return new ViewHolderDate(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_date, parent, false));
            case VIEW_HOLDER_ORM:
                return new ViewHolderORM(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_orm, parent, false));
            default:
                return null;
        }
    }

    public void removeAt(int position) {
        Date date = list.get(position).getModelDate();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, date.getYear());
        cal.set(Calendar.MONTH, date.getMonth());
        cal.set(Calendar.DAY_OF_MONTH, date.getDay());

        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        ArrayList<DateInterface> listCompare = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Calendar cal2 = Calendar.getInstance();
            cal2.set(Calendar.YEAR, list.get(i).getModelDate().getYear());
            cal2.set(Calendar.MONTH, list.get(i).getModelDate().getMonth());
            cal2.set(Calendar.DAY_OF_MONTH, list.get(i).getModelDate().getDay());
            if (cal.equals(cal2)) {
                listCompare.add(list.get(i));
            }
        }

        if (listCompare.size() == 1) {
            LiftDate liftDate = (LiftDate) list.get(position - 1);
            LiftDateHandler.get(context).deleteDate(liftDate);
            list.remove(position - 1);
            notifyItemRemoved(position - 1);
            notifyItemRangeChanged(position - 1, list.size());
        }
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewLongItemClickListener listener) { //Long Click Listener
        this.itemLongClickListener = listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder v, int pos) {
        if (v instanceof ViewHolderDate) {
            ViewHolderDate holder = (ViewHolderDate) v;
            LiftDate liftDate = (LiftDate) list.get(pos);
            holder.initiateHeader(liftDate);
        } else {
            ViewHolderORM holder = (ViewHolderORM) v;
            OneRepMax oneRepMax = (OneRepMax) list.get(pos);
            holder.initiateHistory(oneRepMax);
        }
    }


    /**
     * ViewHolders
     */
    public class ViewHolderDate extends RecyclerView.ViewHolder {
        public TextView date;

        public ViewHolderDate(final View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
        }

        public void initiateHeader(LiftDate item) {
            date.setText(new SimpleDateFormat("MMM dd").format(item.getDate()));
        }
    }

    public class ViewHolderORM extends RecyclerView.ViewHolder {
        public TextView liftName;
        public TextView weightLifted;
        public TextView oneRepMax;
        public TextView time;
        public LinearLayout selector;

        public ViewHolderORM(final View itemView) {
            super(itemView);
            liftName = (TextView) itemView.findViewById(R.id.liftName);
            weightLifted = (TextView) itemView.findViewById(R.id.weightLifted);
            oneRepMax = (TextView) itemView.findViewById(R.id.oneRepMax);
            time = (TextView) itemView.findViewById(R.id.time);
            selector = (LinearLayout) itemView.findViewById(R.id.selector);
        }

        public void initiateHistory(OneRepMax item) {
            liftName.setText(item.getLiftName());
            weightLifted.setText(String.format("%s %s for %s Reps", item.getWeightLifted(), ormPreferenceManager.settingsGetUnits(), item.getRepsPerformed()));
            oneRepMax.setText(String.format("%s %s", item.getOneRepMax(), ormPreferenceManager.settingsGetUnits()));
            time.setText(new SimpleDateFormat("hh:mm:ss a").format(item.getDate()));
            selector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
            selector.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (itemLongClickListener != null) {
                        itemLongClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }
    }
}
