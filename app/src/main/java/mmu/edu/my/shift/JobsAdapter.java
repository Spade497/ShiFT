package mmu.edu.my.shift;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder>{
    private Context mCtx;
    private List<Jobs> jobsList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public JobsAdapter(Context mCtx, List<Jobs> jobsList) {
        this.mCtx=mCtx;
        this.jobsList=jobsList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_display, parent, false);
        JobViewHolder evh = new JobViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Jobs jobs=jobsList.get(position);
        holder.textViewName.setText(jobs.getName());
        holder.textViewLocation.setText(jobs.getDistrict()+", "+jobs.getState());
        holder.textViewDesc.setText(jobs.getDesc());
        holder.textViewWage.setText(jobs.getWage());
        holder.textViewInfo.setText("Contact info: "+jobs.getInfo());
    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }


    static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewDesc, textViewWage, textViewInfo;
        public ImageView mDeleteImage;

        public JobViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textview_name);
            textViewLocation = itemView.findViewById(R.id.textview_location);
            textViewDesc = itemView.findViewById(R.id.textview_desc);
            textViewWage = itemView.findViewById(R.id.textview_wage);
            textViewInfo = itemView.findViewById(R.id.textview_info);
            mDeleteImage = itemView.findViewById(R.id.delete_icon);

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
