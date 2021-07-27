package harmonyemojireactionview;

import android.support.v4.view.ViewPager;
import com.example.emoji_reaction.ResourceTable;
import harmonyemojireactionview.slice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.ability.fraction.FractionManager;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.ShapeElement;

import java.util.ArrayList;
import java.util.List;

public class MainAbility extends Ability {
    private TabList tabLayout;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        setupViewPager((ViewPager) findComponentById(viewpager));
        tabLayout = (TabList) findComponentById(ResourceTable.Id_tabs);
        setupTabIcons();
    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIconElement( ResourceTable.Media_baseline_image_white_24);
        tabLayout.getTabAt(1).setIconElement(ResourceTable.Media_outline_view_day_white_24);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFractionManager());
        adapter.addFraction(new SimpleExample(), "Simple");
        adapter.addFraction(new MyRecyclerView(), "Recycler view");
        viewPager.setAdapter(adapter);
    }

    private void setupPageSlider(PageSlider pageSlider) {
        PageSliderProvider pageSliderProvider = null;
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFractionManager());
        pageSliderProvider.
        adapter.addFraction(new SimpleExample(), "Simple");
        adapter.addFraction(new MyRecyclerView(), "Recycler view");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FractionPagerAdapter, BaseItemProvider {
        private final List<Fraction> mFractionList = new ArrayList<>();
        private final List<String> mFractionTitleList = new ArrayList<>();

        ViewPagerAdapter(FractionManager manager) {
            super(manager);
        }

        @Override
        public Fraction getItem(int position) {
            return mFractionList.get(position);
        }

        @Override
        public int getCount() {
            return mFractionList.size();
        }

        void addFraction(Fraction Fraction, String title) {
            mFractionList.add(Fraction);
            mFractionTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFractionTitleList.get(position);
        }
    }
}


