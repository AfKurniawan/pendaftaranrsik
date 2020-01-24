package id.rsi.klaten.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import id.rsi.klaten.Activity.MainActivity;
import id.rsi.klaten.Model.Jadwal;
import id.rsi.klaten.rsik.R;


public class AllJadwalAdapter extends RecyclerView.Adapter<AllJadwalAdapter.HolderData>{

    private List<Jadwal> mItems ;
    private Context context;

    public AllJadwalAdapter(Context context, List<Jadwal> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_all_jadwal_item,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        Jadwal jd  = mItems.get(position);
        holder.tvIdJadwal.setText(jd.getId_jadwal());
        holder.tvNamaDokter.setText(jd.getNama_lengkap());
        holder.tvNamaPoli.setText(jd.getNama_poli());
        holder.tvHari.setText (jd.getHari());
        holder.tvJam.setText(jd.getJam());
        holder.tvPilih.setText("Pilih");

        holder.jd = jd;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView tvNamaDokter,tvNamaPoli,tvHari, tvPilih, tvJam, tvIdJadwal;
        LinearLayout btnLayout;
        Jadwal jd;

        public  HolderData (View view)
        {
            super(view);

            tvIdJadwal = (TextView) view.findViewById(R.id.et_id_jadwal);
            tvNamaDokter = (TextView) view.findViewById(R.id.tv_nama_dokter);
            tvNamaPoli = (TextView) view.findViewById(R.id.tv_nama_poli);
            tvHari = (TextView) view.findViewById(R.id.tv_hari);
            tvJam = (TextView) view.findViewById(R.id.tv_jam);
            tvPilih = (TextView) view.findViewById(R.id.tv_pilih);
            btnLayout = view.findViewById(R.id.bt_pilih);
            btnLayout.setVisibility(View.GONE);

            btnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, MainActivity.class);
                    update.putExtra("update",1);
                    update.putExtra("id_jadwal", jd.getId_jadwal());
                   /* update.putExtra("nama_lengkap",jd.getNama_lengkap());
                    update.putExtra("nama_poli",jd.getNama_poli());
                    update.putExtra("hari",jd.getHari());
                    update.putExtra("jam",jd.getJam());*/

                    context.startActivity(update);
                }
            });
        }
    }

}

