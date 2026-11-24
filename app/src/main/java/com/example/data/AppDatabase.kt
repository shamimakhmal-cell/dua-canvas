package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [DuaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun duaDao(): DuaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "duacanvas_database"
                )
                    .addCallback(DuaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DuaDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDuas(database.duaDao())
                    }
                }
            }
        }

        suspend fun populateInitialDuas(dao: DuaDao) {
            val initialList = listOf(
                DuaEntity(
                    id = "dua_001",
                    title = "Dua for Rizq (Sustenance & Wealth)",
                    category = "Rizq & Debt",
                    arabic = "اللَّهُمَّ رَبَّنَا أَنْزِلْ عَلَيْنَا مَائِدَةً مِنَ السَّمَاءِ تَكُونُ لَنَا عِيدًا لِأَوَّلِنَا وَآخِرِنَا وَآيَةً مِنْكَ وَارْزُقْنَا وَأَنْتَ خَيْرُ الرَّازِقِينَ",
                    banglaPronunciation = "আল্লাহুম্মা রব্বানা আনযিল আলাইনা মা-ইদাতাম মিনাস সামা-ই তাকূনু লানা 'ঈদল লি-আওওয়ালিনা ওয়া আ-খিরিনা ওয়া আয়া তাম মিনকা ওয়ারযুকনা ওয়া আনতা খায়রুর রাযিকীন।",
                    banglaMeaning = "হে আমাদের রব! আমাদের প্রতি আসমান থেকে খাবার অবতীর্ণ করুন, যা আমাদের প্রথম ও শেষের সবার জন্য আনন্দ এবং আপনার পক্ষ থেকে একটি নিদর্শন হবে। আমাদের রিযিক দিন, আপনিই শ্রেষ্ঠ রিযিকদাতা।",
                    englishMeaning = "O Allah, our Lord, send down to us a table [spread with food] from the heaven to be for us a festival for the first of us and the last of us and a sign from You. And provide for us, and You are the best of providers.",
                    sourceType = "Quran",
                    sourceName = "Surah Al-Ma'idah",
                    reference = "Surah Al-Ma'idah (5:114)",
                    authenticity = "✓ Quran",
                    fazilat = "An answered supplication by Prophet Isa (AS) asking for blessed sustenance and divine provision.",
                    tags = "rizq, wealth, sustenance, food, quranic",
                    isFavorite = true,
                    collectionName = "Rizq"
                ),
                DuaEntity(
                    id = "dua_002",
                    title = "Dua for Relief from Debt",
                    category = "Rizq & Debt",
                    arabic = "اللَّهُمَّ اكْفِنِي بِحَلَالِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
                    banglaPronunciation = "আল্লাহুম্মাকফিনী বিহালালিকা ‘আন হারামিকা, ওয়া আগনিনী বিফাদলিকা ‘আম্মান সিওয়াক।",
                    banglaMeaning = "হে আল্লাহ! আপনার হালাল দ্বারা আমাকে আপনার হারামের বিরুদ্ধে যথেষ্ট করুন এবং আপনার দয়া দ্বারা আমাকে আপনার ছাড়া অন্য সবার থেকে অমুখাপেক্ষী করুন।",
                    englishMeaning = "O Allah, suffice me with Your lawful against Your unlawful, and enrich me by Your grace from anyone besides You.",
                    sourceType = "Hadith",
                    sourceName = "Jami At-Tirmidhi",
                    reference = "Tirmidhi 3563",
                    authenticity = "✓ Hasan Hadith",
                    fazilat = "The Prophet (ﷺ) advised Ali (RA) that even if one has debt like a mountain, Allah will pay it off for him with this dua.",
                    tags = "debt,rizq,wealth,tirmidhi",
                    isFavorite = false,
                    collectionName = null
                ),
                DuaEntity(
                    id = "dua_003",
                    title = "Comprehensive Daily Dua (Good in this World & Hereafter)",
                    category = "Daily Duas",
                    arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                    banglaPronunciation = "রব্বানা আ-তিনা ফিদ দুনইয়া হাসানাতাও ওয়া ফিল আ-খিরাতি হাসানাতাও ওয়া কিনা 'আযাবান নার।",
                    banglaMeaning = "হে আমাদের রব! আমাদের দুনিয়াতে কল্যাণ দিন এবং আখেরাতেও কল্যাণ দিন এবং আমাদের জাহান্নামের আজাব থেকে রক্ষা করুন।",
                    englishMeaning = "Our Lord, give us in this world [that which is] good and in the Hereafter [that which is] good and protect us from the punishment of the Fire.",
                    sourceType = "Quran",
                    sourceName = "Surah Al-Baqarah",
                    reference = "Surah Al-Baqarah (2:201)",
                    authenticity = "✓ Quran",
                    fazilat = "This was the most frequent dua made by the Prophet Muhammad (ﷺ). It encompasses all worldly and spiritual success.",
                    tags = "daily, success, quranic, hereafter",
                    isFavorite = true,
                    collectionName = "My Daily Duas"
                ),
                DuaEntity(
                    id = "dua_004",
                    title = "Morning & Evening Protection Dua",
                    category = "Morning & Evening",
                    arabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مع اسْمِهِ شَيْءٌ في الأرضِ وَلا في السَّماءِ وَهُوَ السَّمِيعُ العَلِيمُ",
                    banglaPronunciation = "বিসমিল্লাহিল্লাযী লা ইয়াদুররু মা'আসমিহী শাইউন ফিল আরদি ওয়া লা ফিস সামা-ই ওয়া হুওয়াস সামীউল 'আলীম।",
                    banglaMeaning = "আল্লাহর নামে, যাঁর নামের সাথে আসমান ও জমিনের কোনো কিছুতেই ক্ষতি করতে পারে না। তিনি সর্বশ্রোতা, সর্বজ্ঞ।",
                    englishMeaning = "In the name of Allah with Whose name nothing can cause harm in the earth nor in the heavens, and He is the All-Hearing, the All-Knowing.",
                    sourceType = "Hadith",
                    sourceName = "Abu Dawood & Tirmidhi",
                    reference = "Sunan Abi Dawud 5088",
                    authenticity = "✓ Sahih Hadith",
                    fazilat = "Whoever recites this three times in the morning and evening will not be harmed by anything.",
                    tags = "morning, evening, protection, safety",
                    isFavorite = true,
                    collectionName = "Protection"
                ),
                DuaEntity(
                    id = "dua_005",
                    title = "Dua for Protection from Anxiety and Sorrow",
                    category = "Anxiety & Difficulties",
                    arabic = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالحَزَنِ، وَأَعُوذُ بِكَ مِنَ العَجْزِ وَالكَسَلِ، وَأَعُوذُ بِكَ مِنَ الجُبْنِ وَالبُخْلِ، وَأَعُوذُ بِكَ من غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল হাম্মী ওয়াল হাযানি, ওয়া আ'ঊযু বিকা মিনাল 'আজযি ওয়াল কাসালি, ওয়া আ'ঊযু বিকা মিনাল জুবনি ওয়াল বুখলি, ওয়া আ'ঊযু বিকা মিন গাবাতিদ দাইনি ওয়া কহরির রিযাল।",
                    banglaMeaning = "হে আল্লাহ! আমি আপনার আশ্রয় নিচ্ছি দুশ্চিন্তা ও বেদনা থেকে, অপারগতা ও অলসতা থেকে, ভীরুতা ও কৃপণতা থেকে এবং ঋণের বোঝা ও মানুষের দমনপীড়ন থেকে।",
                    englishMeaning = "O Allah, I seek refuge in You from grief and sadness, from weakness and laziness, from miserliness and cowardice, and from being overcome by debt and overpowered by men.",
                    sourceType = "Hadith",
                    sourceName = "Sahih Al-Bukhari",
                    reference = "Sahih Bukhari 2893",
                    authenticity = "✓ Sahih Hadith",
                    fazilat = "The Prophet (ﷺ) used to say this dua frequently to relieve severe anxiety and burdens.",
                    tags = "anxiety, sadness, debt, difficulties",
                    isFavorite = false,
                    collectionName = null
                ),
                DuaEntity(
                    id = "dua_006",
                    title = "Dua for Forgiveness (Sayyidul Istighfar)",
                    category = "Forgiveness",
                    arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ من شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي، فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
                    banglaPronunciation = "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাকতানি ওয়া আনা 'আব্দুকা, ওয়া আনা 'আলা 'আহদিকা ওয়া ওয়াদিকা মাসতাতাতু... ফাগফিরলী ফাইন্নাহু লা ইয়াগফিরুয যুনூবা ইল্লা আনতা।",
                    banglaMeaning = "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া কোনো ইলাহ নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা... আমাকে ক্ষমা করুন, কারণ আপনি ছাড়া কেউ পাপ ক্ষমা করতে পারে না।",
                    englishMeaning = "O Allah, You are my Lord, there is none worthy of worship except You. You created me and I am Your slave...",
                    sourceType = "Hadith",
                    sourceName = "Sahih Al-Bukhari",
                    reference = "Sahih Bukhari 6306",
                    authenticity = "✓ Sahih Hadith",
                    fazilat = "Whoever recites this during the day with firm belief and dies before evening is from the people of Paradise; and whoever recites it at night and dies before morning is from them.",
                    tags = "forgiveness, istighfar, paradise, bukhari",
                    isFavorite = true,
                    collectionName = "My Daily Duas"
                ),
                DuaEntity(
                    id = "dua_007",
                    title = "Dua for Parents & Family",
                    category = "Family",
                    arabic = "رَبِّ ارْحَمْهُما كَما رَبَّيانِي صَغيرًا",
                    banglaPronunciation = "রব্বির হামহুমা কামা রাব্বা ইয়ানী সগীরা।",
                    banglaMeaning = "হে আমার রব! তাদের উভয়ের প্রতি রহম করুন যেভাবে তারা শৈশবে আমাকে লালন-পালন করেছিলেন।",
                    englishMeaning = "My Lord, have mercy upon them as they brought me up [when I was] small.",
                    sourceType = "Quran",
                    sourceName = "Surah Al-Isra",
                    reference = "Surah Al-Isra (17:24)",
                    authenticity = "✓ Quran",
                    fazilat = "A beautiful Quranic command and prayer for the forgiveness, health, and mercy upon parents.",
                    tags = "parents, family, quranic, mercy",
                    isFavorite = true,
                    collectionName = "Family"
                ),
                DuaEntity(
                    id = "dua_008",
                    title = "Dua for Breaking Fast in Ramadan",
                    category = "Ramadan",
                    arabic = "ذَهَبَ الظَّمَأُ وَابتَلَّتِ العُرُوقُ، وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
                    banglaPronunciation = "যাহাবায যামাউ ওয়াবতাল্লাতিল 'উরূকু ওয়া ছাবাতাল আজরু ইনশাআল্লাহ।",
                    banglaMeaning = "পিপাসা দূর হলো, শিরা-উপশিরা সিক্ত হলো এবং ইনশাআল্লাহ প্রতিদান নিশ্চিত হলো।",
                    englishMeaning = "The thirst is gone, the veins are moistened, and the reward is confirmed, if Allah wills.",
                    sourceType = "Hadith",
                    sourceName = "Sunan Abi Dawud",
                    reference = "Sunan Abi Dawud 2357",
                    authenticity = "✓ Hasan Hadith",
                    fazilat = "Recited immediately upon breaking the Ramadan fast or voluntary fast.",
                    tags = "ramadan, fast,iftar, sunnah",
                    isFavorite = false,
                    collectionName = "Ramadan"
                ),
                DuaEntity(
                    id = "dua_009",
                    title = "Dua for Travel",
                    category = "Travel",
                    arabic = "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ، وَإِنَّا إِلَى رَبِّنَا لَمُنْقَلِبُونَ",
                    banglaPronunciation = "সুবহানাল্লাযী সাকখারা লানা হা-যা ওয়া মা কুন্না লাহু মুকরিনীন, ওয়া ইন্না ইলা রব্বিনা লামুনকালিবূন।",
                    banglaMeaning = "পবিত্র সেই সত্তা যিনি এটাকে আমাদের অধীন করে দিয়েছেন, অথচ আমাদের তা নিয়ন্ত্রণ করার শক্তি ছিল না। আর নিশ্চয়ই আমরা আমাদের রবের দিকেই ফিরে যাবো।",
                    englishMeaning = "Glory is to Him who has subjected this to us, and we could not have otherwise subdued it. And indeed, to our Lord we will surely return.",
                    sourceType = "Quran",
                    sourceName = "Surah Az-Zukhruf",
                    reference = "Surah Az-Zukhruf (43:13-14)",
                    authenticity = "✓ Quran",
                    fazilat = "Recited when embarking on any vehicle or journey for safe travel protection.",
                    tags = "travel, vehicle, safety, quranic",
                    isFavorite = false,
                    collectionName = null
                ),
                DuaEntity(
                    id = "dua_010",
                    title = "Dua Before Sleeping",
                    category = "Sleep",
                    arabic = "بِاسْمِكَ اللَّهُمَّ أَمُوتُ وَأَحْيَا",
                    banglaPronunciation = "বিস্মিকা আল্লাহুম্মা আমূতু ওয়া আহ্ইয়া।",
                    banglaMeaning = "হে আল্লাহ! আপনার নামেই আমি মৃত্যুবরণ করি (ঘুমাই) এবং জীবিত হই (জাগি)।",
                    englishMeaning = "In Your name, O Allah, I die and I live.",
                    sourceType = "Hadith",
                    sourceName = "Sahih Al-Bukhari",
                    reference = "Sahih Bukhari 6324",
                    authenticity = "✓ Sahih Hadith",
                    fazilat = "Recited every night before going to sleep as taught by the Prophet (ﷺ).",
                    tags = "sleep, night, rest, bukhari",
                    isFavorite = true,
                    collectionName = "My Daily Duas"
                ),
                DuaEntity(
                    id = "dua_011",
                    title = "Dua for Entering and Leaving the Masjid",
                    category = "Salah",
                    arabic = "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ",
                    banglaPronunciation = "আল্লাহুম্মাফতাহ্লী আবওয়াবা রাহমাতিক।",
                    banglaMeaning = "হে আল্লাহ! আমার জন্য আপনার রহমতের দরজাগুলো খুলে দিন।",
                    englishMeaning = "O Allah, open for me the doors of Your mercy.",
                    sourceType = "Hadith",
                    sourceName = "Sahih Muslim",
                    reference = "Sahih Muslim 713",
                    authenticity = "✓ Sahih Hadith",
                    fazilat = "Recited when entering the mosque (and add 'and Your bounty' when leaving).",
                    tags = "masjid, salah, mosque, prayer",
                    isFavorite = false,
                    collectionName = null
                ),
                DuaEntity(
                    id = "dua_012",
                    title = "Dua for Knowledge & Wisdom",
                    category = "Quranic Duas",
                    arabic = "رَبِّ زِدْنِي عِلْمًا",
                    banglaPronunciation = "রব্বি যিদনী ‘ইলমা।",
                    banglaMeaning = "হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন।",
                    englishMeaning = "My Lord, increase me in knowledge.",
                    sourceType = "Quran",
                    sourceName = "Surah Ta-Ha",
                    reference = "Surah Ta-Ha (20:114)",
                    authenticity = "✓ Quran",
                    fazilat = "The specific prayer taught by Allah in the Quran for expanding beneficial knowledge.",
                    tags = "knowledge, quranic, wisdom, study",
                    isFavorite = true,
                    collectionName = "My Daily Duas"
                )
            )
            dao.insertAll(initialList)
        }
    }
}
