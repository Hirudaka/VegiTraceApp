import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LocationUpdateService(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val locationRef: DatabaseReference = database.getReference("locations")

    override fun doWork(): Result {
        val latitude = inputData.getDouble("latitude", 0.0)
        val longitude = inputData.getDouble("longitude", 0.0)
        val timestamp = System.currentTimeMillis()

        val locationData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude,
            "timestamp" to timestamp
        )

        locationRef.push().setValue(locationData)

        return Result.success()
    }
}
