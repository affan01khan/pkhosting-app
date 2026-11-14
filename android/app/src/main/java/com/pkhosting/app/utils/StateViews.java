package com.pkhosting.app.utils;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Small helper that switches a screen between its four visual states:
 * loading spinner, list content, empty message and error message.
 * Every list screen uses the include_list_states layout, which contains
 * exactly these four views.
 */
public final class StateViews {

    public static void showLoading(ProgressBar progress, View content, View empty, View error) {
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    public static void showContent(ProgressBar progress, View content, View empty, View error) {
        progress.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
    }

    public static void showEmpty(ProgressBar progress, View content, View empty, View error) {
        progress.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
    }

    public static void showError(ProgressBar progress, View content, View empty, View error) {
        progress.setVisibility(View.GONE);
        content.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        error.setVisibility(View.VISIBLE);
    }

    private StateViews() {
        // No instances.
    }
}
