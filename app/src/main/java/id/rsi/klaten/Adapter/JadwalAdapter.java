package id.rsi.klaten.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import id.rsi.klaten.Activity.*;
import id.rsi.klaten.Model.Jadwal;
import id.rsi.klaten.Model.LimitDokter;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SharedPrefManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.VolleyLog.TAG;


public class JadwalAdapter extends RecyclerView.Adapter<JadwalAdapter.HolderData>{

    private List<Jadwal> mItems ;
    private Context context;

    public JadwalAdapter (Context context, List<Jadwal> items)
    {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_jadwal_item,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar limitmax = new GregorianCalendar();
        limitmax.add(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String sesuk = df.format(tomorrow);

        holder.etBesok.setText(sesuk);
        //holder.etHariLibur.setVisibility(View.GONE);


        /*Date date = new Date();
        date.setHours(date.getHours());*/

        /*SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("HH:mm");
        holder.etJam.setText(simpDate.format(date));*/








        Jadwal jd = mItems.get(position);

            holder.tvIdJadwal.setText(jd.getId_jadwal());
            holder.tvNamaDokter.setText(jd.getNama_lengkap());
            holder.etKodeDokter.setText(jd.getKd_dokter());
            holder.etKodePoli.setText(jd.getKd_poli());
            holder.tvNamaPoli.setText(jd.getNama_poli());
            holder.tvHari.setText(jd.getHari());
            holder.etPraktek.setText(jd.getPraktek());
            holder.tvJam.setText(jd.getJam());
            holder.etIdDokter.setText(jd.getId_dokter());
            holder.tvPilih.setText("Pilih");



            holder.jd = jd;
        }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class HolderData extends RecyclerView.ViewHolder
    {
        TextView tvHariMinggu, tvNamaDokter, tvNamaPoli, tvIdDokter, tvHari, tvPilih, tvJam, tvIdJadwal;
        EditText etBesok, etIdDokter, etKodeDokter, etKodePoli, etPraktek, etJumlahDokter;
        LinearLayout btnLayout;
        Jadwal jd;

        public  HolderData (View view)
        {
            super(view);

            tvIdJadwal = (TextView) view.findViewById(R.id.et_id_jadwal);
            tvNamaDokter = (TextView) view.findViewById(R.id.tv_nama_dokter);
            tvNamaPoli = (TextView) view.findViewById(R.id.tv_nama_poli);
            tvHari = (TextView) view.findViewById(R.id.tv_hari);
            etBesok = (EditText)view.findViewById(R.id.et_tanggal_besok);
            etJumlahDokter = (EditText)view.findViewById(R.id.et_jumlah_dokter);
            tvJam = (TextView) view.findViewById(R.id.tv_jam);
            etIdDokter = (EditText) view.findViewById(R.id.et_id_dokter);
            etIdDokter.setVisibility(View.GONE);
            tvPilih = (TextView) view.findViewById(R.id.tv_pilih);


            etKodeDokter = (EditText)view.findViewById(R.id.et_kd_dokter);
            etKodeDokter.setVisibility(View.GONE);
            etKodePoli = (EditText)view.findViewById(R.id.et_kd_poli);
            etKodePoli.setVisibility(View.GONE);
            etPraktek  = (EditText)view.findViewById(R.id.et_praktek);
            etPraktek.setVisibility(View.GONE);

            btnLayout = view.findViewById(R.id.bt_pilih);

            btnLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //limitDokter();
                    Intent update = new Intent(context, MainActivity.class);
                    update.putExtra("update",1);
                    update.putExtra("id_jadwal", jd.getId_jadwal());
                    update.putExtra("kd_dokter", jd.getKd_dokter());
                    update.putExtra("kd_poli", jd.getKd_poli());
                    update.putExtra("nama_lengkap",jd.getNama_lengkap());
                    update.putExtra("nama_poli",jd.getNama_poli());
                    update.putExtra("hari",jd.getHari());
                    update.putExtra("praktek", jd.getPraktek());
                    update.putExtra("jam",jd.getJam());
                    update.putExtra("id_dokter", jd.getId_dokter());

                    context.startActivity(update);
                }
            });
        }

        private void limitDokter(){


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.LIMIT_DOKTER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                //getting the whole json object from the response
                                JSONObject obj = new JSONObject(response);


                                JSONArray heroArray = obj.getJSONArray("data");

                                //now looping through all the elements of the json array
                                for (int i = 0; i < heroArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject pasObj = heroArray.getJSONObject(i);

                                    //creating a hero object and giving them the values from json object
                                    LimitDokter limit = new LimitDokter(
                                            pasObj.getString("jml_dokter")


                                    );


                                    etJumlahDokter.setText(limit.getJml_dokter());
                                }




                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // progressBar.setVisibility(View.INVISIBLE);
                            // Toast.makeText(, "Saldo Belum Respon", Toast.LENGTH_LONG).show();
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("tgl_kontrol", etBesok.getText().toString());
                    params.put("id_dokter", etIdDokter.getText().toString());
                    // params.put("password", passwordHolder);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);

            requestQueue.add(stringRequest);

        }

    }

}

