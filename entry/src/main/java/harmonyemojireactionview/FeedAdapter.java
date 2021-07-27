package harmonyemojireactionview;

//import android.support.v7.widget.RecyclerView;


import com.example.emoji_reaction.ResourceTable;
import com.ritik.emojireactionlibrary.ClickInterface;
import com.ritik.emojireactionlibrary.EmojiReactionView;
import ohos.agp.components.BaseItemProvider;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.Text;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.window.dialog.ToastDialog;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

/
public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder>, BaseItemProvider {
    private ArrayList<Feed> mDataSet;
    private Context context;

    FeedAdapter(ArrayList<Feed> myDataSet) {
        mDataSet = myDataSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Text messageTextView, timeTextView, name;
        EmojiReactionView photo;
        private ViewHolder(Component view) {
            super(view);

            photo = view.findComponentById(ResourceTable.R.id.photoImageView);
            messageTextView = view.findComponentById(R.id.messageTextView);
            timeTextView = view.findComponentById(R.id.time);
            name = view.findComponentById(R.id.name);
        }
    }


    /*
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(v);
    }
    */
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ComponentContainer parent, int viewType) {
        Component v = LayoutBoost.inflate(parent.getContext(),ResourceTable.Layout_feed, parent, false);
        this.context = parent.getContext();
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NotNull final ViewHolder holder, final int position) throws NotExistException, WrongTypeException, IOException {
        final Feed feed = mDataSet.get(position);
        holder.photo.setPixelMap(feed.getPicAddress());
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
//
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

    @Override
    public int getCount() {
        return mDataSet.size();
    }

}
