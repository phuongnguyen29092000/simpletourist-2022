package com.example.simpletouristapp.ui.domestic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.simpletouristapp.MainActivity;
import com.example.simpletouristapp.MainActivityLogged;
import com.example.simpletouristapp.R;
import com.example.simpletouristapp.adapter.FeedBackAdapter;
import com.example.simpletouristapp.databinding.DetailTourBinding;
import com.example.simpletouristapp.model.FeedBackResponse;
import com.example.simpletouristapp.model.SendFeedbackResponse;
import com.example.simpletouristapp.model.Tour;
import com.example.simpletouristapp.model.TourResponse;
import com.example.simpletouristapp.service.FeedBacksApiService;
import com.example.simpletouristapp.service.ToursApiService;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTourFragment extends Fragment {
    private String tourId;
    private DetailTourBinding binding;
    private ToursApiService toursApiService;
    private List<SlideModel> slideModelList;
    private FeedBackAdapter feedBackAdapter;
    private RecyclerView rvFeedback;
    private FeedBacksApiService feedBacksApiService;
    private SharedPreferences sharedPreferences;
    private Integer status;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tourId = (String) getArguments().getSerializable("IdTour");
            status = (Integer) getArguments().getSerializable("Status");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = DetailTourBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentDate = Calendar.getInstance().getTime();
        String date = simpleDateFormat.format(currentDate);
        final Date[] date1 = {null};
        final Date[] date2 = {null};
        final Date[] date3 = {null};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

        rvFeedback = binding.rvFeedback;

        slideModelList = new ArrayList<>();

        toursApiService = new ToursApiService();

        feedBacksApiService = new FeedBacksApiService();

        sharedPreferences = getActivity().getSharedPreferences("Token", Context.MODE_PRIVATE);

        rvFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshFeedBack();

        Call<TourResponse> call = toursApiService.getTourByIdAPi(tourId);
        call.enqueue(new Callback<TourResponse>() {
            @Override
            public void onResponse(Call<TourResponse> call, Response<TourResponse> response) {
                if (response.code() == 200) {
                    TourResponse tourResponse = response.body();
                    Tour tour = tourResponse.data;
                    date2[0] = tour.getTimeStart();
                    Locale lc = new Locale("nv", "VN");
                    NumberFormat nf = NumberFormat.getCurrencyInstance(lc);
                    String pattern = "dd/MM/yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(tour.getNameTour());
                    slideModelList.add(new SlideModel(ToursApiService.BASE_URL + tour.getImageAvatar().substring(7), null));
                    for (String slide : tour.getImageSlide()
                    ) {
                        slideModelList.add(new SlideModel(ToursApiService.BASE_URL + slide.substring(7), null));
                    }
                    binding.nameTour.setText(tour.getNameTour());
                    binding.ratingTour.setRating(tour.getRating());
                    binding.imageSlideDetail.setImageList(slideModelList, ScaleTypes.CENTER_CROP);
                    binding.tvDetailPrice.setText(nf.format(tour.getPrice()));
                    binding.tvCompanyName.setText(tour.getOwner().getCompanyName());
                    if (tour.getDiscount() != 0) {
                        binding.tvDetailPrice.setPaintFlags(binding.tvDetailPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        binding.tvPriceAfterDiscount.setText(nf.format(tour.getPrice() * (1 - tour.getDiscount())));
                        binding.tvPriceAfterDiscount.setVisibility(View.VISIBLE);
                    }
                    binding.tvTimeDetail.setText(simpleDateFormat.format(tour.getTimeStart()) + " - " + simpleDateFormat.format(tour.getTimeEnd()));
                    binding.tvHotelDetail.setText(tour.getNameHotel());
                    binding.tvAmountDetail.setText(Integer.toString(tour.getAmount()));
                    binding.tvAmountRemainDetail.setText(Integer.toString(tour.getRemainingAmount()));
                    binding.tvTypePlace.setText(tour.getTypePlace().getName());
                    binding.description.setText(tour.getDescription());
                    binding.schedule.setText(tour.getSchedule());
                    if (tour.getOwner().isActive()) {
                        binding.tvActive.setText("Đang hoạt động");
                    } else {
                        binding.tvActive.setText("Dừng hoạt động");
                    }
                    if (getActivity().getClass() == MainActivityLogged.class) {
                        binding.edtEmail.setText(sharedPreferences.getString("email", ""));
                    }
                } else {
                    Toast.makeText(getContext(), "Wrong Id Tour", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TourResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", t.getMessage());
            }
        });
        Log.d("tourId", tourId);
        binding.btnBookTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    date1[0] = simpleDateFormat.parse(date);
                    date3[0] = simpleDateFormat.parse(simpleDateFormat.format(date2[0]));
                    long getDiff = date3[0].getTime() - date1[0].getTime();
                    long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
                    if (getDaysDiff < 5) {
                        builder.setTitle("Thời gian đặt vé đã hết");
                        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        if (binding.tvActive.getText().toString().equals("Dừng hoạt động")) {
                            builder.setTitle("Tour đã dừng hoạt động");
                            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                        } else {
                            if (getActivity().getClass() == MainActivity.class) {
                                builder.setTitle("Bạn cần phải đăng nhập trước khi đặt tour");
                                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Navigation.findNavController(view).navigate(R.id.action_nav_detail_tour_to_nav_login);
                                    }
                                });
                                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                            } else {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("tourName", binding.nameTour.getText().toString());
                                bundle.putSerializable("idTour", tourId);
                                bundle.putSerializable("price", binding.tvDetailPrice.getText().toString());
                                Navigation.findNavController(view).navigate(R.id.action_nav_detail_tour_to_nav_book_tour, bundle);
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        binding.seeMoreDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.description.setMaxLines(20);
                binding.seeMoreDescription.setVisibility(View.GONE);
                binding.hideDescription.setVisibility(View.VISIBLE);
            }
        });
        binding.hideDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.description.setMaxLines(2);
                binding.hideDescription.setVisibility(View.GONE);
                binding.seeMoreDescription.setVisibility(View.VISIBLE);
            }
        });

        binding.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.schedule.setMaxLines(20);
                binding.seeMore.setVisibility(View.GONE);
                binding.hide.setVisibility(View.VISIBLE);
            }
        });
        binding.hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.schedule.setMaxLines(2);
                binding.hide.setVisibility(View.GONE);
                binding.seeMore.setVisibility(View.VISIBLE);
            }
        });

        binding.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity().getClass() == MainActivity.class) {
                    builder.setTitle("Bạn cần phải đăng nhập trước khi đánh giá");
                    builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Navigation.findNavController(view).navigate(R.id.action_nav_detail_tour_to_nav_login);
                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    MainActivityLogged.getAccessInfo(getContext());
                    Call<SendFeedbackResponse> call1 = feedBacksApiService.sendFeedback(
                            "Bearer " + sharedPreferences.getString("access_token", ""), tourId
                            , sharedPreferences.getString("id_customer", ""), binding.edtComment.getText().toString()
                            , (int) binding.ratingFeedback.getRating());
                    call1.enqueue(new Callback<SendFeedbackResponse>() {
                        @Override
                        public void onResponse(Call<SendFeedbackResponse> call, Response<SendFeedbackResponse> response) {
                            if (response.code() == 201) {
                                refreshFeedBack();
                                builder.setTitle("Đánh giá thành công");
                                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });
                                builder.show();
                                binding.edtComment.setText("");
                            } else {
                                builder.setTitle("Bạn cần đặt tour và thanh toán trước khi đánh giá");
                                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();
                                Log.d("code", String.valueOf(response.code()));
                            }
                        }

                        @Override
                        public void onFailure(Call<SendFeedbackResponse> call, Throwable t) {
                            builder.setTitle("Đánh giá thất bại");
                            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                            Log.d("rate_error", t.getMessage());
                        }
                    });
                }
            }
        });
        try {
            if (status == 1) {
                binding.edtComment.setFocusable(true);
                binding.edtComment.requestFocus();
            }
        } catch (Exception e) {
        }
        return root;
    }

    public void refreshFeedBack() {
        Call<FeedBackResponse> call2 = feedBacksApiService.getFeedBackById(tourId);
        call2.enqueue(new Callback<FeedBackResponse>() {
            @Override
            public void onResponse(Call<FeedBackResponse> call, Response<FeedBackResponse> response) {
                if (response.code() == 200) {
                    FeedBackResponse feedBackResponse = response.body();
                    feedBackAdapter = new FeedBackAdapter(getContext(), feedBackResponse.getData());

                    rvFeedback.setAdapter(feedBackAdapter);
                }
            }

            @Override
            public void onFailure(Call<FeedBackResponse> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("feedbackerror", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
