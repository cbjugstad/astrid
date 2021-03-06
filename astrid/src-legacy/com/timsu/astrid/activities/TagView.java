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
package com.timsu.astrid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * This activity is launched from a desktop shortcut and takes the user to
 * view a specific tag
 *
 * @author timsu
 *
 */
public class TagView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launchTaskList(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        launchTaskList(intent);
    }

    private void launchTaskList(Intent intent) {
        String tag = intent.getData().toString();
        long tagId = Long.parseLong(tag.substring(tag.indexOf(":")+1));

        Bundle variables = new Bundle();
        variables.putLong(TaskListSubActivity.TAG_TOKEN, tagId);

        Intent taskListIntent = new Intent(this, TaskList.class);
        taskListIntent.putExtra(TaskList.VARIABLES_TAG, variables);
        startActivity(taskListIntent);

        finish();
    }
}
