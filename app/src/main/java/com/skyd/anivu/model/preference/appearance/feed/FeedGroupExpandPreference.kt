package com.skyd.anivu.model.preference.appearance.feed

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.skyd.anivu.base.BasePreference
import com.skyd.anivu.ext.dataStore
import com.skyd.anivu.ext.put
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object FeedGroupExpandPreference : BasePreference<Boolean> {
    private const val FEED_GROUP_EXPAND = "feedGroupExpand"
    override val default = true

    val key = booleanPreferencesKey(FEED_GROUP_EXPAND)

    fun put(context: Context, scope: CoroutineScope, value: Boolean) {
        scope.launch(Dispatchers.IO) {
            context.dataStore.put(key, value)
        }
    }

    override fun fromPreferences(preferences: Preferences): Boolean = preferences[key] ?: default
}
