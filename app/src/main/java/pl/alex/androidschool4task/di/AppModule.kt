package pl.alex.androidschool4task.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import pl.alex.androidschool4task.data.AnimalDao
import pl.alex.androidschool4task.data.AnimalDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application, callBack: AnimalDatabase.CallBack): AnimalDatabase {
        return Room.databaseBuilder(app, AnimalDatabase::class.java, "animal_database")
            .fallbackToDestructiveMigration()
            .addCallback(callBack)
            .build()
    }
    @Provides
    fun provideAnimalDao(db: AnimalDatabase): AnimalDao{
        return db.animalDao()
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope