package mmu.edu.my.shift;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder> {
    private Context mCtx;
    private List<Jobs> jobsList;

    public JobsAdapter(Context mCtx, List<Jobs> jobsList) {
        this.mCtx=mCtx;
        this.jobsList=jobsList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobViewHolder(
                LayoutInflater.from(mCtx).inflate(R.layout.layout_display,parent,false)
        );
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

    class JobViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewDesc, textViewWage, textViewInfo;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.textview_name);
            textViewLocation=itemView.findViewById(R.id.textview_location);
            textViewDesc=itemView.findViewById(R.id.textview_desc);
            textViewWage=itemView.findViewById(R.id.textview_wage);
            textViewInfo=itemView.findViewById(R.id.textview_info);
        }
    }
}
