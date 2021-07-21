package harmonyemojireactionview;

import android.support.v7.widget.RecyclerView;
import com.example.emojireactionlibrary.EmojiReactionView;
import emojireaction.ClickInterface;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class is the adapter for displaying sample feeds
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {
    private ArrayList<Feed> mDataSet;
    private Context context;

    /*
    class ViewHolder extends RecyclerView.ViewHolder {
        Text messageTextView, timeTextView, name;
        //        ImageView photo;
        EmojiReactionView photo;

        private ViewHolder(View view) {
            super(view);
            photo = view.findViewById(R.id.photoImageView);
            messageTextView = view.findViewById(R.id.messageTextView);

            timeTextView = view.findViewById(R.id.time);
            name = view.findViewById(R.id.name);
        }
    }
     */

    class ViewHolder extends RecyclerView.ViewHolder {
        Text messageTextView, timeTextView, name;
        //        ImageView photo;
        EmojiReactionView photo;
        private ViewHolder(Component view) {
            super(view);
            photo = view.findComponentById(ResourceTable.id.photoImageView);
            messageTextView = view.findComponentById(R.id.messageTextView);

            timeTextView = view.findComponentById(R.id.time);
            name = view.findComponentById(R.id.name);
        }
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    FeedAdapter(ArrayList<Feed> myDataSet) {
        mDataSet = myDataSet;
    }

    // Create new views (invoked by the layout manager)
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ComponentContainer parent,
                                         int viewType) {
        // create a new view
//        Log.i("point fa50","create");
        Component v = LayoutBoost.inflate(parent.getContext(),R.layout.feed, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(v);
    }
    //TODO open and close circle anim on view creation

    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) {
        // - get element from your dataSet at this position
        // - replace the contents of the view with that element
//        Log.i("point fa50","bind");
        final Feed feed = mDataSet.get(position);
        holder.photo.setImageResource(feed.getPicAddress());
        holder.photo.setHomeEmojiVisible();
        holder.photo.setOnEmojiClickListener(new ClickInterface() {
            @Override
            public void onEmojiClicked(int emojiIndex, int x, int y) {
                String message;
                if (x != -1) {
                    switch (emojiIndex) {
                        case 0:
                            message = " Great!! ";
                            break;
                        case 1:
                            message = " Hehe ";
                            break;
                        case 2:
                            message = " Loved... ";
                            break;
                        case 3:
                            message = " Shocked!! ";
                            break;
                        case 4:
                            message = " Sad... ";
                            break;
                        case 5:
                            message = " Lit!! ";
                            break;
                        default:
                            message = " ** ";
                    }
                    ToastDialog toastDialog = new ToastDialog(holder.messageTextView.getContext());
                    toastDialog.setText(message).show();
                }
                feed.setClickedEmoji(emojiIndex);
            }

            @Override
            public void onEmojiUnclicked(int emojiIndex, int x, int y) {
//                if (x != -1)
//                    Toast.makeText(context, "Emoji " + emojiIndex +" removed", Toast.LENGTH_SHORT).show();
            }
        });

        // always after listener for changes
        holder.photo.setClickedEmojiNumber(feed.getClickedEmoji());

//        holder.photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.messageTextView.setText(feed.getMessage());
        holder.timeTextView.setText(feed.getTime());
        holder.name.setText(feed.getName());

    }

    // Return the size of your dataSet (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

}
