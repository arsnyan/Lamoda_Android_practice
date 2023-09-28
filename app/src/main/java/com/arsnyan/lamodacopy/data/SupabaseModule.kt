package com.arsnyan.lamodacopy.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {
    @Provides
    fun provideSupabaseClient(): SupabaseClient {
        val client = createSupabaseClient(
            supabaseUrl = "https://fxgvgfeszwjzkysmnnzv.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ4Z3ZnZmVzendqemt5c21ubnp2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE2OTU5MTA2NjcsImV4cCI6MjAxMTQ4NjY2N30.jsszu0PXN1Qz2382lVpszfzY_LI25tHQTfbM3zmk1p8",
        ) {
            install(Postgrest)
        }
        return client
    }

    @Provides
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }
}