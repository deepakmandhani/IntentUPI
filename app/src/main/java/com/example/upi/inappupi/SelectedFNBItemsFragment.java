/*
package com.example.upi.inappupi;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.bms.models.fnb.FnBData;
import com.movie.bms.R;
import com.movie.bms.mvp.presenters.FNBGrabBitePresenter;
import com.movie.bms.mvp.views.IUpdateFragment;
import com.movie.bms.utils.BMSUiUtility;
import com.movie.bms.utils.BMSUtility;
import com.bms.common.utils.customcomponents.CustomTextView;
import com.movie.bms.views.activities.FnBGrabABiteActivity;
import com.movie.bms.views.adapters.FnBGrabBiteSelectedItemsAdapter;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectedFNBItemsFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    public static final String SELECTED_ITEMS_KEY = "SELECTED_ITEMS_KEY";
    public FnBGrabBiteSelectedItemsAdapter adapter;
    @BindView(R.id.fnb_quantity_you_save_layout)
    RelativeLayout itemQuantityYouSaveLayout;
    @BindView(R.id.fnb_pay_layout)
    RelativeLayout amoutPayLayout;
    @BindView(R.id.fnb_total_item_quantity)
    CustomTextView mFNBItemCount;
    @BindView(R.id.fnb_summary_activity_tv_pay_amt)
    CustomTextView mFNBTotalAmount;
    @BindView(R.id.fnb_you_save_text)
    CustomTextView mFNBSaveText;
    @BindView(R.id.fnb_you_save_rs)
    CustomTextView mFNBSaveAmount;
    @BindView(R.id.fnb_selected_item_rv)
    RecyclerView fnbSelectedItemRv;
    CoordinatorLayout.Behavior layoutBehavior;
    IUpdateFragment iUpdateFragment;
    BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
            if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                if (layoutBehavior != null && layoutBehavior instanceof BottomSheetBehavior) {
                    // ((BottomSheetBehavior) layoutBehavior).setState(BottomSheetBehavior.STATE_EXPANDED);
                    //  dismiss();
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }


    };
    private FNBGrabBitePresenter fnbPresenter;
    private List<FnBData> selectedItemsList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedItemsList = (List<FnBData>) Parcels.unwrap(getArguments().getParcelable(SELECTED_ITEMS_KEY));
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getActivity(), R.layout.fragment_selected_fnb_items, null);
        dialog.setContentView(contentView);
        ButterKnife.bind(this, contentView);
        itemQuantityYouSaveLayout.setOnClickListener(this);
        amoutPayLayout.setOnClickListener(this);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        layoutBehavior = params.getBehavior();
        if (layoutBehavior != null && layoutBehavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) layoutBehavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            ((BottomSheetBehavior) layoutBehavior).setHideable(false);
            ((BottomSheetBehavior) layoutBehavior).setPeekHeight(BMSUiUtility.dpToPx(getActivity(), 400));
        }

        fnbSelectedItemRv.setHasFixedSize(true);
        fnbSelectedItemRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        fnbSelectedItemRv.setNestedScrollingEnabled(true);

        fnbPresenter = ((FnBGrabABiteActivity) getActivity()).mFnbPresenter;
        adapter = new FnBGrabBiteSelectedItemsAdapter(getActivity(), selectedItemsList, fnbPresenter);
        fnbSelectedItemRv.setAdapter(adapter);
        calculateTotalSavingandPrice();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fnb_quantity_you_save_layout:

                dismiss();
                break;

            case R.id.fnb_pay_layout:

                dismiss();
                ((FnBGrabABiteActivity) getActivity()).showPickUpSeatDeliveryOptions();
                break;

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            iUpdateFragment = (IUpdateFragment) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iUpdateFragment.update();
    }

    public void calculateTotalSavingandPrice() {
        float totalPrice = 0;
        float totalSave = 0;
        for (FnBData fnbData : selectedItemsList
                ) {
            totalPrice += Float.parseFloat(fnbData.getItemSell()) * fnbData.getTotalCount();
            if (!fnbData.getDisplayPrice().trim().equalsIgnoreCase("0")) {
                totalSave += fnbData.getItemDiscount() * fnbData.getTotalCount();
            }
        }
        mFNBTotalAmount.setText(getResources().getString(R.string.fnb_price_with_rupee_symbol,
                BMSUtility.formatRupeeValue(String.valueOf(totalPrice))));
        int noOfFnbItems = ((FnBGrabABiteActivity) getActivity()).noOfItemsSelected;
        mFNBItemCount.setText(String.format(getResources().getQuantityString(R.plurals.fnb_bottom_no_of_items, noOfFnbItems), noOfFnbItems));

        if (totalSave > 0) {
            mFNBSaveText.setVisibility(View.VISIBLE);
            mFNBSaveAmount.setVisibility(View.VISIBLE);
            mFNBSaveAmount.setText(getResources().getString(R.string.fnb_price_with_rupee_symbol,
                    BMSUtility.formatRupeeValue(String.valueOf(totalSave))));
        } else {
            mFNBSaveText.setVisibility(View.GONE);
            mFNBSaveAmount.setVisibility(View.GONE);
        }
    }
}*/
