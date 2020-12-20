package com.hodinkee.hodinnews

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.hodinkee.hodinnews", appContext.packageName)

        val article = Article(
            source = "None",
            author = "None",
            title = "Title",
            url = "url",
            description = "desc",
            urlToImage = "",
            content = "hola mundo",
            publishedAt = Date()
        )

        println(article)
        val parcel = Parcel.obtain()
        article.writeToParcel(parcel, article.describeContents())
    }
}