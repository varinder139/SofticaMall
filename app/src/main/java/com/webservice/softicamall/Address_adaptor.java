package com.webservice.softicamall;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webservice.softicamall.DeliveryActivity.SELECT_ADDRESS;
import static com.webservice.softicamall.MyAcountFragment.MANAGE_ADDRESS;
import static com.webservice.softicamall.MyAddressesActivity.refreshItem;

public class Address_adaptor extends RecyclerView.Adapter<Address_adaptor.ViewHolder> {

    private List<AddressesModel> addressesModelList;
    private int MODE;
    private int preSelectedPosition;
    private boolean refresh = false;
    private Dialog loadingDialog;

    public Address_adaptor(List<AddressesModel> addressesModelList, int MODE, Dialog loadingDialog) {
        this.addressesModelList = addressesModelList;
        this.MODE = MODE;
        preSelectedPosition = DBqueries.selectedAddress;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String city = addressesModelList.get(position).getCity();
        String locality = addressesModelList.get(position).getLocality();
        String flatNo = addressesModelList.get(position).getFlatNo();
        String pinode = addressesModelList.get(position).getPincode();
        String landmark = addressesModelList.get(position).getLandmark();
        String name = addressesModelList.get(position).getName();
        String mobileNo = addressesModelList.get(position).getMobileNo();
        String alternateMobileNo = addressesModelList.get(position).getAlternateMobileNo();
        String stateSpinner = addressesModelList.get(position).getState();
        boolean selected = addressesModelList.get(position).getSeleted();

        holder.setData(name, city, pinode, selected, position, mobileNo, alternateMobileNo, flatNo, locality, stateSpinner, landmark);
    }

    @Override
    public int getItemCount() {
        return addressesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView fullName, address, pinode;
        private ImageView icon;
        private LinearLayout optionContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            fullName = itemView.findViewById(R.id.ad_names);
            address = itemView.findViewById(R.id.ad_address);
            pinode = itemView.findViewById(R.id.ad_pincode);
            icon = itemView.findViewById(R.id.ad_icon_view);
            optionContainer = itemView.findViewById(R.id.option_container);
        }

        private void setData(String userName, String city, String userPincode, Boolean userSeleted, final int position, String mobileNo, String alternateMobileNo, String flatNo, String loaclity, String state, String landmark) {
            if (alternateMobileNo.equals("")) {
                fullName.setText(userName + " " + mobileNo);
            } else {
                fullName.setText(userName + " " + mobileNo + " or " + alternateMobileNo);
            }


            if (landmark.equals("")) {
                address.setText(flatNo + " " + loaclity + " " + city + " " + state);
            } else {
                address.setText(flatNo + " " + loaclity + " " + landmark + " " + city + " " + state);
            }
            pinode.setText(userPincode);

            if (MODE == SELECT_ADDRESS) {
                icon.setImageResource(R.drawable.check_black);
                if (userSeleted) {
                    icon.setVisibility(View.VISIBLE);
                    preSelectedPosition = position;
                } else {
                    icon.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (preSelectedPosition != position) {
                            addressesModelList.get(position).setSeleted(true);
                            addressesModelList.get(preSelectedPosition).setSeleted(false);
                            refreshItem(preSelectedPosition, position);
                            preSelectedPosition = position;
                            DBqueries.selectedAddress = position;
                        }
                    }
                });

            } else if (MODE == MANAGE_ADDRESS) {
                optionContainer.setVisibility(View.GONE);
                optionContainer.getChildAt(0).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { ///////////// Edit Address
                        Intent addAddressIntent = new Intent(itemView.getContext(), AddAddressActivity.class);
                        addAddressIntent.putExtra("INTENT", "update_address");
                        addAddressIntent.putExtra("index", position);
                        itemView.getContext().startActivity(addAddressIntent);

                        refresh = false;

                    }
                });
                optionContainer.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { ////// remove address

                        loadingDialog.show();

                        Map<String, Object> addresses = new HashMap<>();
                        int x = 0;
                        int selected = -1;
                        for (int i = 0; i < addressesModelList.size(); i++) {
                            if (i != position) {
                                x++;
                                addresses.put("city_" + x, addressesModelList.get(i).getCity());
                                addresses.put("locality_" + x, addressesModelList.get(i).getLocality());
                                addresses.put("flat_no_" + x, addressesModelList.get(i).getFlatNo());
                                addresses.put("pincode_" + x, addressesModelList.get(i).getPincode());
                                addresses.put("landmark_" + x, addressesModelList.get(i).getLandmark());
                                addresses.put("name_" + x, addressesModelList.get(i).getName());
                                addresses.put("mobile_no_" + x, addressesModelList.get(i).getMobileNo());
                                addresses.put("alternate_mobile_no_" + x, addressesModelList.get(i).getAlternateMobileNo());
                                addresses.put("state_" + x, addressesModelList.get(i).getState());

                                if (addressesModelList.get(position).getSeleted()) {
                                    if (position - 1 >= 0) {
                                        if (x == position) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        }else {
                                            addresses.put("selected_" + x, addressesModelList.get(i).getSeleted());
                                        }
                                    } else {
                                        if (x == 1) {
                                            addresses.put("selected_" + x, true);
                                            selected = x;
                                        } else {
                                            addresses.put("selected_" + x, addressesModelList.get(i).getSeleted());
                                        }
                                    }
                                } else {
                                    addresses.put("selected_" + x, addressesModelList.get(i).getSeleted());
                                if (addressesModelList.get(i).getSeleted()){
                                    selected = x;
                                }
                                }
                            }
                        }
                        addresses.put("list_size", x);
                        final int finalSelected = selected;
                        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_ADDRESSES")
                                .set(addresses).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    DBqueries.addressesModelList.remove(position);
                                    if (finalSelected != -1) {
                                        DBqueries.selectedAddress = finalSelected - 1;
                                        DBqueries.addressesModelList.get(finalSelected - 1).setSeleted(true);
                                    }else if (DBqueries.addressesModelList.size() == 0){
                                        DBqueries.selectedAddress = -1;
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                                loadingDialog.dismiss();
                            }
                        });
                        refresh = false;
                    }
                });
                icon.setImageResource(R.drawable.vert_dot);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionContainer.setVisibility(View.VISIBLE);
                        if (refresh) {
                            refreshItem(preSelectedPosition, preSelectedPosition);
                        } else {
                            refresh = true;
                        }
                        preSelectedPosition = position;
                    }
                });
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refreshItem(preSelectedPosition, preSelectedPosition);
                        preSelectedPosition = -1;
                    }
                });
            }
        }
    }
}
