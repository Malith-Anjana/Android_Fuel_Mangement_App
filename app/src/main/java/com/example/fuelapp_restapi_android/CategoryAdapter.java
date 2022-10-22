package com.example.fuelapp_restapi_android;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fuelapp_restapi_android.model.Category;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> category;
    private String url = "http://10.0.2.2:5109/api/students/";
    public CategoryAdapter(Context context, ArrayList<Category> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.category_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, final int position) {
        holder.title.setText(category.get(position).getCategory());
        holder.no.setText("#" + String.valueOf(position+1));

        holder.editCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = category.get(position).get_id();
                String value = category.get(position).getCategory();
                editCategory(id, value);
            }
        });
        holder.deleteCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = category.get(position).get_id();
                deleteCategory(id);
            }
        });
    }

    private void deleteCategory(final String id) {
        TextView close, judul;
        EditText cat;
        Button submit;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.delete_cat);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        judul = (TextView) dialog.findViewById(R.id.judul);

        judul.setText("Delete category");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        submit = (Button) dialog.findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit("DELETE", "", dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    //edit category

    private void editCategory(final String id, String value) {
        TextView close, judul;
        EditText cat;
        Button submit;
        final Dialog dialog;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.activity_modcat);

        close = (TextView) dialog.findViewById(R.id.txtClose);
        judul = (TextView) dialog.findViewById(R.id.judul);

        judul.setText("Edit category");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cat = (EditText) dialog.findViewById(R.id.cat);
        submit = (Button) dialog.findViewById(R.id.submit);

        cat.setText(value);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = cat.getText().toString();
                Submit("PUT", data, dialog, id);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void Submit(String method,final String data, final Dialog dialog, final String id) {
        if(method == "PUT") {
            Log.d("TESTM", url + id);

            try {
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", data);
                jsonBody.put("id", id);
                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.PUT, url + id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        category.clear();
                        dialog.dismiss();
                        Toast.makeText(context, "Data was Updated", Toast.LENGTH_SHORT).show();
                        Log.i("LOG_RESPONSE", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_RESPONSE", error.toString());
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                Volley.newRequestQueue(context).add(stringRequest);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


        } else if (method == "DELETE"){
            Log.d("TESTM", String.valueOf(id));
            StringRequest request = new StringRequest(Request.Method.DELETE, url + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    dialog.dismiss();
                    Intent intent = new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                    Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Data Deleted Failed", Toast.LENGTH_LONG).show();
                }
            });
            Volley.newRequestQueue(context).add(request);
        }
    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title, no;
        private ImageView editCat, deleteCat;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            no = (TextView) itemView.findViewById(R.id.no);
            title = (TextView) itemView.findViewById(R.id.nameCat);
            editCat = (ImageView)  itemView.findViewById(R.id.editCategory);
            deleteCat = (ImageView) itemView.findViewById(R.id.deleteCat);
        }
    }

}


