package in.dropcodes.npuser.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.dropcodes.npuser.Model.MainModel;
import in.dropcodes.npuser.ParkingDetailsActivity;
import in.dropcodes.npuser.R;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder>{

    private Context context;
    private List<MainModel> mainModels;

    public MainAdapter(Context context, List<MainModel> mainModels) {
        this.context = context;
        this.mainModels = mainModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final MainModel model = mainModels.get(position);
        holder.mName.setText("Parking Name:"+" "+model.getName());
        holder.mPlace.setText("Parking Area:"+" "+model.getArea());
        //Loading Image
        Picasso.get().load(model.getImage()).fit().centerInside().placeholder(R.drawable.placeholder).into(holder.mImage);
        final String uid = model.getUid();
        //Handling ItemClick
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ParkingDetailsActivity.class);
                intent.putExtra("uid",uid);

                //adding Shared animation
                Pair[] pairs = new Pair[4];
                pairs[0]=new Pair<View , String>(holder.mImage,"ParkImage");
                pairs[1]=new Pair<View , String>(holder.mName,"ParkName");
                pairs[2]=new Pair<View , String>(holder.mPlace,"ParkAddress");
                pairs[3]=new Pair<View , String>(holder.mCard,"ParkCard");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) context,pairs);
                context.startActivity(intent, options.toBundle()) ;

            }
        });

    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mName, mPlace;
        public CircleImageView mImage;
        public LinearLayout mLinearLayout;
        public CardView mCard;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.parking_name);
            mPlace = itemView.findViewById(R.id.parking_place);
            mImage = itemView.findViewById(R.id.parking_image);
            mLinearLayout = itemView.findViewById(R.id.linear_layout);
            mCard = itemView.findViewById(R.id.card_view);
        }
    }
}
