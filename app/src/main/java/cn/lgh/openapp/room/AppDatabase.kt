package cn.lgh.openapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cn.lgh.openapp.bean.SearchInfo

/**
 * @author lgh
 * @date 2021/6/29
 *
 */
@Database(entities = [SearchInfo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchDao(): SearchDao

    companion object {
        private var instance: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "search.db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build()
            }
            return instance as AppDatabase
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }


}

