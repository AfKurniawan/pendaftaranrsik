package id.rsi.klaten.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import net.glxn.qrgen.android.QRCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.rsi.klaten.Activity.DetailHistoryActivity;
import id.rsi.klaten.Activity.HistoryListActivity;
import id.rsi.klaten.Model.Booking;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.rsik.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HolderData> {

    private List<Booking> mItems;
    private Context context;
    LinearLayout linearLayout;
    ImageButton btnDelete;

    public HistoryAdapter(Context context, List<Booking> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_booking_item,parent,false);
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.HolderData holder, int position) {
        Booking bk = mItems.get(position);


        //holder.etStatusDelete.setText("0");
        holder.tvNamaPasien.setText(bk.getNama_pasien());
        holder.etIdBooking.setText(bk.getId_booking());
        holder.tvNoAntrian.setText(bk.getNo_antrian());

        if (bk.getNo_barcode().equals("")){

            holder.etNoBarcode.setText("Kosong");
            String stNomor = holder.etNoBarcode.getText().toString();
            //Bitmap myBitmap = QRCode.from(stNomor).bitmap();
            //holder.ivQrCode.setImageBitmap(myBitmap);
            Drawable d = context.getResources().getDrawable(android.R.drawable.ic_menu_camera);
            holder.ivQrCode.setImageDrawable(d);

        } else {

            holder.etNoBarcode.setText(bk.getNo_barcode());
            holder.etNoBarcode.setVisibility(View.GONE);
            String stNomor = holder.etNoBarcode.getText().toString();
            Bitmap myBitmap = QRCode.from(stNomor).bitmap();
            holder.ivQrCode.setImageBitmap(myBitmap);
        }

        holder.tvNoRm.setText(bk.getNo_rm());
        holder.tvTglBooking.setText(bk.getTgl_booking());
        holder.tvNamaPoli.setText(bk.getNama_poli());
        holder.tvNamaDokter.setText(bk.getNama_lengkap());
        holder.tvHari.setText(bk.getHari()+", "+ bk.getJam());
        holder.tvPenjamin.setText(bk.getPenjamin());

        if(bk.getPenjamin().equals("Pilih Penjamin")){

            holder.tvPenjamin.setVisibility(View.GONE);
            holder.tvJnsdaftar.setText("Poli " + bk.getJns_daftar());

        } else {

            holder.tvPenjamin.setVisibility(View.VISIBLE);
            holder.tvJnsdaftar.setVisibility(View.GONE);
        }




        holder.bk = bk;







    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }



    class HolderData extends RecyclerView.ViewHolder {
        TextView tvNamaPasien, tvNoRm, tvTglBooking, tvJam, tvHari, tvNoAntrian,  tvNamaPoli, tvNamaDokter, tvPenjamin, tvJnsdaftar;
        EditText etStatusDelete, etIdBooking, etNoBarcode;
        ImageView ivQrCode;
        LinearLayout btnLayout;
        Booking bk;
        private int position;

        public HolderData(View view) {


            super(view);

            ivQrCode = (ImageView)view.findViewById(R.id.iv_qrcode);

            etStatusDelete = view.findViewById(R.id.et_status_del);
            etStatusDelete.setText("0");
            etStatusDelete.setVisibility(View.GONE);

            etIdBooking = view.findViewById(R.id.et_id_booking);
            etIdBooking.setVisibility(View.GONE);

            tvNamaPasien = (TextView) view.findViewById(R.id.tv_nama_pasien);
            tvNoRm = (TextView)view.findViewById(R.id.tv_no_rm);
            //tvPenjamin = (TextView) view.findViewById(R.id.tv_penjamin);

            tvJnsdaftar = view.findViewById(R.id.tv_jns_daftar);
            tvNoAntrian = (TextView) view.findViewById(R.id.tv_no_antrian);

            etNoBarcode = (EditText) view.findViewById(R.id.et_no_barcode);

            tvNamaPoli = (TextView) view.findViewById(R.id.tv_nama_poli);
            tvNamaDokter = (TextView) view.findViewById(R.id.tv_nama_dokter2);

            tvTglBooking = (TextView)view.findViewById(R.id.tv_tgl_booking);
            tvHari = (TextView)view.findViewById(R.id.tv_history_hari);
            tvJam = (TextView)view.findViewById(R.id.tv_history_jam);
            tvPenjamin = (TextView)view.findViewById(R.id.tv_penjamin);

            btnDelete = view.findViewById(R.id.bt_delete);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    new AlertDialog.Builder(context)
                            .setTitle("Hapus")
                            .setMessage("Anda yakin akan menghapus pendaftaran pasien: "+ tvNamaPasien.getText()+" ?")
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {


                                    updateStatusBooking();

                                    Intent intent = new Intent(context, HistoryListActivity.class);
                                    context.startActivity(intent);
                                    ((Activity)context).finish();



                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                }
            });






            linearLayout = view.findViewById(R.id.lyt_parent);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, DetailHistoryActivity.class);
                    update.putExtra("update",1);
                    update.putExtra("no_antrian", bk.getNo_antrian());
                    update.putExtra("no_barcode", bk.getNo_barcode());
                    update.putExtra("tgl_booking", bk.getTgl_booking());
                    update.putExtra("nama_pasien", bk.getNama_pasien());
                    update.putExtra("no_rm", bk.getNo_rm());
                    update.putExtra("penjamin", bk.getPenjamin());
                    update.putExtra("jns_daftar", bk.getJns_daftar());
                    update.putExtra("nama_poli", bk.getNama_poli());
                    update.putExtra("nama_lengkap", bk.getNama_lengkap());
                    update.putExtra("hari", bk.getHari());
                    update.putExtra("jam", bk.getJam());
                    context.startActivity(update);
                }
            });



        }

        private void updateStatusBooking() {



            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.UPDATE_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject obj = new JSONObject(response);

                            if (!obj.getBoolean("success")) {

                                Toast.makeText((context), "Pendaftaran Pasien sudah dihapus", Toast.LENGTH_LONG).show();


                                JSONArray poliArray = obj.getJSONArray("data");

                                for (int i = 0; i < poliArray.length(); i++) {

                                    JSONObject hObject = poliArray.getJSONObject(i);


                                    Booking bk = new Booking(

                                            hObject.getString("id_booking"),
                                            hObject.getString("nama_pasien"),
                                            hObject.getString("no_rm"),
                                            hObject.getString("no_antrian"),
                                            hObject.getString("no_barcode"),
                                            hObject.getString("penjamin"),
                                            hObject.getString("jns_daftar"),
                                            hObject.getString("nama_poli"),
                                            hObject.getString("nama_lengkap"),
                                            hObject.getString("tgl_booking"),
                                            hObject.getString("jam"),
                                            hObject.getString("hari")

                                    );

                                    mItems.add(bk);
                                }

                                tvNamaPasien.setText(bk.getNama_pasien());
                                tvNoRm.setText(bk.getNo_rm());
                                tvNoAntrian.setText(bk.getNo_antrian());
                                etNoBarcode.setText(bk.getNo_barcode());
                                tvNamaDokter.setText(bk.getNama_lengkap());
                                tvNamaPoli.setText(bk.getNama_poli());
                                tvTglBooking.setText(bk.getTgl_booking());
                                tvJnsdaftar.setText(bk.getJns_daftar());

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       //pb.setVisibility(View.GONE);
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id_booking", etIdBooking.getText().toString());


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(context);

        requestQueue.add(stringRequest);

        }



    }

    }




