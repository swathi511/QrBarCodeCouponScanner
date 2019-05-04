package com.hjsoftware.qrbarcodecouponscanner.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.hjsoftware.qrbarcodecouponscanner.R;
import com.hjsoftware.qrbarcodecouponscanner.model.CouponHistory;
import java.util.List;

/**
 * Created by hjsoftware on 12/2/18.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private AdapterCallback mAdapterCallback;
    private List<CouponHistory> couponhistortyList;
    CouponHistory couponHistory;

    @Override

    public RecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.MyViewHolder holder, int position) {

        couponHistory = couponhistortyList.get(position);

        holder.couponcode.setText(couponHistory.getCouponcode());
        holder.couponamount.setText(couponHistory.getCouponamount());
        holder.custmername.setText(couponHistory.getCustomername());
        holder.customermobile.setText(couponHistory.getCustomermobile());


    }
    public RecyclerAdapter(Context context, List<CouponHistory> couponHistoryList, RecyclerView rView) {
        this.couponhistortyList = couponHistoryList;

//        try {
//            this.mAdapterCallback = ((AdapterCallback) context);
//        } catch (ClassCastException e) {
//            throw new ClassCastException("Activity must implement AdapterCallback.");
//        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView couponcode,couponamount,custmername,customermobile;
        RelativeLayout rlLayout;



        public MyViewHolder(View itemView) {
            super(itemView);

            couponcode=(TextView)itemView.findViewById(R.id.tv_coupon_value);
            couponamount=(TextView)itemView.findViewById(R.id.tv_coupon_amount);
            custmername=(TextView)itemView.findViewById(R.id.tv_customer_name);
            customermobile=(TextView)itemView.findViewById(R.id.tv_customer_mobile);
            rlLayout=(RelativeLayout)itemView.findViewById(R.id.ll_rlayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        String bookingId= (String) v.getTag();
                        mAdapterCallback.onMethodCallback(bookingId);

                    }
                    catch (ClassCastException e)
                    {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return couponhistortyList.size();
    }
    public static interface AdapterCallback {
        void onMethodCallback(String bookingId);
    }
}
