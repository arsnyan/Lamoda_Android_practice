package com.arsnyan.lamodacopy

import android.app.Application
import android.content.res.Configuration
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

@HiltAndroidApp
class LamodaApplication : Application()