package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*
import java.time.LocalDate

class SymptonBuilder {
    private val output: MutableSet<Symptom> = mutableSetOf()
    fun withCramps(): SymptonBuilder {
        output.add(Symptom.Cramps)
        return this
    }

    fun withFatigue(): SymptonBuilder {
        output.add(Symptom.Fatigue)
        return this
    }

    fun withMoodChanges(): SymptonBuilder {
        output.add(Symptom.MoodChanges)
        return this
    }

    fun build(): Set<Symptom> {
        return output
    }
}

enum class Symptom {
    Cramps,
    Fatigue,
    MoodChanges;

    companion object {
        fun match(input: String): Symptom? {
            return when (input) {
                "Cramps" -> Cramps
                "Fatigue" -> Fatigue
                "MoodChanges" -> MoodChanges
                else -> null
            }
        }
    }
}

enum class Mood {
    Happy, Neutral, Sad;
}

@Entity(tableName = "note")
@TypeConverters(LMPConverters::class, NoteConverters::class)
data class NoteDB(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    var symptom: Set<Symptom>,
    var mood: Mood,
    var painLevel: Int,
    var otherNote: String,
)

@Dao
interface NoteDbDAO {
    @Insert
    suspend fun insert(date: NoteDB)

    @Query("SELECT * FROM note WHERE date BETWEEN :start AND :end")
    suspend fun getMonthlyNotes(start: LocalDate, end: LocalDate): List<NoteDB>

    @Update
    suspend fun update(note: NoteDB)

    @Delete
    suspend fun delete(note: NoteDB)
}

class NoteConverters {
    @TypeConverter
    fun fromSymptomSet(symptoms: Set<Symptom>): String {
        return symptoms.joinToString(",") { it.name }
    }

    @TypeConverter
    fun toSymptomSet(symptomsString: String): Set<Symptom> {
        return symptomsString.split(",").mapNotNull { Symptom.match(it) }.toSet()
    }
}