/*
 * ASTRID: Android's Simple Task Recording Dashboard
 *
 * Copyright (c) 2009 Tim Su
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package com.todoroo.andlib.widget;

import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.todoroo.andlib.service.Autowired;
import com.todoroo.andlib.service.DependencyInjectionService;

/** Dialog box with an arbitrary number of number pickers */
public class NNumberPickerDialog extends AlertDialog implements OnClickListener {

    @Autowired
    private Integer nNumberPickerLayout;

    public interface OnNNumberPickedListener {
        void onNumbersPicked(int[] number);
    }

    private final List<NumberPickerWidget>      pickers = new LinkedList<NumberPickerWidget>();
    private final OnNNumberPickedListener mCallback;

    /** Instantiate the dialog box.
     *
     * @param context
     * @param callBack callback function to get the numbers you requested
     * @param title title of the dialog box
     * @param initialValue initial picker values array
     * @param incrementBy picker increment by array
     * @param start picker range start array
     * @param end picker range end array
     * @param separators text separating the spinners. whole array, or individual
     *        elements can be null
     */
    public NNumberPickerDialog(Context context, OnNNumberPickedListener callBack,
            String title, int[] initialValue, int[] incrementBy, int[] start,
            int[] end, String[] separators) {
        super(context);
        mCallback = callBack;

        DependencyInjectionService.getInstance().inject(this);

        setButton(context.getText(android.R.string.ok), this);
        setButton2(context.getText(android.R.string.cancel), (OnClickListener) null);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(nNumberPickerLayout, null);
        setView(view);
        LinearLayout container = (LinearLayout)view;

        setTitle(title);
        LayoutParams npLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.FILL_PARENT);
        npLayout.gravity = 1;
        LayoutParams sepLayout = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.FILL_PARENT);
        for(int i = 0; i < incrementBy.length; i++) {
            NumberPickerWidget np = new NumberPickerWidget(context, null);
            np.setIncrementBy(incrementBy[i]);
            np.setLayoutParams(npLayout);
            np.setRange(start[i], end[i]);
            np.setCurrent(initialValue[i]);

            container.addView(np);
            pickers.add(np);

            if(separators != null && separators[i] != null) {
                TextView text = new TextView(context);
                text.setText(separators[i]);
                if(separators[i].length() < 3)
                    text.setTextSize(48);
                else
                    text.setTextSize(20);
                text.setGravity(Gravity.CENTER);
                text.setLayoutParams(sepLayout);
                container.addView(text);
            }
        }
    }

    public void setInitialValues(int[] values) {
        for(int i = 0; i < pickers.size(); i++)
            pickers.get(i).setCurrent(values[i]);
    }

    public void onClick(DialogInterface dialog, int which) {
        if (mCallback != null) {
            int[] values = new int[pickers.size()];
            for(int i = 0; i < pickers.size(); i++) {
                pickers.get(i).clearFocus();
                values[i] = pickers.get(i).getCurrent();
            }
            mCallback.onNumbersPicked(values);
        }
    }
}
