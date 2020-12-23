package com.incursio.newster

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.incursio.newster.news.data.Category
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
        assertEquals("com.incursio.newster", appContext.packageName)

        val article = Article(
            source = "None",
            author = "None",
            title = "Title",
            url = "url",
            description = "desc",
            urlToImage = "",
            content = "hola mundo",
            publishedAt = Date(),
            category = Category.LOCAL,
            id = "asd"
        )

        println(article)
        val parcel = Parcel.obtain()
        article.writeToParcel(parcel, article.describeContents())
    }
}