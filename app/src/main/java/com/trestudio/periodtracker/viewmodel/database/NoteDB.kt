package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*
import java.time.LocalDate

class SymptonBuilder() {
    private val output: Set<Symptom> = setOf()
    fun withCramps(): SymptonBuilder {
        output.plus(Symptom.Cramps)
        return this
    }

    fun withFatigue(): SymptonBuilder {
        output.plus(Symptom.Fatigue)
        return this
    }

    fun withMoodChanges(): SymptonBuilder {
        output.plus(Symptom.MoodChanges)
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
        fun builder(): SymptonBuilder {
            return SymptonBuilder()
        }

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
    Happy, Neutral, Sad
}

@Entity(tableName = "note")
@TypeConverters(LMPConverters::class, NoteConverters::class)
data class NoteDB(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val date: LocalDate,
    val symptom: Set<Symptom>,
    val painLevel: Int,
    val otherNote: String,
) {
    companion object {
        fun localDateToMonthAndString(input: LocalDate): Pair<String, String> {
            val month = input.monthValue.toString()
            val year = input.year.toString()
            return Pair(month, year)
        }
    }
}

@Dao
interface NoteDbDAO {
    @Insert
    suspend fun insert(date: NoteDB)

    @Query("SELECT * FROM note WHERE strftime('%m', date) = :month AND strftime('%Y', date) = :year")
    suspend fun getNotesForMonth(month: String, year: String): List<NoteDB>
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