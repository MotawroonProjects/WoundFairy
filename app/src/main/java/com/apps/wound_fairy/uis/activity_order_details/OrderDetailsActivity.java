package com.apps.wound_fairy.uis.activity_order_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityOrderDetailsBinding;
import com.apps.wound_fairy.model.OrderModel;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_send_order.SendOrderActivity;
import com.squareup.picasso.Picasso;

public class OrderDetailsActivity extends BaseActivity {

    private ActivityOrderDetailsBinding binding;
    private OrderModel orderModel;
    private ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        orderModel = (OrderModel) intent.getSerializableExtra("data");
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.orderDetails), R.color.white, R.color.black);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        binding.setModel(orderModel);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                orderModel = (OrderModel) result.getData().getSerializableExtra("data");
                binding.setModel(orderModel);
            }

        Spannable word = new SpannableString(orderModel.getTotal_price());

        word.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPrice.setText(word);
        Spannable wordTwo = new SpannableString(getResources().getString(R.string.egp));

        wordTwo.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, wordTwo.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvPrice.append(wordTwo);
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDetailsActivity.this, SendOrderActivity.class);
                intent.putExtra("order", orderModel);
                launcher.launch(intent);

            }
        });
    }
}