package com.example.upi.inappupi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UPIIntentFlowBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private static final String UPI_APP_LIST = "upi_app_list";
    private static final int UPI_REQ_CODE = 1991;
    private static final String UPI_APP_PKG_NAME = "com.google.android.apps.nbu.paisa.user";
    private Button btnUPIProceed;
    //private Listener mListener;
    //CoordinatorLayout.Behavior layoutBehavior;
    //View contentView;

    BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View view, int newState) {
            if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View view, float v) {

        }
    };

    public static UPIIntentFlowBottomSheetDialogFragment newInstance(List<UPIAppInfo> upiAppInfoList) {
        final UPIIntentFlowBottomSheetDialogFragment fragment = new UPIIntentFlowBottomSheetDialogFragment();
        final Bundle args = new Bundle();
        args.putParcelableArrayList(UPI_APP_LIST, (ArrayList<? extends Parcelable>) upiAppInfoList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return /*contentView = */inflater.inflate(R.layout.fragment_item_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();

/*
        Float density = getContext().getResources().getDisplayMetrics().density;
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
         layoutBehavior = layoutParams.getBehavior();*/

        //setting default height for smaller devices
/*        if (density == 1.0) {
            layoutHeightDevice = 0.50;
        } else {
            layoutHeightDevice = 0.35;
        }*/


/*        if (layoutBehavior != null && layoutBehavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) layoutBehavior).setBottomSheetCallback(bottomSheetCallback);
            ((BottomSheetBehavior) layoutBehavior).setHideable(false);
            ((BottomSheetBehavior) layoutBehavior).setState(BottomSheetBehavior.STATE_EXPANDED);
            //((BottomSheetBehavior) layoutBehavior).setPeekHeight((int) Math.ceil(BMSUiUtility.getDeviceHeight(getContext()) * (layoutHeightDevice)));
        }*/


        List<UPIAppInfo> upiAppInfoList = getArguments().getParcelableArrayList(UPI_APP_LIST);
        final UPIAppListingRecyclerViewAdapter upiAppListingRecyclerViewAdapter = new UPIAppListingRecyclerViewAdapter(upiAppInfoList);
        final RecyclerView recyclerView = view.findViewById(R.id.rv_popular_upi_apps);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4, GridLayoutManager.VERTICAL  , false));
        recyclerView.setAdapter(upiAppListingRecyclerViewAdapter);

        btnUPIProceed = view.findViewById(R.id.btn_upi_proceed);
        final TextView tvOtherUpiApp = view.findViewById(R.id.tv_all_upi_app_label);
        final TextView tvSelectUpiApp = view.findViewById(R.id.tv_select_upi_app_label);
        tvOtherUpiApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL  , false));
                //upiAppListingRecyclerViewAdapter.addOrUpdateAdapter(30);
                tvOtherUpiApp.setVisibility(View.GONE);
                tvSelectUpiApp.setText("All UPI apps");
                btnUPIProceed.setBackgroundColor(getResources().getColor(R.color.design_fab_shadow_start_color));
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //final Fragment parent = getParentFragment();
       /* if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }*/
    }

    @Override
    public void onDetach() {
        //mListener = null;
        super.onDetach();
    }


    public void redirectToSelectedUPIApp(String upiAppPackageName) {
        String address = ""/* payAddress.getText().toString().trim()*/;
        address = TextUtils.isEmpty(address) ? "deepaknitb24@okicici" : address;

        String amount = ""/* payAmount.getText().toString().trim()*/;
        amount = TextUtils.isEmpty(amount) ? "01" : amount;

        String description = ""/* payDescription.getText().toString().trim()*/;
        description = TextUtils.isEmpty(description) ? "Pay for UPI testing" : description;

        Uri uri = new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", address) // payment address
                .appendQueryParameter("pn", "Deepak Mandhani")
                //.appendQueryParameter("mc", "1234")
                //.appendQueryParameter("tr", "123456789")
                .appendQueryParameter("tn", description) // payment description/note
                .appendQueryParameter("am", amount) // payment amount
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("url", "https://test.merchant.website")
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);

        // for specific app like G-PAy
        intent.setPackage(upiAppPackageName);
        startActivityForResult(intent, UPI_REQ_CODE);

        //Intent intentChooser = Intent.createChooser(intent, "Pay with below UPI apps-");
        if(intent.resolveActivity(getContext().getPackageManager()) != null) {
            //startActivityForResult(intentChooser, UPI_REQ_CODE);
            startActivityForResult(intent, UPI_REQ_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPI_REQ_CODE) {
            Toast.makeText(getContext(), data != null ? data.getStringExtra("Status") : "NoStatusFound :(", Toast.LENGTH_SHORT).show();
            String returnData = data !=null ? data.getDataString() : "NoDataFound :(";
            Log.d(getTag(), "-" + resultCode + " : " + returnData );
        }
    }

    public interface Listener {
        void onItemClicked(int position);
    }

    private class UPIAppListingRecyclerViewAdapter extends RecyclerView.Adapter<UPIAppListingRecyclerViewAdapter.UPIAppItemViewHolder> {

        private List<UPIAppInfo> upiAppInfoList;

        UPIAppListingRecyclerViewAdapter(List<UPIAppInfo> upiAppInfoList) {
            this.upiAppInfoList = upiAppInfoList;
        }

        @Override
        public UPIAppItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UPIAppItemViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(UPIAppItemViewHolder holder, int position) {
            UPIAppInfo upiAppInfo = upiAppInfoList.get(position);
            holder.tvUPIAppName.setText(upiAppInfo.appName);
            holder.ivUPIAppIcon.setImageDrawable(getDrawableFromPackageName(upiAppInfo.packageName));
        }

        @Override
        public int getItemCount() {
            return upiAppInfoList != null ? upiAppInfoList.size() : 0;
        }

        public void addOrUpdateAdapter(List<UPIAppInfo> appInfoList) {
            if(upiAppInfoList != null && !upiAppInfoList.isEmpty()) {
                upiAppInfoList.clear();
            }
            upiAppInfoList = appInfoList;
            notifyDataSetChanged();
        }

        private Drawable getDrawableFromPackageName(String packageName) {
            try {
                return getActivity().getPackageManager().getApplicationIcon(packageName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return getActivity().getResources().getDrawable(R.drawable.ic_launcher_background);
        }

        public class UPIAppItemViewHolder extends RecyclerView.ViewHolder {

            final TextView tvUPIAppName;
            final ImageView ivUPIAppIcon;

            UPIAppItemViewHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.upi_app_list_item, parent, false));

                ivUPIAppIcon = itemView.findViewById(R.id.iv_upi_app_icon);
                tvUPIAppName = itemView.findViewById(R.id.tv_upi_app_name);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //if (mListener != null) {
                        //    mListener.onItemClicked(getAdapterPosition());
                        //dismiss();
                        //}
                        redirectToSelectedUPIApp(upiAppInfoList.get(getAdapterPosition()).packageName);
                        btnUPIProceed.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        //todo- keep selected adapter position- getAdapterPosition()
                    }
                });
            }
        }

    }
}
