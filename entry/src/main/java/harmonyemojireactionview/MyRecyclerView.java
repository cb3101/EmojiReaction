package harmonyemojireactionview;

import com.example.emoji_reaction.ResourceTable;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.agp.components.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MyRecyclerView extends Fraction {

    public static BaseItemProvider mAdapter;
    ArrayList<Feed> feeds;

    public MyRecyclerView() {
        // Required empty public constructor
    }

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    */

    /*
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.my_recycler, container, false);
        RecyclerView mRecyclerView = view.findViewById(R.id.my_recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        feeds = new ArrayList<>();

        mAdapter = new FeedAdapter(feeds);

        feeds.add(new Feed("Adam", R.drawable.feed8, "Nov 6,11:52 AM", "The journey not the arrival matters.", -1));
        feeds.add(new Feed("Alina", R.drawable.feed9, "Nov 6,11:52 AM", "Just living is not enough...", -1));
        feeds.add(new Feed("James", R.drawable.feed4, "Nov 6,11:52 AM", "The dog is the perfect portrait subject. He doesn't pose. He isn't aware of the camera.", -1));
        feeds.add(new Feed("Emily", R.drawable.feed10, "Nov 6,11:52 AM", "Dream as if you’ll live forever, live as if you’ll die today.", -1));
        feeds.add(new Feed("Moore", R.drawable.feed3, "Nov 6,11:52 AM", "Quotes .... truth!! life", -1));
        feeds.add(new Feed("Thomson", R.drawable.feed5, "Nov 6,11:52 AM", "I spent 90 percent of my money on women and drink. The rest I wasted!", -1));
        feeds.add(new Feed("William", R.drawable.feed6, "Nov 6,11:52 AM", "Music is my medicine!!", -1));
        feeds.add(new Feed("Olivia", R.drawable.feed7, "Nov 6,11:52 AM", "It's refreshing to have some time off from wondering whether I look fat.", -1));
        feeds.add(new Feed("Sophia", R.drawable.feed1, "Nov 6,11:52 AM", "Adventure may hurt you but monotony will kill you.", -1));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
     */

   // @Override
    public Component onCreateView(@NotNull LayoutScatter inflater, ComponentContainer container){
        Component component = inflater.parse(ResourceTable.Layout_my_recycler, container, false);
        ListContainer listContainer = (ListContainer) component.findComponentById(ResourceTable.Id_my_recycler_view);

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        feeds = new ArrayList<>();

        mAdapter = new FeedAdapter(feeds);

        feeds.add(new Feed("Adam", ResourceTable.Media_feed8, "Nov 6,11:52 AM", "The journey not the arrival matters.", -1));
        feeds.add(new Feed("Alina", ResourceTable.Media_feed9, "Nov 6,11:52 AM", "Just living is not enough...", -1));
        feeds.add(new Feed("James", ResourceTable.Media_feed4, "Nov 6,11:52 AM", "The dog is the perfect portrait subject. He doesn't pose. He isn't aware of the camera.", -1));
        feeds.add(new Feed("Emily", ResourceTable.Media_feed10, "Nov 6,11:52 AM", "Dream as if you’ll live forever, live as if you’ll die today.", -1));
        feeds.add(new Feed("Moore", ResourceTable.Media_feed3, "Nov 6,11:52 AM", "Quotes .... truth!! life", -1));
        feeds.add(new Feed("Thomson", ResourceTable.Media_feed5, "Nov 6,11:52 AM", "I spent 90 percent of my money on women and drink. The rest I wasted!", -1));
        feeds.add(new Feed("William", ResourceTable.Media_feed6, "Nov 6,11:52 AM", "Music is my medicine!!", -1));
        feeds.add(new Feed("Olivia", ResourceTable.Media_feed7, "Nov 6,11:52 AM", "It's refreshing to have some time off from wondering whether I look fat.", -1));
        feeds.add(new Feed("Sophia", ResourceTable.Media_feed1, "Nov 6,11:52 AM", "Adventure may hurt you but monotony will kill you.", -1));

        listContainer.setItemProvider(mAdapter);
        listContainer.setLayoutManager(new DirectionalLayoutManager());

        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return component;
    }
}
