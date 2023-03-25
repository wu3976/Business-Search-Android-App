package io.github.wu3976.csci571hw9;

import android.view.MotionEvent;

public interface OnItemTouchListener {
    void OnItemTouch(int position, ReservationRowModel item, MotionEvent motionEvent);
}
