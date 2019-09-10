package com.example.kshitiz.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.kshitiz.helloworld.setting.Setting;
import com.example.kshitiz.helloworld.setting.SettingsConfig;
import com.example.kshitiz.helloworld.userpreference.UserPreferencesStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ActionsRequiredActivity extends Activity {
    private final UserPreferencesStore userPreferencesStore;
    private final SettingsConfig settingConfiguration;

    public ActionsRequiredActivity(){
        super();
        this.userPreferencesStore = new UserPreferencesStore();
        this.settingConfiguration = new SettingsConfig();
    }
    // Loop through whitelisted settings
    // For enabled settings display text area
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_actions_required);
        final RecyclerView recyclerView = findViewById(R.id.actions_requried_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final List<String> lists = new ArrayList<>();
        final Context context = getApplicationContext();
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        final Set<Setting> settings = userPreferencesStore.getSettingsEnabledForWatch(sharedPreferences);
        for(final Setting setting: settings) {
            if(settingConfiguration.getHandler(setting).isEnabled(context)) {
                lists.add(setting.toString());
            }
        }

        final RecyclerView.Adapter mAdapter = new ActionRequiredRecyclerAdapter(getApplicationContext(), lists);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

    }
}
