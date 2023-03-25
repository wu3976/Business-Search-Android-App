package io.github.wu3976.csci571hw9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetailViewPagerAdapter extends FragmentStateAdapter {
    private Bundle bundle;
    public DetailViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, Bundle bundle_in) {
        super(fragmentActivity);
        this.bundle = bundle_in;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: { return detail_frag_1.newInstance(bundle); }
            case 1: { return detail_frag_2.newInstance(bundle); }
            case 2: { return detail_frag_3.newInstance(bundle); }
            default: { return detail_frag_1.newInstance(bundle); }
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
