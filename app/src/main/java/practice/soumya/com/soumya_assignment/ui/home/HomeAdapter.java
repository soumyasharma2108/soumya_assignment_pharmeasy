package practice.soumya.com.soumya_assignment.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import practice.soumya.com.soumya_assignment.R;
import practice.soumya.com.soumya_assignment.models.Data;
import practice.soumya.com.soumya_assignment.networking.Constants;
import practice.soumya.com.soumya_assignment.ui.details.DetailsActivity;
import practice.soumya.com.soumya_assignment.utils.uiutility.PaginationAdapterCallback;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    static final int ITEM = 0;
    static final int LOADING = 1;
    static final int MAIN = 2;
    List<Data> resultsList;
    Activity mActivity;
    boolean isLoadingAdded = false;
    boolean retryPageLoad = false;

    PaginationAdapterCallback mCallback;

    String errorMsg;

    public HomeAdapter(Activity mActivity) {
        this.mActivity=mActivity;
        this.mCallback = (PaginationAdapterCallback) mActivity ;
        resultsList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new ListVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case MAIN:
                View viewHero = inflater.inflate(R.layout.item_main, parent, false);
                viewHolder = new MainVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Data result = resultsList.get(position); // Movie


        switch (getItemViewType(position)) {


            case MAIN:
                final MainVH mainVH = (MainVH) holder;
                mainVH.mFirstName.setText(result.getFirstName());
                mainVH.mLastName.setText(result.getLastName());
                loadImage(result.getAvatar()).into(mainVH.mPosterImg);

                break;

            case ITEM:

                final ListVH listVH = (ListVH) holder;
                listVH.mFirstName.setText(result.getFirstName());
                listVH.mLastName.setText(result.getLastName());

                // load movie thumbnail
                loadImage(result.getAvatar())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                listVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                listVH.mProgress.setVisibility(View.GONE);
                                return false;

                            }
                        })
                        .into(listVH.mPosterImg);
                listVH.listcard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, DetailsActivity.class);
                        intent.putExtra(Constants.Data, Parcels.wrap( result));
                        mActivity.startActivity(intent);
                        mActivity.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;
                if (retryPageLoad) {

                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    mActivity.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;

        }
    }

    @Override
    public int getItemCount() {
        return resultsList == null ? 0 : resultsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MAIN;
        } else {
            return (position == resultsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }
    }


    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here
     *
     * @return Glide builder
     */
    RequestBuilder<Drawable> loadImage(@NonNull String url) {
        return Glide.with(mActivity)
                .load(url); // cache both original & resized image


    }


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(Data r) {
        resultsList.add(r);
        notifyItemInserted(resultsList.size() - 1);
    }

    public void addAll(List<Data> moveResults) {
        for (Data result : moveResults) {
            add(result);
        }
    }

    public void remove(Data r) {
        int position = resultsList.indexOf(r);
        if (position > -1) {
            resultsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Data());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = resultsList.size() - 1;
        Data result = getItem(position);

        if (result != null) {
            resultsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Data getItem(int position) {
        return resultsList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(resultsList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class MainVH extends RecyclerView.ViewHolder {
        @BindView(R.id.firstname)
        TextView mFirstName;
        @BindView(R.id.lastname)
        TextView mLastName;
        @BindView(R.id.avatar)
        ImageView mPosterImg;

        public MainVH(View itemView) {
            super(itemView);
            // binding view
            ButterKnife.bind(this, itemView);

        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class ListVH extends RecyclerView.ViewHolder {
        @BindView(R.id.layout_main)
        FrameLayout listcard;
        @BindView(R.id.firstname)
        TextView mFirstName;
        @BindView(R.id.lastname)
        TextView mLastName;
        @BindView(R.id.avatar)
        ImageView mPosterImg;
        @BindView(R.id.img_progress)
        ProgressBar mProgress;

        public ListVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {
        @BindView(R.id.loadmore_progress)
        ProgressBar mProgressBar;
        @BindView(R.id.loadmore_retry)
        ImageButton mRetryBtn;
        @BindView(R.id.loadmore_errortxt)
        TextView mErrorTxt;
        @BindView(R.id.loadmore_errorlayout)
        LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.loadmore_errorlayout)
        public void onErrorLayoutClick() {
            mCallback.retryPageLoad();
        }
    }

}
